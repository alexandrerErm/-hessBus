import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class DepositController extends MainController implements Initializable{
	//Fxml components in Deposit.fxml
	@FXML private Label balanceLabel;
	@FXML private Label usernameMenu;
	@FXML private Label balanceMenu;
	//This method is used to deposit money in passenger's account.
	public void OnClicked(ActionEvent e) {
		//A dialogue window prompts and asks passenger to insert money in his account.
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Deposit");
		dialog.setHeaderText("Εισαγωγή χρημάτων στον λογαριασμό ThessBus");
		dialog.setContentText("Παρακαλούμε εισάγετε ποσό:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    Main.loginUser.updateBalance(result.get().equals("")?0:Double.parseDouble(result.get()));
		    balanceLabel.setText(Double.toString(Main.loginUser.getBalance()));
		    
		    Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert");
			alert.setHeaderText(null);
			alert.setContentText("Το ποσό σου ανανεώθηκε!");
			alert.showAndWait();
			//Updating passenger's data in file and inserting the new card in product's file.
		    Passenger temp = new Passenger(Main.loginUser.getUsername(),
		    		Main.loginUser.getPassword(), Main.loginUser.getEmail(),
		    		Main.loginUser.getCardNum(), Main.loginUser.getId(),
		    		Main.loginUser.getPhoneNum(), Main.loginUser.getPassport(),
		    		Main.loginUser.getBalance(), Main.loginUser.getUserNum());

			FileManager.updatePassenger(Main.loginUser, "Users.dat", temp);
		}
	}
	
	//Initializing DepositController.
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		balanceLabel.setText(Double.toString((Main.loginUser.getBalance())));
		usernameMenu.setText(Main.loginUser.getUsername());
		balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
	}

}