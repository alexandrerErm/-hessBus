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
			alert.setContentText("����� �������� ��� ������ �� ����������;");
			
			//If he clicks ok
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				if (Main.loginUser.getBalance() < cost) {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setTitle("Alert");
					alert1.setHeaderText(null);
					alert1.setContentText("��� ����� ������ �������");
					alert1.showAndWait();
					}
				else {
					Alert newalert = new Alert(AlertType.CONFIRMATION);
					newalert.setTitle("Alert");
					newalert.setHeaderText(null);
					newalert.setContentText("������! �� ��������� ��� ����������!");
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
			Ticket newTicket = new Ticket(cost, Main.loginUser, "�����", 1, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked two way ticket
	public void onClickedTwoWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 0.6 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "������", 2, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked three way ticket
	public void onClickedThreeWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 0.8 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "�������", 3, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	
	//Clicked four way ticket
	public void onClickedFourWay(ActionEvent e) throws WriterException, IOException {
		if (bus != "") {
			cost = 1.0 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "���������", 4, bus);
			TicketData(newTicket);
		}
		else
			noBusSelectedAlert();
	}
	

	public void onClickedAirport(ActionEvent e)  throws WriterException, IOException {

		if (bus != "") {
			cost = 1.0 * Main.loginUser.getCheck();
			Ticket newTicket = new Ticket(cost, Main.loginUser, "����������", 1, bus);
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
		alert.setContentText("����� ������� ���������!");
		alert.showAndWait();
	}

	
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		busesComboBox.setPromptText("���������");
		busesComboBox.setItems(
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
