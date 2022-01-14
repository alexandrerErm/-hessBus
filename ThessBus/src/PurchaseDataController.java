import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PurchaseDataController extends MainController implements Initializable {
	@FXML private TextField lastNameField;
	@FXML private Button checkDataButton;
	@FXML private TextField nameField;
	@FXML private TextField dateTimeField;
	@FXML private TextField lastValidationDateTimeField;
	@FXML private TextField typeProductField;
	@FXML private TextField productNumField;
	@FXML private TextField busField;
	@FXML private TextField routesField;
	@FXML private TextField priceField;
	
	public void setDataToFields(Product productChecked, int typeOfProduct) {
		
		//Break username into firstname(first token) and lastname(second token)
		String name="", surname ="";
        StringTokenizer st = new StringTokenizer(productChecked.getOwner().getUsername());
        name = st.nextToken();
        surname = st.nextToken();
        lastNameField.setText(surname);
        nameField.setText(name);
        
        //Set date field
        String dateTime = productChecked.getDate_time();
        dateTimeField.setText(dateTime.substring(0, 13) + ":" + dateTime.substring(13, 15) + ":" + dateTime.substring(15, 17));
        
        //Set productnum and price field
        productNumField.setText(productChecked.getProduct_num());
        priceField.setText(Double.toString(productChecked.getPrice()));
        
        //Set fields according to radio button (if type of product is "καρτα" or "εισητηριο")
        if(typeOfProduct == 0) {
        	busField.setText("-");
        	lastValidationDateTimeField.setText("-");
        	typeProductField.setText("Κάρτα: " + ((Card) productChecked).getDuration() + " μηνών/α");
        	routesField.setText("Απεριορίστων διαδρομών");
        }
        else {
        	busField.setText(((Ticket) productChecked).getBus());
        	
        	lastValidationDateTimeField.setText("-");
        	if(((Ticket) productChecked).getNo_of_routes() != 1 && ((Ticket) productChecked).getValidation_date_time() != null) {
        		String validationDateTime = ((Ticket) productChecked).getValidation_date_time();
        		lastValidationDateTimeField.setText(validationDateTime.substring(0, 13) + ":" + validationDateTime.substring(13, 15) +
        										":" + validationDateTime.substring(15, 17));
        	}
        	
        	typeProductField.setText("Εισιτήριο");
        	routesField.setText(Integer.toString(((Ticket) productChecked).getNo_of_routes()));
        }
        		
	}
	
	public void onClickedCheckData(ActionEvent actionEvent) throws NumberFormatException, ParseException {
		int duration = 0;
		//Check how many times a ticket can be validated and duration
		if(routesField.getText().equals("Απεριορίστων διαδρομών")) {
			String monthsubstring = typeProductField.getText().substring(7, 8);
			monthsubstring = monthsubstring + (typeProductField.getText().substring(8, 9).equals(" ")?
												"":typeProductField.getText().substring(8, 9));
			duration = Integer.parseInt(monthsubstring);
		}
		else {
			switch(Integer.parseInt(routesField.getText())) {
			case 2:
				duration = 70;
				break;
			case 3:
				duration = 90;
				break;
			case 4:
				duration = 120;
				break;
			}
		}
		
		//Check if ticket is valid and issue fine
		boolean valid = Main.loginIns.ticketValidation(dateTimeField.getText(), duration, busField.getText(), 
									lastValidationDateTimeField.getText(), Double.parseDouble(priceField.getText()));
		String contentText = (valid)?"Από τον έλεγχο του προϊόντος δεν προέκυψε ανάγκη για έκδοση προστίμου":
									 "Ο επιβάτης ενδέχεται να βρίσκεται παράνομα στο λεωφορείο";
		
		Stage primaryStage = getStageFromEvent(actionEvent);
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Έλεγχος");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("ΟΚ");
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Πρόστιμο");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Inspector.fxml"));
			Parent root = null;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			Scene scene = new Scene(root);
			
			//setUserData so that the fxml file of the loader can be retrieved
    		scene.setUserData(loader);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(" ");
			primaryStage.show();
        }
        else if(result.get() == ButtonType.CANCEL) {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Fine.fxml"));
    		Parent root = null;
    		try {
				root = loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		FineController ctrl = (FineController) loader.getController();
    		ctrl.getLeftPane().getChildren().remove(ctrl.getBackHyperlink());
    		
    		Scene scene = new Scene(root, 600, 400);
    		
    		//setUserData so that the fxml file of the loader can be retrieved
    		scene.setUserData(loader);
    		
    		primaryStage.setScene(scene);
    		primaryStage.setTitle(" ");
    		primaryStage.show();
        }
        	
	}

	public static Stage getStageFromEvent(ActionEvent actionEvent) {
		Node source = (Node) actionEvent.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		return stage;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}