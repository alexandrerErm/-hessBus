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
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>("������� ���������", choices);
		dialog.setTitle("Pay Fine");
		dialog.setHeaderText(null);
		dialog.setContentText("������� �� ��������" + System.lineSeparator() +"��� ������ �� ���������: ");
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
						alert.setContentText("�� �������� ��� ���������");
						alert.showAndWait();
						
						//initialize();
					} 
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Alert");
						alert.setHeaderText(null);
						alert.setContentText("�� �������� ��� ��� �������!");
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
		welcome.setText("���� ���, " + Main.loginUser.getUsername() + "!");
		
		fineLabel.setText("   ����� (" + Main.loginUser.countUnpaidFines() + ") ��������/� ��������/�" 
				/*") ��������/� ��� ���������: " + Double.toString(Main.loginUser.calculateTotalFines()) + "�"*/);
		
		if(Main.loginUser.countUnpaidFines() == 0) {
			payNow.setMouseTransparent(true);
			payNow.setEffect(new GaussianBlur());
			fineLabel.setText("   ��� ����� �������� ���� �������");
		}
		
		//Set in start screen in the red frame if he has multi way ticket  
		if(Main.loginUser.countMultiWayNotValidatedTickets() > 0) {
			HBox MultiWayTicketsHBox = new HBox();
			MultiWayTicketsHBox.setSpacing(30);
			MultiWayTicketsHBox.setPadding(new Insets(0, 0, 0, 20));

			Label multiWayTicketsLabel = new Label("����� (" + Main.loginUser.countMultiWayNotValidatedTickets() + ") ���������/� ���������"
									+ System.lineSeparator() + "��������� �� �����������");
			multiWayTicketsLabel.setFont(new Font(13));
			Hyperlink viewTicketslink = new Hyperlink("�������");
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
				choices.add("���������� ����������: " + p.getDate_time() + ", ����������� �������: " + 
							((Ticket)p).getRemaining_routes() + ", ����: " + Double.toString(p.getPrice()) + "�");
		}
		
		
		Dialog<HashMap<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Validate Ticket");
		dialog.setHeaderText(null);

		ButtonType mergeButtonType = new ButtonType("���������", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(mergeButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<String> ticketBox = new ComboBox<>();
		ticketBox.setPromptText("���������");
		ObservableList<String> choicesObs = FXCollections.observableArrayList(choices);
		ticketBox.setItems(choicesObs);
		
		ComboBox<String> busBox = new ComboBox<>();
		busBox.setPromptText("���������");
		busBox.setItems(
				FXCollections.observableArrayList("01: �.�. ��������� - ����������� - ���. ������������ - �.�.�.�.",
						"01�: �.�. ��������� - ����������� - ���. ������������ - �.�.�.�. ���� �����������",
						"02: �.�. ���� - �.�. ������� ���� ��������", 
						"02�: �.�. ���� - �.�. ������� ���� ������������ �.�.�.",
						"03: �.�. ���� - �.�. �������",
						"04�: �������� - �.�.�.�. ���������� - ���������",
						"04�: �.�.�.�. ���������� - �������� - ���������",
						"04�: ��������� - �.�.�.�. ���������� - ��������� �����" ,
						"05: �.����� - ���������",
						"06: ��������� - ���������",
						"07: ��.������� - ������������",
						"08: ���� - �.�.�.�. ���� ��������",
						"09: �.�. ������� - ����������",
						"09�: �.�. ������� - ���� ��� �� ����������",
						"09�: �.�. ������� - ���������� �.�.�.�",
						"10: �������� - �.�. �������",
						"11: ������ - �.�. �������",
						"11�: ������ - �.�. ������� ������� ����������",
						"12: �.�.�.�. - ���� ������",
						"14: ��� ������ - �.�. �������",
						"14�: ��� ������ - �.�. ������� ���� �������",
						"15: ������� ��������� - �������� ������",
						"15�: ������� ��������� - ��������",
						"16: ������������� - �������� ������",
						"17: ��������� - �.�. �������",
						"18: ��.��������� - ��������",
						"19: ��������� �������� - �.�. �������",
						"19�: �.�. ������� - ��������� ��������",
						"20: �������� - ������������",
						"21: ������� - ������������",
						"21�: ������� - ������������ ���� �����������",
						"22: ��� ����",
						"23: �.�. ������� - ������",
						"24: ��. ���������� - ����� ������",
						"25: ������� - ���������",
						"25A: ������� - ����� ���������",
						"26: �������� - ��.����������",
						"27: ����������� - ������������",
						"27�: ����������� - ������������ - �.�.�.�.",
						"27�: ����������� - ������������ - �������� - ������",
						"28�: ���. ������������ - 424 �.�.�.�. - ������������",
						"28�: 424 �.�.�.�. - ���. ������������ - �.�. ��������� - �����������",
						"29: ������� - ������������",
						"29�: ������� - ������������ ���� ���������",
						"30: ��������� - �������", 
						"31: �������� - ����", 
						"32: �.��������� - ������������",
						"32�: �.��������� - ������������ ���� ������������",
						"33: ��.����������� - ���������",
						"33�: ��.����������� - ��������� - �������� ������ ���������",
						"34: �.��������� - ������������",
						"35: ������� - ���������",
						"36: �������� - �.�.�.�. ���������� - ������ - �.�. �.�.�.�",
						"36�: �������� - ���� ����������� - ������ - �.�. �.�.�.�",
						"36�: �������� - �.�.�.�. ���������� - ������ - ���� �����������",
						"36�: �������� - �.�.�.�. ���������� - ������ - �.�. �.�.�.� - ���� �����������",
						"36�: �������� - �.�.�.�. ���������� - ���� ����������� - ������ ",
						"36�: �������� - �.�.�.�. ���������� - ������ - �.�.�.� ",
						"36�: �������� - ������",
						"36�: �������� - �.�.�.�. ���������� - ������ - ���� �����������",
						"36�: �������� - �.�.�.�. ����������",
						"36�: �������� - ������ - ���� �����������",
						"37: �.�. ������� - ��������",
						"38: �.�. ������� - ��� ��������",
						"39: ������� - ����������",
						"39�: ������� - ���������� - �������� ������ ���������",
						"40: �.�. ������� - ��������",
						"40�: �.�. ������� - �������� ���� �.�.�.�.",
						"40�: �.�. ������� - �������� ���� �����������",
						"42: �������� - ���������� �������",
						"42�: ���������� ������� ��� ��������",
						"42�: ���������� ������� - �����������",
						"43: ������ �������",
						"45: �.�.�.�. ��������� - �.�.�.�. ���������� - ������",
						"45�: �.�.�.�. ��������� - �.�.�.�. ����������",
						"45�: �.�.�.�. ��������� - ������ - �.�.�.�. ����������",
						"50: ����������� ������",
						"51: �.�. ������� - ������",
						"51�: ������ - ���������",
						"52: �.�. ������� - �.�.�.�. - ������",
						"53: ������ ������",
						"54: �.�. ������� - �����",
						"54�: �.�. ������� - ����� ���� �.�. ��.���������",
						"55: ����������� - �����������",
						"55�: ����������� - ��������",
						"55�: ����������� - ������ ��������",
						"55�: ����������� - �������",
						"55�: ����������� - ����� ���� ��.��",
						"55�: ����������� - ����� - ����������",
						"55�: ����������� - ���������� - �����",
						"55�: ����������� - ����������",
						"56: �.�. ������� - �����������",
						"56�: �.�. ������� - ����������� - ��������",
						"57: ������������ - ����������� - �/�� ��������",
						"58: ��������� - �������� - �/�� ��������",
						"59: ������ ������",
						"59�: �������� ������ - �������",
						"59�: �������� ������",
						"59�: ������ ������ - ����������",
						"60�: �������� ��������� �����������(�����/�����)",
						"60�: �������� ��������� �����������(�.��������)",
						"60�: �������� - ����� - �.�. ����",
						"61: ��������� - �����������",
						"61�: ��������� - ����������� - ������",
						"64: �.�. ������� - ������",
						"64�: ���������  - ������ ������� �����",
						"64�: ���������  - ������ ����������� ������",
						"64�: �.�. ������� - ������ ������",
						"64�: �.�. ������� - ������ ������� ����� ������",
						"66: �������� - �����",
						"67: �.�. ���� - ������",
						"67�: ������ - ������������ - ����",
						"67�: �.�. ���� - ������ - ������� ������������",
						"68�: �.�. ���� - ������� ������������ - ������������",
						"68�: �.�. ���� - ���� - ������",
						"69�: �.�. ���� - �������",
						"69�: �.�. ���� - ������� - ������� - �.�.�.",
						"69�: ���� - ������� - ��������",
						"69�: ���� - ������� - �.�.�. - ��������",
						"69�: ���� - ������� - �.�.�. - �������",
						"72: �.�. ���� - �.���������",
						"72�: �.�. ���� - �.��������� ���� ������������",
						"72�: �.�. ���� - ���������� - �.��������� ������ ������",
						"76: �.�. ���� - ����������",
						"76�: ���������� - �.���������",
						"76�: �.�. ���� - ���������� - �.�.�. - �.�.�.",
						"77: �.��������� - �������",
						"77�: �.��������� - ������� - �������� - ������ - �.�.�. - �.�.�.",
						"77�: �.��������� - ������� ���� �.�.�. - �.�.�.",
						"77�: �.��������� - ������� - �������� - ������",
						"77�: �.��������� - ������� ���� �.�.�.",
						"79: �.�. ���� - ����������",
						"79�: �.�. ���� - ���������� - �.�.�.�. ����������",
						"79�: �.�.�.�. ���������� - ���������� - �.�. ����",
						"80: �.�.�.�. - �������",
						"80�: �.�.�.�. - ������� ���� ����������",
						"80�: ������� - �.�.�.�. ���� ����������",
						"80�: �.�.�.�. - ������� ���� ������",
						"80�: �.�.�.�. - ������� ����� - ���������",
						"81: �.�.�.�. - ��������",
						"81�: �.�.�.�. - ��.��������� - ��������",
						"81�: �.�.�.�. - ��.��������� - �.�.�.�.",
						"81�: ������� - ��������",
						"81�: ��������� - �.�.�.�.",
						"82�: ���������� - ����������",
						"82�: ���������� - ����������",
						"82�: �.�.�.�. - ���������",
						"82�: ��������� - �.�.�.�.",
						"82�: �.�.�.�. - ���������",
						"82�: ��������� - �.�.�.�.",
						"83: �������� - ���/���� (������)",
						"83�: �������� - ���/���� (������� - �������� - ���/����)",
						"83�: �������� - ���/���� (������� - ������ - ���/����)",
						"83�: �������� - ���/���� ���� ������� �������",
						"83�: ����������� - �������� (�������� ����������� ���� ��� �������)",
						"83�: �������� - ���/���� ���� �.�.�.",
						"83�: �������� - ���/���� (�������� - �������� - ���/����",
						"84�: ���� - ����������� - ������ - ����",
						"84�: ���� - ������ - ����������� - ����",
						"85: ���/���� - �������������",
						"85�: ���/���� - ������",
						"85�: ���/���� - ������ - ���������",
						"85�: ���/���� - ������ - �������������",
						"85�: �������� - �������������",
						"85�: ���/���� - ������� - ������������",
						"85�: ���/���� - ������� - ������",
						"85�: ���/���� - �������� - �������������",
						"85�: ���/���� - ������ - ������� - ���������",
						"85�: ���/���� - ��������� - �������������",
						"86: ���/���� - ����������",
						"86�: ��������� - ����������",
						"86�: ��������� - ����������",
						"86�: ������ - ��������� - ����������",
						"86�: ������ - �������� - ����������",
						"86�: �������� - ��������� - ��������� - ����������",
						"86�: ������ - ��������� - ����������",
						"86�: �������� - ��������� - ����������",
						"86�: �������� - ��������� - ����������",
						"86�: ������ - �������� - ��������� - ����������",
						"86�: �������� - ����������",
						"86�: ���������� - ������ - �����������",
						"86�: �������� - ��������� - �������� - ����������",
						"86�: �������� - ��������� - ����������",
						"86�: ���������� - ��������� - ������ �������",
						"87�: ����� - ��������",
						"87�: ���� - �������� - ��������� - ��������� - ������",
						"87�: �������� - ��.�������",
						"87�: ��������� - ��������",
						"87�: �������� - ��������� - ������",
						"87�: ���� - �������� - ��������� - ������� - �����",
						"87�: ���� - �������� - ��.�������� - ���������� - ��������� - �����",
						"87�: �.�. �.�.�.�. - �������� - ��������� - �����",
						"87�: ���� - �������� - ����� - ��.������� - ����������",
						"87�: ���� - �������� - ����� - ��.�������",
						"87�: ���� - �������� - ��������� - ���������",
						"87�: ���� - �������� - ����� - ���������",
						"87�: ���� - �������� - ����� - ��������� - ��.�������",
						"87�: ���� - �������� - ��������� - �������",
						"87�: �������� - ��.������� - ����������",
						"88: ���� - �������� ���� ����",
						"88�: ���� - ������ - ��������",
						"88�: ���� - �������� - ������",
						"88�: ���� - �������� - ������� ���� ����",
						"88�: ���� - �������� - ������� ��� ������ ����������",
						"88�: ���� - �������� ��� ������ ����������",
						"88�: ���� - �������� ��� ������� - ����",
						"88�: ���� - �������� ��� ������� - ������ ����������",
						"89�: �.�.�.�. - ��������",
						"89�: �.�.�.� - �����������",
						"89�: ����������� - �.�.�.�",
						"89�: �/��� ���������� - �����������",
						"89�: ����������� - �/�� ����������",
						"90: �������� - �������",
						"90�: ������� - �������",
						"90�: ����������� - �������",
						"90�: ����������� - ��������",
						"90�: ������� - ���������",
						"91�: �������� - �������",
						"91�: �������� - �������(�������)",
						"91�: �������� - ������� - ������",
						"91�: �������� - ������ - ��������(�������� - ����������)",
						"91�: �������� - �������� - �������� ",
						"91�: �������� - �������� �����������",
						"91�: �������� - ����������",
						"91�: �������� - �������",
						"91�: ������� - ��������",
						"92: �������� - ����� - �������",
						"92�: �������� - �����",
						"92�: �������� - ��������� ���������",
						"92�: �������� - ������" ,
						"�1:���� - ����������",
						"N1A: ���� - ���������� ��������� ���� �.�. �.�.�.�.",
						"�1: ���� - ���������� ���������"));
		
		grid.add(new Label("������� �� ��������� ���" + System.lineSeparator() + "������ �� �����������:"), 0, 0);
		grid.add(ticketBox, 1, 0);
		grid.add(new Label("������� ���������:"), 0, 1);
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
							alert.setContentText("�� ������������ ��� � ��������� ��� ����������" + System.lineSeparator() + 
												 "����� ����� ��� �������� ����� ��� ��������� �� ����!" + System.lineSeparator() +
												 "��������;");
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
						alert.setContentText("�� ��������� ��� �����������");
						alert.showAndWait();
							
						//initialize();

					}
				}
		    }
		    else {
		    	Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Alert");
				alert.setHeaderText(null);
				alert.setContentText("��� ����� ��������������� ������ ��� ��� ��� ��������");
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
