import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class InformationController extends MainController implements Initializable{
	//Just a class for the text on the information pane
	@FXML private Label usernameMenu;
	@FXML private Label balanceMenu;
	

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		usernameMenu.setText(Main.loginUser.getUsername());
		balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
		
	}

}