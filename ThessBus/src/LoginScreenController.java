import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginScreenController extends MainController implements Initializable {
	//Fxml components in LoginScreen.fxml
	@FXML
	public Button loginButton;
	@FXML
	public TextField usernameField;
	@FXML
	public PasswordField passwordField;
	@FXML
	private Hyperlink visitorHyperlink;
	//This method is called when user(Passenger or Inspector) tries to Log in with his ThessBus account.
	public void onClickedLogin(ActionEvent e) throws IOException {

		Stage primarystage = Main.getStagefromEvent(e);
		 //This statement checks if user name and password field are filled.
		if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Alert");
			alert.setHeaderText(null);
			alert.setContentText("Δεν έχεις συμπληρώσει κάποιο/α από τα πεδία μάγκα!");
			Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
		} 
		else {
			//Search if user(passenger or inspector) exists in project's files.
			if (usernameField.getText().contains("inspector")) {
				Main.loginIns = (TicketInspector) FileManager.searchUser(usernameField.getText(),
						passwordField.getText(), "Inspectors.dat");
			}
			else
				Main.loginUser = (Passenger) FileManager.searchUser(usernameField.getText(), passwordField.getText(),
						"Users.dat");
			//If the user is passenger start screen is prompted and passenger's products and fines are loaded in Main.loginUser static variable.
			if (Main.loginUser != null) {
				primarystage.close();
				Main.loginUser.setProducts(FileManager.getProducts(Main.loginUser.getUserNum(), "Products.dat"));
				Main.loginUser.setFines(FileManager.getFines(Main.loginUser.getUserNum(), "Fines.dat"));
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
				Parent root = null;
					root = loader.load();
				Scene scene = new Scene(root);
				
				//setUserData so that the fxml file of the loader can be retrieved.
	    		scene.setUserData(loader);	
				stage.setScene(scene);
				stage.setTitle("ThessBus: Home");
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					//If passenger presses X then user and fine files are updated and the program closes after confirmation.
					@Override
					public void handle(WindowEvent arg0) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirmation Alert");
						alert.setHeaderText(null);
						alert.setContentText("Θες σίγουρα να βγεις;");

						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.OK) {
							Passenger temp = new Passenger(Main.loginUser.getUsername(),
										Main.loginUser.getPassword(), Main.loginUser.getEmail(),
										Main.loginUser.getCardNum(), Main.loginUser.getId(),
										Main.loginUser.getPhoneNum(), Main.loginUser.getPassport(),
										Main.loginUser.getBalance(), Main.loginUser.getUserNum());
							FileManager.updatePassenger(Main.loginUser, "Users.dat", temp);
							if(Main.loginUser.getFines()!=null)
								FileManager.updateFines(Main.loginUser.getUserNum(), Main.loginUser.getFines(), "Fines.dat");
							
							Main.loginUser = null;
						} else
							arg0.consume();
						
					}
				});
				stage.show();
				
			}
			//If user is inspector inspector's screen is prompted
			else if (Main.loginIns != null) {
				primarystage.close();
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Inspector.fxml"));
				Parent root = null;
				root = loader.load();
				Scene scene = new Scene(root);
				
				//setUserData so that the fxml file of the loader can be retrieved
	    		scene.setUserData(loader);	
				stage.setScene(scene);
				stage.setTitle("ThessBus: Inspector");
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					//If inspector presses X the program closes after confirmation.
					@Override
					public void handle(WindowEvent arg0) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirmation Alert");
						alert.setHeaderText(null);
						alert.setContentText("Θες σίγουρα να βγεις;");

						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.CANCEL) {
							arg0.consume();
						}
						
						Main.loginIns = null;
					}
				});
				stage.show();
			}
			else {
				//If user doesn't exist an error occurs
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Alert");
				alert.setHeaderText(null);
				alert.setContentText("Error. Username or password doesn't match.");
				Optional<ButtonType> result = alert.showAndWait();
	            if (result.get() == ButtonType.OK){
	                alert.close();
	            }
			}
		}
	}
	//This method is used to load register screen
	public void onClickedSignUp(ActionEvent actionEvent) throws Exception {
		Stage primaryStage = MainController.getStageFromEvent(actionEvent);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterScreen.fxml"));
		Parent root = null;
		root = loader.load();

		Scene scene = new Scene(root);

		// setUserData so that the fxml file of the loader can be retrieved
		scene.setUserData(loader);

		primaryStage.setScene(scene);
		primaryStage.setTitle("ThessBus: Sign Up");
		primaryStage.show();
	}
	//If a user doesn't have a account he can browse the app's products as a visitor
	public void onHyperlinkVisitor(ActionEvent actionEvent) throws Exception {
		Stage primaryStage = MainController.getStageFromEvent(actionEvent);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VisitorScreen.fxml"));
		Parent root = null;
		root = loader.load();

		Scene scene = new Scene(root);

		// setUserData so that the fxml file of the loader can be retrieved
		scene.setUserData(loader);

		primaryStage.setScene(scene);
		primaryStage.setTitle("ThessBus: Visitor");
		primaryStage.show();
	}
	

	//Initializing LoginScreenController.
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
