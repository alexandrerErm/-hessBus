import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartScreenController extends MainController implements Initializable {
	//FXML labels 
	@FXML
	private Label usernameMenu;
	@FXML
	private Label balanceMenu;
	@FXML
	private Label welcome;
	@FXML
	private Label fineLabel;
	@FXML
	private Hyperlink payNow;
	@FXML private Pane payNowPane;
	@FXML
	private Label warningLabel;
	@FXML
	private Button okButton;
	private Card falseProduct;

	public void onClickedTicket(ActionEvent actionEvent) throws IOException {
		Stage primaryStage = getStageFromEvent(actionEvent);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Ticket_Panel.fxml"));
		Parent root = null;
		root = loader.load();

		Scene scene = new Scene(root);

		// setUserData so that the fxml file of the loader can be retrieved
		scene.setUserData(loader);

		primaryStage.setScene(scene);
		primaryStage.setTitle("ThessBus: Ticket Purchase");
		primaryStage.show();
	}

	public void onClickedCard(ActionEvent actionEvent) throws IOException {
		Stage primaryStage = getStageFromEvent(actionEvent);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
		Parent root = null;
		root = loader.load();

		Scene scene = new Scene(root);

		// setUserData so that the fxml file of the loader can be retrieved
		scene.setUserData(loader);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ThessBus: Card Purchase");
		primaryStage.show();
	}

	//appear the list of the fines for the current user 
	public void onClickPayNow(ActionEvent e) throws IOException {
		ArrayList<String> choices = new ArrayList<>();
		for (Fine f : Main.loginUser.getFines()) {
			if(f.isPaid() == false)
				choices.add("Date Time: " + f.getDate_time() + ", Bus: " + f.getBus() + ", Price: "
							+ Double.toString(f.getPrice()));
		}
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>("Επιλογή Προστίμου", choices);
		dialog.setTitle("Pay Fine");
		dialog.setHeaderText(null);
		dialog.setContentText("Διάλεξε το πρόστιμο" + System.lineSeparator() +"που θέλεις να πληρώσεις: ");
		Optional<String> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			for (Fine f : Main.loginUser.getFines()) {
				if (result.get().contains(f.getDate_time())) {
					if (f.getPrice() <= Main.loginUser.getBalance()) {
						f.finePaid();
						Main.loginUser.reduceBalance(f.getPrice());
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Alert");
						alert.setHeaderText(null);
						alert.setContentText("Το πρόστιμο σου πληρώθηκε");
						alert.showAndWait();
						
						//initialize();
					} 
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Alert");
						alert.setHeaderText(null);
						alert.setContentText("Το υπόλοιπό σου δεν επαρκεί!");
						alert.showAndWait();
						break;
					}

				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Set in start screen in the red frame if he has fines 
		usernameMenu.setText(Main.loginUser.getUsername());
		balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
		welcome.setText("Γεια σου, " + Main.loginUser.getUsername() + "!");
		
		fineLabel.setText("   Έχεις (" + Main.loginUser.countUnpaidFines() + ") πρόστιμο/α απλήρωτο/α" 
				/*") πρόστιμο/α που κοστίζουν: " + Double.toString(Main.loginUser.calculateTotalFines()) + "€"*/);
		
		if(Main.loginUser.countUnpaidFines() == 0) {
			payNow.setMouseTransparent(true);
			payNow.setEffect(new GaussianBlur());
			fineLabel.setText("   Δεν έχεις πρόστιμα προς πληρωμή");
		}
		
		//Set in start screen in the red frame if he has multi way ticket  
		if(Main.loginUser.countMultiWayNotValidatedTickets() > 0) {
			HBox MultiWayTicketsHBox = new HBox();
			MultiWayTicketsHBox.setSpacing(30);
			MultiWayTicketsHBox.setPadding(new Insets(0, 0, 0, 20));

			Label multiWayTicketsLabel = new Label("Έχεις (" + Main.loginUser.countMultiWayNotValidatedTickets() + ") εισιτήριο/α πολλαπλών"
									+ System.lineSeparator() + "διαδρομών σε εκκρεμότητα");
			multiWayTicketsLabel.setFont(new Font(13));
			Hyperlink viewTicketslink = new Hyperlink("Προβολή");
			//viewTicketslink.setTextFill(new );
			viewTicketslink.setUnderline(true);
			viewTicketslink.setOnAction((ActionEvent e) -> {
			    onClickViewTickets(e);
			});
			MultiWayTicketsHBox.getChildren().add(multiWayTicketsLabel);
			MultiWayTicketsHBox.getChildren().add(viewTicketslink);

			payNowPane.getChildren().add(MultiWayTicketsHBox);
		}
		//Set in start screen in the red frame if he has not valid card 
		ArrayList<Product> products = Main.loginUser.getProducts();
		for (Product product : products) {
			if(product instanceof Card && ((Card) product).isValid() == false && ((Card) product).isFlag() == false) {
				warningLabel.setVisible(true);
				okButton.setVisible(true);
				falseProduct = ((Card) product);
			}
				
		}
		
	}
	
	//If he want to use the multi way ticket again
	public void onClickViewTickets(ActionEvent e) {
		ArrayList<String> choices = new ArrayList<>();
		for (Product p: Main.loginUser.getProducts()) {
			if(p instanceof Ticket && ((Ticket)p).getRemaining_routes() > 0)
				choices.add("Ημερομηνία επικύρωσης: " + p.getDate_time() + ", Απομένουσες χρήσεις: " + 
							((Ticket)p).getRemaining_routes() + ", Τιμή: " + Double.toString(p.getPrice()) + "€");
		}
		
		
		Dialog<HashMap<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Validate Ticket");
		dialog.setHeaderText(null);

		ButtonType mergeButtonType = new ButtonType("Επικύρωση", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(mergeButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<String> ticketBox = new ComboBox<>();
		ticketBox.setPromptText("Εισιτήριο");
		ObservableList<String> choicesObs = FXCollections.observableArrayList(choices);
		ticketBox.setItems(choicesObs);
		
		ComboBox<String> busBox = new ComboBox<>();
		busBox.setPromptText("Λεωφορείο");
		busBox.setItems(
				FXCollections.observableArrayList("01: Τ.Σ. Ευκαρπίας - Σκλαβενίτης - Νοσ. Παπαγεωργίου - Κ.Τ.Ε.Λ.",
						"01Α: Τ.Σ. Ευκαρπίας - Σκλαβενίτης - Νοσ. Παπαγεωργίου - Κ.Τ.Ε.Λ. μέσω Αμπελοκήπων",
						"02: Α.Σ. ΙΚΕΑ - Ν.Σ. Σταθμός μέσω Εγνατίας", 
						"02Α: Α.Σ. ΙΚΕΑ - Ν.Σ. Σταθμός μέσω αγροκτήματος Α.Π.Θ.",
						"03: Α.Σ. ΙΚΕΑ - Ν.Σ. Σταθμός",
						"04Α: Χαριλάου - Κ.Τ.Ε.Λ. Χαλκιδικής - Καλαμαριά",
						"04Β: Κ.Τ.Ε.Λ. Χαλκιδικής - Χαριλάου - Καλαμαριά",
						"04Ε: Καλαμαριά - Κ.Τ.Ε.Λ. Χαλκιδικής - Γερμανική Σχολή" ,
						"05: Ν.Κρήνη - Βενιζέλου",
						"06: Καλαμαριά - Βενιζέλου",
						"07: Αγ.Ιωάννης - Πανεπιστήμιο",
						"08: ΙΚΕΑ - Κ.Τ.Ε.Λ. μέσω Εγνατίας",
						"09: Ν.Σ. Σταθμός - Λαχαναγορά",
						"09Α: Ν.Σ. Σταθμός - μέσα απο τη Λαχαναγορά",
						"09Β: Ν.Σ. Σταθμός - Λαχαναγορά Κ.Τ.Ε.Ο",
						"10: Χαριλάου - Ν.Σ. Σταθμός",
						"11: Πυλαία - Ν.Σ. Σταθμός",
						"11Β: Πυλαία - Ν.Σ. Σταθμός ξενώνας Θεαγένειου",
						"12: Κ.Τ.Ε.Λ. - Κάτω Τούμπα",
						"14: Άνω Τούμπα - Ν.Σ. Σταθμός",
						"14Α: Άνω Τούμπα - Ν.Σ. Σταθμός μέσω Γένεσις",
						"15: Σαράντα Εκκλησίες - Ιστορικό Κέντρο",
						"15Α: Σαράντα Εκκλησίες - Γυμνάσιο",
						"16: Ευαγγελίστρια - Ιστορικό Κέντρο",
						"17: Τριανδρία - Ν.Σ. Σταθμός",
						"18: Αγ.Ανάργυροι - Κορδελιό",
						"19: Ελευθέριο Κορδελιό - Ν.Σ. Σταθμός",
						"19Α: Ν.Σ. Σταθμός - Ελευθέριο Κορδελιό",
						"20: Μενεμένη - Αριστοτέλους",
						"21: Εύοσμος - Αριστοτέλους",
						"21Α: Εύοσμος - Αριστοτέλους μέσω Κοιμητηρίων",
						"22: Άνω Πόλη",
						"23: Ν.Σ. Σταθμός - Συκιές",
						"24: Πλ. Ελευθερίας - Χίλια Δένδρα",
						"25: Νεάπολη - Βενιζέλου",
						"25A: Νεάπολη - Τέρμα Βενιζέλου",
						"26: Καλλιθέα - Πλ.Ελευθερίας",
						"27: Σταυρούπολη - Πανεπιστήμιο",
						"27Α: Σταυρούπολη - Πανεπιστήμιο - Ο.Α.Ε.Δ.",
						"27Β: Σταυρούπολη - Πανεπιστήμιο - Γυμνάσιο - Λύκειο",
						"28Α: Νοσ. Παπαγεωργίου - 424 Γ.Σ.Ν.Ε. - Πανεπιστήμια",
						"28Β: 424 Γ.Σ.Ν.Ε. - Νοσ. Παπαγεωργίου - Τ.Σ. Ευκαρπίας - Σκλαβενίτης",
						"29: Πολίχνη - Αριστοτέλους",
						"29Α: Πολίχνη - Αριστοτέλους μέσω Εργατικών",
						"30: Τριανδρία - Αποθήκη", 
						"31: Βούλγαρη - ΚΤΕΛ", 
						"32: Κ.Ηλιούπολη - Αριστοτέλους",
						"32Α: Κ.Ηλιούπολη - Αριστοτέλους μέσω Γυμναστηρίου",
						"33: Αγ.Παντελεήμων - Βενιζέλου",
						"33Α: Αγ.Παντελεήμων - Βενιζέλου - Εμπορικό Κέντρο Μακεδονία",
						"34: Α.Ηλιούπολη - Αριστοτέλους",
						"35: Μετέωρα - Βενιζέλου",
						"36: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος - Α.Σ. Ι.Κ.Ε.Α",
						"36Α: Βούλγαρη - Ζώνη Καινοτομίας - Κόσμος - Α.Σ. Ι.Κ.Ε.Α",
						"36Ε: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος - Ζώνη Καινοτομίας",
						"36Ζ: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος - Α.Σ. Ι.Κ.Ε.Α - Ζώνη Καινοτομίας",
						"36Η: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Ζώνη Καινοτομίας - Κόσμος ",
						"36Κ: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος - Κ.Τ.Ε.Λ ",
						"36Ν: Βούλγαρη - Κόσμος",
						"36Ρ: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος - Ζώνη Καινοτομίας",
						"36Τ: Βούλγαρη - Κ.Τ.Ε.Λ. Χαλκιδικής",
						"36Υ: Βούλγαρη - Κόσμος - Ζώνη Καινοτομίας",
						"37: Ν.Σ. Στάθμος - Κρυονέρι",
						"38: Ν.Σ. Σταθμός - Νέα Ευκαρπία",
						"39: Κηφησιά - Δικαστήρια",
						"39Α: Κηφησιά - Δικαστήρια - Εμπορικό Κέντρο Μακεδονία",
						"40: Ν.Σ. Σταθμός - Καλοχώρι",
						"40Α: Ν.Σ. Σταθμός - Καλοχώρι μέσω Σ.Δ.Ο.Ε.",
						"40Κ: Ν.Σ. Σταθμός - Καλοχώρι μέσω Ρομποπούλου",
						"42: Κορδελιό - Κοιμητήρια Ευόσμου",
						"42Α: Κοιμητήρια Ευόσμου από Μενεμένη",
						"42Β: Κοιμητήρια Ευόσμου - Αμπελόκηποι",
						"43: Τοπικό Ευόσμου",
						"45: Κ.Τ.Ε.Λ. Μακεδονία - Κ.Τ.Ε.Λ. Χαλκιδικής - Κόσμος",
						"45Α: Κ.Τ.Ε.Λ. Μακεδονία - Κ.Τ.Ε.Λ. Χαλκιδικής",
						"45Β: Κ.Τ.Ε.Λ. Μακεδονία - Κόσμος - Κ.Τ.Ε.Λ. Χαλκιδικής",
						"50: Πολιτιστική Γραμμή",
						"51: Ν.Σ. Σταθμός - Σίνδος",
						"51Α: Σίνδος - Ανατολικό",
						"52: Ν.Σ. Σταθμός - Α.Τ.Ε.Ι. - Σίνδος",
						"53: Σίνδος Τοπικό",
						"54: Ν.Σ. Σταθμός - Ιωνία",
						"54Α: Ν.Σ. Σταθμός - Ιωνία μέσω Ι.Ν. Αγ.Αθανασίου",
						"55: Σταυρούπολη - Ωραιόκραστο",
						"55Α: Σταυρούπολη - Γυμνάσιο",
						"55Β: Σταυρούπολη - Παλαιά Συμαχική",
						"55Ε: Σταυρούπολη - Φιλοθέη",
						"55Κ: Σταυρούπολη - Ιωνία μέσω ΕΛ.ΤΑ",
						"55Μ: Σταυρούπολη - Ιωνία - Κοιμητήρια",
						"55Ν: Σταυρούπολη - Κοιμητήρια - Ιωνία",
						"55Ρ: Σταυρούπολη - Νεοχωρούδα",
						"56: Ν.Σ. Σταθμός - Ωραιόκαστρο",
						"56Α: Ν.Σ. Σταθμός - Ωραιόκαστρο - Γυμνάσιο",
						"57: Αριστοτέλους - Ασβεστοχώρι - Δ/ση Χορτιάτη",
						"58: Βενιζέλου - Πανόραμα - Δ/ση Χορτιάτη",
						"59: Τοπικό Πεύκων",
						"59Α: Γυμνάσιο Πεύκων - Κυκλικό",
						"59Β: Γυμνάσιο Πεύκων",
						"59Κ: Τοπικό Πεύκων - Κοιμητήριο",
						"60Α: Οικισμός Μακεδονία Πανοράματος(Μακεδ/μάχων)",
						"60Β: Οικισμός Μακεδονία Πανοράματος(Ι.Δραγούμη)",
						"60Ε: Πανόραμα - Θέρμη - Α.Σ. ΙΚΕΑ",
						"61: Χορτιάτης - Διασταύρωση",
						"61Α: Χορτιάτης - Διασταύρωση - Λύκειο",
						"64: Ν.Σ. Σταθμός - Φίλυρο",
						"64Α: Χορτιάτης  - Φίλυρο Παιδικό Χωριό",
						"64Β: Χορτιάτης  - Φίλυρο Χριστιανική Ελπίδα",
						"64Ε: Ν.Σ. Σταθμός - Φίλυρο Λύκειο",
						"64Κ: Ν.Σ. Σταθμός - Φίλυρο Παιδικό Χωριό Λύκειο",
						"66: Χαριλάου - Θέρμη",
						"67: Α.Σ. ΙΚΕΑ - Τριάδι",
						"67Α: Τριάδι - Κεραμουργεία - ΙΚΕΑ",
						"67Β: Α.Σ. ΙΚΕΑ - Τριάδι - Διεθνές Πανεπιστήμιο",
						"68Α: Α.Σ. ΙΚΕΑ - Διεθνές Πανεπιστήμιο - Κεραμουργεία",
						"68Β: Α.Σ. ΙΚΕΑ - ΟΑΕΔ - Λάκκια",
						"69Η: Α.Σ. ΙΚΕΑ - Επανομή",
						"69Κ: Α.Σ. ΙΚΕΑ - Επανομή - Παραλία - Ι.Ε.Κ.",
						"69Ν: ΙΚΕΑ - Επανομή - Ντουράκι",
						"69Ρ: ΙΚΕΑ - Επανομή - Ι.Ε.Κ. - Ντουράκι",
						"69Τ: ΙΚΕΑ - Επανομή - Ι.Ε.Κ. - Παραλία",
						"72: Α.Σ. ΙΚΕΑ - Ν.Μηχανιώνα",
						"72Α: Α.Σ. ΙΚΕΑ - Ν.Μηχανιώνα μέσω Αγγελοχωρίου",
						"72Β: Α.Σ. ΙΚΕΑ - Αγγελοχώρι - Ν.Μηχανιώνα Κέντρο Υγείας",
						"76: Α.Σ. ΙΚΕΑ - Αγγελοχώρι",
						"76Α: Αγγελοχώρι - Ν.Μηχανιώνα",
						"76Β: Α.Σ. ΙΚΕΑ - Αγγελοχώρι - Τ.Ε.Λ. - Ι.Ε.Κ.",
						"77: Ν.Μηχανιώνα - Επανομή",
						"77Α: Ν.Μηχανιώνα - Επανομή - Γυμνάσιο - Λύκειο - Ι.Ε.Κ. - Τ.Ε.Ε.",
						"77Β: Ν.Μηχανιώνα - Επανομή μέσω Ι.Ε.Κ. - Τ.Ε.Ε.",
						"77Ε: Ν.Μηχανιώνα - Επανομή - Γυμνάσιο - Λύκειο",
						"77Κ: Ν.Μηχανιώνα - Επανομή μέσω Ι.Ε.Κ.",
						"79: Α.Σ. ΙΚΕΑ - Αεροδρόμιο",
						"79Α: Α.Σ. ΙΚΕΑ - Αεροδρόμιο - Κ.Τ.Ε.Λ. Χαλκιδικής",
						"79Β: Κ.Τ.Ε.Λ. Χαλκιδικής - Αεροδρόμιο - Α.Σ. ΙΚΕΑ",
						"80: Κ.Τ.Ε.Λ. - Μάλγαρα",
						"80Α: Κ.Τ.Ε.Λ. - Μάλγαρα μέσω Ανατολικού",
						"80Β: Μάλγαρα - Κ.Τ.Ε.Λ. μέσω Ανατολικού",
						"80Ε: Κ.Τ.Ε.Λ. - Μάλγαρα μέσω Ιωνίας",
						"80Ζ: Κ.Τ.Ε.Λ. - Μάλγαρα Ιωνία - Ανατολικό",
						"81: Κ.Τ.Ε.Λ. - Κουφάλια",
						"81Α: Κ.Τ.Ε.Λ. - Αγ.Αθανάσιος - Κουφάλια",
						"81Β: Κ.Τ.Ε.Λ. - Αγ.Αθανάσιος - Κ.Τ.Ε.Λ.",
						"81Ε: Ελεούσα - Κουφάλια",
						"81Κ: Βαλτοχώρι - Κ.Τ.Ε.Λ.",
						"82Α: Νεοχωρούδα - Πεντάλοφος",
						"82Α: Πεντάλοφος - Νεοχωρούδα",
						"82Ε: Κ.Τ.Ε.Λ. - Μονόλοφος",
						"82Κ: Μονόλοφος - Κ.Τ.Ε.Λ.",
						"82Μ: Κ.Τ.Ε.Λ. - Μονόλοφος",
						"82Ν: Μονόλοφος - Κ.Τ.Ε.Λ.",
						"83: Λαγκαδάς - Θεσ/νίκη (Εξπρές)",
						"83Α: Λαγκαδάς - Θεσ/νίκη (Ανάληψη - Λαγκαδάς - Θεσ/νίκη)",
						"83Β: Λαγκαδάς - Θεσ/νίκη (Ανάληψη - Λαγυνά - Θεσ/νίκη)",
						"83Μ: Λαγκαδάς - Θεσ/νίκη μέσω Λουτρών Λαγκαδά",
						"83Ν: Σταυρούπολη - Λαγκαδάς (Οικισμός Παπακυριαζή μέσω Άνω Λαγυνών)",
						"83Τ: Λαγκαδάς - Θεσ/νίκη μέσω Ι.Ε.Κ.",
						"83Χ: Λαγκαδάς - Θεσ/νίκη (Χρυσαυγή - Λαγκαδάς - Θεσ/νίκη",
						"84Α: Λητή - Μελισσοχώρι - Δρυμός - Λητή",
						"84Β: Λητή - Δρυμός - Μελισσοχώρι - Λητή",
						"85: Θεσ/νίκη - Ευαγγελίστρια",
						"85Α: Θεσ/νίκη - Κριθιά",
						"85Β: Θεσ/νίκη - Κριθιά - Λευκοχώρι",
						"85Ζ: Θεσ/νίκη - Κριθιά - Ευαγγελίστρια",
						"85Η: Λαγκαδάς - Ευαγγελίστρια",
						"85Κ: Θεσ/νίκη - Κυδώνια - Ευγγελίστρια",
						"85Μ: Θεσ/νίκη - Εξαμίλι - Κριθιά",
						"85Ν: Θεσ/νίκη - Νικόπολη - Ευαγγελίστρια",
						"85Τ: Θεσ/νίκη - Κριθιά - Εξαμίλι - Λευκοχώρι",
						"85Χ: Θεσ/νίκη - Μαυροράχη - Ευαγγελίστρια",
						"86: Θεσ/νίκη - Λαγκαδίκια",
						"86Α: Γερακαρού - Λαγκαδίκια",
						"86Β: Βασιλούδι - Λαγκαδίκια",
						"86Ε: Λαγυνά - Βασιλούδι - Λαγκαδίκια",
						"86Ζ: Λαγυνά - Αρδαμέρι - Λαγκαδίκια",
						"86Η: Καβαλάρι - Βασιλούδι - Γερακαρού - Λαγκαδίκια",
						"86Κ: Λαγυνά - Γερακαρού - Λαφκαδίκια",
						"86Λ: Καβαλάρι - Βασιλούδι - Λαγκαδίκια",
						"86Μ: Αρδαμέρι - Γερακαρού - Λαγκαδίκια",
						"86Ν: Λαγυνά - Αρδαμέρι - Γερακαρού - Λαγκαδίκια",
						"86Ρ: Αρδαμέρι - Λαγκαδίκια",
						"86Τ: Λαγκαδίκια - Λαγυνά - Θεσσαλονίκη",
						"86Υ: Καβαλάρι - Βασιλούδι - Αρδαμέρι - Λαγκαδίκια",
						"86Φ: Καβαλάρι - Γερακαρού - Λαγκαδίκια",
						"86Χ: Λαγκαδίκια - Βασιλούδι - Τοπικό Σχολικό",
						"87Α: Ρύσιο - Βασιλικά",
						"87Β: ΙΚΕΑ - Βασιλικά - Ραιδεστός - Περιστερά - Λιβάδι",
						"87Γ: Βασιλικά - Αγ.Αντώνης",
						"87Ε: Ραιδεστός - Βασιλικά",
						"87Ζ: Βασιλικά - Περιστερά - Λιβάδι",
						"87Η: ΙΚΕΑ - Βασιλικά - Ραιδεστός - Φιλοθέη - Λακιά",
						"87Θ: ΙΚΕΑ - Βασιλικά - Αγ.Αντώνιος - Μονοπήγαδο - Ταγαράδες - Ρύσιο",
						"87Λ: Α.Σ. Ι.Κ.Ε.Α. - Βασιλικά - Ραιδεστός - Λακιά",
						"87Μ: ΙΚΕΑ - Βασιλικά - Ρύσιο - Αγ.Αντώνης - Μονοπήγαδο",
						"87Ν: ΙΚΕΑ - Βασιλικά - Ρύσιο - Αγ.Αντώνης",
						"87Π: ΙΚΕΑ - Βασιλικά - Ραιδεστός - Περιστερά",
						"87Ρ: ΙΚΕΑ - Βασιλικά - Ρύσιο - Ταγαράδες",
						"87Τ: ΙΚΕΑ - Βασιλικά - Ρύσιο - Ταγαράδες - Αγ.Αντώνης",
						"87Φ: ΙΚΕΑ - Βασιλικά - Ραιδεστός - Φιλοθέη",
						"87Χ: Βασιλικά - Αγ.Αντώνης - Μονοπήγαδο",
						"88: ΙΚΕΑ - Μεσημέρι μέσω ΧΥΤΑ",
						"88Α: ΙΚΕΑ - Καρδία - Τρίλοφος",
						"88Β: ΙΚΕΑ - Τρίλοφος - Καρδία",
						"88Ε: ΙΚΕΑ - Μεσημέρι - Επανομή μέσω ΧΥΤΑ",
						"88Η: ΙΚΕΑ - Μεσημέρι - Επανομή από δυτική παράπλευρη",
						"88Κ: ΙΚΕΑ - Μεσημέρι από δυτική παράπλευρη",
						"88Μ: ΙΚΕΑ - Μεσημέρι από Λάκκωμα - ΧΥΤΑ",
						"88Ν: ΙΚΕΑ - Μεσημέρι από Λάκκωμα - δυτική παράπλευρη",
						"89Α: Κ.Τ.Ε.Λ. - Ξηροχώρι",
						"89Β: Κ.Τ.Ε.Λ - Ακροπόταμος",
						"89Ε: Ακροπόταμος - Κ.Τ.Ε.Λ",
						"89Κ: Δ/νση Αγιονερίου - Ακροπόταμος",
						"89Ν: Ακροπόταμος - Δ/ση Αγιονερίου",
						"90: Κουφάλια - Μάλγαρα",
						"90Α: Λουδίας - Μάλγαρα",
						"90Β: Ακροπόταμος - Μάλγαρα",
						"90Ε: Ακροπόταμος - Κουφάλια",
						"90Κ: Μάλγαρα - Χαλκηδόνα",
						"91Α: Λαγκαδάς - Ανάληψη",
						"91Β: Λαγκαδάς - Ανάληψη(Σχολεία)",
						"91Ε: Λαγκαδάς - Άσσηρος - Κριθιά",
						"91Η: Λαγκαδάς - Λαγυνά - Λαγκαδάς(Ηράκλειο - Περιβολάκι)",
						"91Κ: Λαγκαδάς - Καβαλάρι - Λαγκαδάς ",
						"91Ν: Λαγκαδάς - Οικισμός Παπακυριαζή",
						"91Τ: Λαγκαδάς - Λαγκαδίκια",
						"91Υ: Χρυσαυγή - Ανάληψη",
						"91Χ: Ανάληψη - Χρυσαυγή",
						"92: Κουφάλια - Άθυρα - Ραχλωνα",
						"92Α: Κουφάλια - Άθυρα",
						"92Ε: Κουφάλια - Εργατικές Κατοικίες",
						"92Ρ: Κουφάλια - Ραχώνα" ,
						"Χ1:ΚΤΕΛ - Αεροδρόμιο",
						"N1A: ΚΤΕΛ - Αεροδρόμιο Νυχτερινό μέσω Α.Σ. Ι.Κ.Ε.Α.",
						"Ν1: ΚΤΕΛ - Αεροδρόμιο Νυχτερινό"));
		
		grid.add(new Label("Διάλεξε το εισιτήριο που" + System.lineSeparator() + "θέλεις να επικυρώσεις:"), 0, 0);
		grid.add(ticketBox, 1, 0);
		grid.add(new Label("Διάλεξε λεωφορείο:"), 0, 1);
		grid.add(busBox, 1, 1);

		dialog.getDialogPane().setContent(grid);
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == mergeButtonType) {
		        HashMap<String, String> result = new HashMap<>();
		        result.put("ticket", ticketBox.getValue());
		        result.put("bus", busBox.getValue());
		        return result;
		    }
		    return null;
		});

		Optional<HashMap<String, String>> result = dialog.showAndWait();
		//when the ticket and card is not valid 
		result.ifPresent(r -> {
		    if(r.get("ticket") != null && r.get("bus") != null) {
		    	for (Product p : Main.loginUser.getProducts()) {
					if (r.get("ticket").contains(p.getDate_time())) {
						
						if(((Ticket)p).isValid() == false) {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Alert");
							alert.setHeaderText(null);
							alert.setContentText("Σε ενημερώνουμε πως η επικύρωση του εισιτηρίου" + System.lineSeparator() + 
												 "είναι εκτός των χρονικών ορίων που ορίζονται γι αυτο!" + System.lineSeparator() +
												 "ΣΥΝΕΧΕΙΑ;");
							Optional<ButtonType> result1 = alert.showAndWait();
							if(result1.get() == ButtonType.CANCEL)
								break;
						}
						
						((Ticket)p).Refresh_num_of_routes();
						((Ticket)p).setValidation_date_time();
						String bus = r.get("bus").substring(0, 2);
						bus = bus + (Character.isLetter((r.get("bus").charAt(2))) ? r.get("bus").charAt(2) : "");
						((Ticket)p).setBus(bus);
						FileManager.updateProduct(p, "Products.dat");
							
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Alert");
						alert.setHeaderText(null);
						alert.setContentText("Το εισιτήριο σου επικυρώθηκε");
						alert.showAndWait();
							
						//initialize();

					}
				}
		    }
		    else {
		    	Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Alert");
				alert.setHeaderText(null);
				alert.setContentText("Δεν έχεις πραγματοποιήσει κάποια από τις δυο επιλογές");
				alert.showAndWait();
		    }
			
		});
	}
	//Disappear the warning label for not valid card
	public void OnClickedOk(ActionEvent actionEvent)
	{
		warningLabel.setVisible(false);
		okButton.setVisible(false);
        falseProduct.setFlagTrue();
	}
		
}
