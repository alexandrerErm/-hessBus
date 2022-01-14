import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FineController extends MainController implements Initializable {
	
	//FXML components for Inspector.FXML 
	@FXML Pane leftPane;
	@FXML Hyperlink backHyperlink;
	@FXML TextField nameField;
	@FXML TextField lastNameField;
	@FXML TextField idField;
	@FXML TextField adressField;
	@FXML TextField phoneField;
	@FXML TextField busField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	//Method for the inspector to charge user with a fine
	public void onClickedFine(ActionEvent actionEvent) throws IOException
	{
		if(nameField.getText().isEmpty() || lastNameField.getText().isEmpty() || busField.getText().isEmpty()) {
			/*Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Alert");
			alert.setHeaderText(null);
			alert.setContentText("Συμπλήρωσε όλα τα απαραίτητα πεδία μάγκα μου!");
			alert.showAndWait();*/
			showAlert("Συμπλήρωσε όλα τα απαραίτητα πεδία μάγκα μου!");
		}
		else {
			String name = nameField.getText() + " " + lastNameField.getText();
			Passenger passenger = (Passenger) FileManager.search(name, "Users.dat");
			if(passenger != null) {
				Fine fine = new Fine(passenger, Main.loginIns.getUserNum(), busField.getText());
				FileManager.insertFine(fine, "Fines.dat");
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Alert");
				alert.setHeaderText(null);
				alert.setContentText("Το πρόστιμο κόπηκε με επιτυχία");
				Optional<ButtonType> result = alert.showAndWait();
	            if (result.get() == ButtonType.OK) {
	            	Stage stage = Main.getStagefromEvent(actionEvent);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Inspector.fxml"));
					Parent root = null;
					root = loader.load();
					Scene scene = new Scene(root);
					
					//setUserData so that the fxml file of the loader can be retrieved
			        scene.setUserData(loader);
			        
					stage.setScene(scene);
					stage.setTitle("ThessBus: Inspector");
					stage.show();
	            }
			}
			else
				showAlert("Ο χρήστης που ψάχνεις δεν είναι εγγεγραμμένος στο σύστημα!");
		
		}
	}
	
	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setTitle("Alert");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	//Back button
	public Pane getLeftPane() {
		return leftPane;
	}
	
	//Sign out button
	public Hyperlink getBackHyperlink() {
		return backHyperlink;
	}
	
}