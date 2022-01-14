import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.google.zxing.WriterException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TicketController extends MainController implements Initializable {

	//FXML buttons 
	@FXML private Button oneWayNormal;
	@FXML private Button oneWayReduced;
	@FXML private Button twoWayNormal;
	@FXML private Button twoWayReduced;
	@FXML private Button ThreeWayNormal;
	@FXML private Button ThreeWayReduced;
	@FXML private Button FourWayNormal;
	@FXML private Button FourWayReduced;
	@FXML private Button AirportNormal;
	@FXML private Button AirportReduced;
	@FXML private Pane buttonsPane;
	@FXML private VBox navBarVBox;
	@FXML private Hyperlink signOutHyperlink;
	@FXML private ComboBox<String> busesComboBox;
	@FXML private Label usernameMenu;
	@FXML private Label balanceMenu;

	private String bus = "";
	private double cost;
	
    
	public void TicketData(Ticket newTicket) throws WriterException, IOException{	
		    //If you want to purchase for sure
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Είσαι σίγουρος ότι θέλεις να συνεχίσεις;");
			
			//If he clicks ok
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				if (Main.loginUser.getBalance() < cost) {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setTitle("Alert");
					alert1.setHeaderText(null);
					alert1.setContentText("Δεν έχεις αρκετά χρήματα");
					alert1.showAndWait();
					}
				else {
					Alert newalert = new Alert(AlertType.CONFIRMATION);
					newalert.setTitle("Alert");
					newalert.setHeaderText(null);
					newalert.setContentText("Γουχου! Το εισιτήριο σου αγοράστηκε!");
					newalert.showAndWait();
					
					Main.loginUser.reduceBalance(cost);
					Main.loginUser.addProduct(newTicket);
					
					Passenger temp = new Passenger(Main.loginUser.getUsername(),
							Main.loginUser.getPassword(), Main.loginUser.getEmail(),
							Main.loginUser.getCardNum(), Main.loginUser.getId(),
							Main.loginUser.getPhoneNum(), Main.loginUser.getPassport(),
							Main.loginUser.getBalance(), Main.loginUser.getUserNum());

					FileManager.updatePassenger(Main.loginUser, "Users.dat", temp);
					FileManager.insertProducts(Main.loginUser.getUsername(), newTicket,
							"Products.dat");
				}
			} 
			else {
			    // ... user chose CANCEL or closed the dialog
			}
	}
		
    //Clicked one way ticket
	public void onClickedOneWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 0.5 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "Μονής", 1, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked two way ticket
	public void onClickedTwoWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 0.6 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "Διπλής", 2, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked three way ticket
	public void onClickedThreeWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 0.8 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "Τριπλής", 3, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked four way ticket
	public void onClickedFourWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 1.0 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "Τετραπλής", 4, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	

	public void onClickedAirport(ActionEvent e)  throws WriterException, IOException {

		if (bus != "") {
			cost = 1.0 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "Αεροδρόμιο", 1, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();

	}
	
	//Choose bus and it keeps the number of the bus 
	public void comboBoxChoice(ActionEvent actionEvent) {
		bus = busesComboBox.getValue().substring(0, 2);
		bus = bus + (Character.isLetter((busesComboBox.getValue().charAt(2))) ? busesComboBox.getValue().charAt(2) : "");
	}

	//if he not choose any of the buses
	public void noBusSelectedAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Alert");
		alert.setHeaderText(null);
		alert.setContentText("Πρώτα διάλεξε λεωφορείο!");
		alert.showAndWait();
	}

	
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		busesComboBox.setPromptText("Λεωφορείο");
		busesComboBox.setItems(
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
						"14: ’νω Τούμπα - Ν.Σ. Σταθμός",
						"14Α: ’νω Τούμπα - Ν.Σ. Σταθμός μέσω Γένεσις",
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
						"22: ’νω Πόλη",
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
						"83Ν: Σταυρούπολη - Λαγκαδάς (Οικισμός Παπακυριαζή μέσω ’νω Λαγυνών)",
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
						"91Ε: Λαγκαδάς - ’σσηρος - Κριθιά",
						"91Η: Λαγκαδάς - Λαγυνά - Λαγκαδάς(Ηράκλειο - Περιβολάκι)",
						"91Κ: Λαγκαδάς - Καβαλάρι - Λαγκαδάς ",
						"91Ν: Λαγκαδάς - Οικισμός Παπακυριαζή",
						"91Τ: Λαγκαδάς - Λαγκαδίκια",
						"91Υ: Χρυσαυγή - Ανάληψη",
						"91Χ: Ανάληψη - Χρυσαυγή",
						"92: Κουφάλια - ’θυρα - Ραχλωνα",
						"92Α: Κουφάλια - ’θυρα",
						"92Ε: Κουφάλια - Εργατικές Κατοικίες",
						"92Ρ: Κουφάλια - Ραχώνα" ,
						"Χ1:ΚΤΕΛ - Αεροδρόμιο",
						"N1A: ΚΤΕΛ - Αεροδρόμιο Νυχτερινό μέσω Α.Σ. Ι.Κ.Ε.Α.",
						"Ν1: ΚΤΕΛ - Αεροδρόμιο Νυχτερινό"));
        
		//If the passenger has university,school id then he has a discount
		if (Main.loginUser != null) {
			if (Main.loginUser.getCheck() == 1) {
				oneWayNormal.setMouseTransparent(true);
				twoWayNormal.setMouseTransparent(true);
				ThreeWayNormal.setMouseTransparent(true);
				FourWayNormal.setMouseTransparent(true);
				AirportNormal.setMouseTransparent(true);
			} else {
				oneWayReduced.setMouseTransparent(true);
				twoWayReduced.setMouseTransparent(true);
				ThreeWayReduced.setMouseTransparent(true);
				FourWayReduced.setMouseTransparent(true);
				AirportReduced.setMouseTransparent(true);
			}
			
			usernameMenu.setText(Main.loginUser.getUsername());
			balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
		}

	}

	//Getters and Setters
	
	public VBox getNavBarVBox() {
		return navBarVBox;
	}

	public Hyperlink getSignOutHyperlink() {
		return signOutHyperlink;
	}

	public Button getOneWayNormal() {
		return oneWayNormal;
	}

	public Button getOneWayReduced() {
		return oneWayReduced;
	}

	public Button getTwoWayNormal() {
		return twoWayNormal;
	}

	public Button getTwoWayReduced() {
		return twoWayReduced;
	}
	
	

	public Button getThreeWayNormal() {
		return ThreeWayNormal;
	}

	public Button getThreeWayReduced() {
		return ThreeWayReduced;
	}

	public Button getFourWayNormal() {
		return FourWayNormal;
	}

	public Button getFourWayReduced() {
		return FourWayReduced;
	}

	public Button getAirportNormal() {
		return AirportNormal;
	}

	public Button getAirportReduced() {
		return AirportReduced;
	}

	public Pane getButtonsPane() {
		return buttonsPane;
	}

	public ComboBox<String> getBusesComboBox() {
		return busesComboBox;
	}

}
