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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends MainController implements Initializable {
	@FXML
	public TextField nameField;
	@FXML
	public TextField surnameField;
	@FXML
	public TextField emailField;
	@FXML
	public PasswordField passwordField1;
	@FXML
	public PasswordField passwordField2;
	@FXML
	public TextField passportField;
	@FXML
	public TextField phoneField;
	@FXML
	public TextField idField;
	@FXML
	public TextField cardField;

	public void onClickedRegister(ActionEvent e) throws IOException {

		// Check if user has filled all required fields
		if (nameField.getText().equals("") || surnameField.getText().equals("") || passwordField1.getText().equals("")
				|| passwordField2.getText().equals("")) {
			showAlert("Complete the necessary fields");
		} else {
			// Apply some form restrictions
			if (passportField.getText().length() == 12 || passportField.getText().isEmpty())
				if (passwordField1.getText().equals(passwordField2.getText())) {
					if (phoneField.getText().length() == 10 && phoneField.getText().matches("\\d+")) {
						if (emailField.getText().contains("@")) {
							// If all user input is correct create and save the new user credentials
							Main.loginUser = new Passenger(nameField.getText() + " " + surnameField.getText(),
									passwordField1.getText(), emailField.getText(), cardField.getText(),
									idField.getText(), phoneField.getText(), passportField.getText(), 0);
							FileManager.InsertUser(Main.loginUser, "Users.dat");

							// appear the start screen when he register
							Stage stage = Main.getStagefromEvent(e);
							stage.close();
							Stage primaryStage = new Stage();
							FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
							Parent root = null;
							root = loader.load();
							Scene scene = new Scene(root);

							// setUserData so that the fxml file of the loader can be retrieved
							scene.setUserData(loader);

							primaryStage.setScene(scene);
							primaryStage.setTitle("ThessBus: StartScreen");

							// What happens when user closes window
							primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
								@Override
								public void handle(WindowEvent arg0) {
									Alert alert = new Alert(AlertType.CONFIRMATION);
									alert.setTitle("Confirmation Alert");
									alert.setHeaderText(null);
									alert.setContentText("Θες σίγουρα να βγεις;");

									Optional<ButtonType> result = alert.showAndWait();

									if (result.get() == ButtonType.OK) {
										if (Main.loginUser != null) {
											if (Main.loginUser.getFines() != null)
												FileManager.updateFines(Main.loginUser.getUsername(),
														Main.loginUser.getFines(), "Fines.dat");

										}
									} else
										arg0.consume();
								}
							});
							primaryStage.show();
						} else {
							showAlert("The email address is not valid");
						}
					} else {
						showAlert("The phone number must have 10 digits, and only digits");
					}
				} else {
					showAlert("Passwords do not match");
				}
			else {
				showAlert("The passport number must have 12 digits");
			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Alert");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
