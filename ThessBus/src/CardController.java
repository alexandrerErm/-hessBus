import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CardController extends MainController implements Initializable {
	private Passenger owner = Main.loginUser;
	private double cost;
	//Fxml components in  Card.fxml
	@FXML private Button monthlyReduced;
	@FXML private Button threeMonthsReduced;
	@FXML private Button sixMonthsReduced;
	@FXML private Button annualReduced;
	@FXML private Pane buttonsPane;
	@FXML private VBox navBarVBox;
	@FXML private Hyperlink signOutHyperlink;
	@FXML private Button monthlyNormal;
	@FXML private Button threeMonthsNormal;
	@FXML private Button sixMonthsNormal;
	@FXML private Button annualNormal;
	@FXML private Label usernameMenu;
	@FXML private Label balanceMenu;
	//This method is used to purchase a card.
	public void CardData(Card newcard) throws WriterException, IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Είσαι σίγουρος ότι θέλεις να συνεχίσεις;");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//Checking if user's balance is enough to purchase the card.
			if (owner.getBalance() < cost) {
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
				newalert.setContentText("Γιουχου! Η κάρτα σου αγοράστηκε!");
				newalert.showAndWait();
				
				//Update user's balance and insert the new card in his products
				owner.reduceBalance(cost);
				owner.addProduct(newcard);
				//Updating user's data in file and inserting the new card in product's file
				Passenger temp = new Passenger(Main.loginUser.getUsername(),
						Main.loginUser.getPassword(), Main.loginUser.getEmail(),
						Main.loginUser.getCardNum(), Main.loginUser.getId(),
						Main.loginUser.getPhoneNum(), Main.loginUser.getPassport(),
						Main.loginUser.getBalance(),Main.loginUser.getUserNum());

				FileManager.updatePassenger(Main.loginUser, "Users.dat", temp);
				FileManager.insertProducts(Main.loginUser.getUsername(), newcard,
							"Products.dat");
			}
		}
		else {
		    // ... user chose CANCEL or closed the dialog
		}
	}
	//Generating different types of cards.
	public void generateMonthlyCard(ActionEvent event) throws IOException, WriterException {
		cost = 15 * owner.getCheck();
		Card newcard = new Card(cost, owner, "Μηνιαία", 1);
		CardData(newcard);
	}

	public void generateThreeMonthsCard(ActionEvent event) throws IOException, WriterException {
		cost = 42 * owner.getCheck();
		Card newcard = new Card(cost, owner, "Τριμηνιαία", 3);
		CardData(newcard);
	}

	public void generateSixMonthsCard(ActionEvent event) throws IOException, WriterException {
		cost = 75 * owner.getCheck();
		Card newcard = new Card(cost, owner, "Εξαμινιαία", 6);
		CardData(newcard);
	}

	public void generateAnnualCard(ActionEvent event) throws IOException, WriterException {
		cost = 135 * owner.getCheck();
		Card newcard = new Card(cost, owner, "Ετήσια", 12);
		CardData(newcard);
	}
	//Generating getters and setters.
	public Button getMonthlyNormal() {
		return monthlyNormal;
	}

	public Button getThreeMonthsNormal() {
		return threeMonthsNormal;
	}

	public Button getSixMonthsNormal() {
		return sixMonthsNormal;
	}

	public Button getAnnualNormal() {
		return annualNormal;
	}
	
	public Button getMonthlyReduced() {
		return monthlyReduced;
	}

	public Button getThreeMonthsReduced() {
		return threeMonthsReduced;
	}

	public Button getSixMonthsReduced() {
		return sixMonthsReduced;
	}

	public Button getAnnualReduced() {
		return annualReduced;
	}

	public Pane getButtonsPane() {
		return buttonsPane;
	}

	public VBox getNavBarVBox() {
		return navBarVBox;
	}

	public Hyperlink getSignOutHyperlink() {
		return signOutHyperlink;
	}
	
	//Initializing CardController.
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(owner != null) {
			//Check if user is reduced or normal
			if(owner.getCheck() == 1) {
				monthlyNormal.setMouseTransparent(true);
				threeMonthsNormal.setMouseTransparent(true);
				sixMonthsNormal.setMouseTransparent(true);
				annualNormal.setMouseTransparent(true);
			}
			else {
				monthlyReduced.setMouseTransparent(true);
				threeMonthsReduced.setMouseTransparent(true);
				sixMonthsReduced.setMouseTransparent(true);
				annualReduced.setMouseTransparent(true);
			}
			
			usernameMenu.setText(Main.loginUser.getUsername());
			balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
		}

	}

}
