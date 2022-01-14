import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HistoryController extends MainController implements Initializable {
	//Fxml components in History.fxml
	@FXML private Label historystatusLabel;
	@FXML private VBox historyVBox;
	@FXML private VBox navBarVBox;
	@FXML private Label usernameMenu;
	@FXML private Label balanceMenu;
	//Setting history screen which includes qrcode image,date-time,bus and price of the product.
	public void setFieldsandData(ArrayList<Product> products, ArrayList<Fine> fines) {
		if(products.isEmpty() && fines.isEmpty())
			historystatusLabel.setText("Δεν υπάρχουν στοιχεία για προβολή!");
		else {
			if(products.isEmpty() == false) {
				Label gap = new Label("Εισιτήρια");
				gap.setFont(new Font(16));
				gap.setPadding(new Insets(0, 0, 0, 20));
				historyVBox.getChildren().add(gap);
				
				HBox titlesHBox = new HBox();
				titlesHBox.setPrefSize(450, 20);
				titlesHBox.setSpacing(30);
				titlesHBox.setPadding(new Insets(0, 0, 0, 20));
				
				Label qrCode = new Label("QR Code");
				qrCode.setFont(new Font(15));
				Label date = new Label("Ημερομηνία");
				date.setFont(new Font(15));
				Label time = new Label("Ώρα");
				time.setFont(new Font(15));
				Label bus = new Label("Λεωφορείο");
				bus.setFont(new Font(15));
				Label price = new Label("Τιμή");
				price.setFont(new Font(15));
				
				titlesHBox.getChildren().add(qrCode);
				titlesHBox.getChildren().add(date);
				titlesHBox.getChildren().add(time);
				titlesHBox.getChildren().add(bus);
				titlesHBox.getChildren().add(price);

				historyVBox.getChildren().add(titlesHBox);
				
				for (Product product : products) {
					HBox hbox = new HBox();
					hbox.setSpacing(30);
					hbox.setPadding(new Insets(10, 0, 0, 20));
					
					ImageView imgview = new ImageView();
					imgview.setFitWidth(60);
					imgview.setFitHeight(40);
					imgview.setImage(QRcode.printQRCode(product.getQR_code()));
					String dateTime = product.getDate_time();
					Label timeLabel = new Label(dateTime.substring(11, 13) + ":" + dateTime.substring(13, 15) + ":" + dateTime.substring(15, 17));
					timeLabel.setPrefWidth(32);
					Label dateLabel = new Label(dateTime.substring(0, 10));
					dateLabel.setPrefWidth(81);
					Label busLabel = new Label();
					busLabel.setText((product.getPrice()>2)?"-":((Ticket)product).getBus());
					busLabel.setPrefWidth(78);
					Label priceLabel = new Label(Double.toString(product.getPrice()));
					priceLabel.setPrefWidth(25);
					
					hbox.getChildren().add(imgview);
					hbox.getChildren().add(dateLabel);
					hbox.getChildren().add(timeLabel);		
					hbox.getChildren().add(busLabel);
					hbox.getChildren().add(priceLabel);
					historyVBox.getChildren().add(hbox);
					//A windows is prompted which shows all the details about the product.
					hbox.setOnMousePressed(e -> {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Product's Qr Code");
						alert.setHeaderText(null);
						alert.setContentText("Ημερομηνία: " + dateTime.substring(0, 10) + " "
								+ dateTime.substring(11, 13) + ":" + dateTime.substring(13, 15) + ":"
								+ dateTime.substring(15, 17) + System.lineSeparator() + "Τύπος προϊόντος: " 
								+ ((product instanceof Ticket)?"Εισιτήριο ":"") + product.type
								+ ((product instanceof Ticket)?" διαδρομής":" κάρτα απεριορίστων διαδρομών")
								+ System.lineSeparator() + ((product instanceof Ticket)?("Λεωφορείο: " +((Ticket)product).getBus()):""));
						ImageView temp = new ImageView();
						temp.setImage(QRcode.printQRCode(product.getQR_code()));
						temp.setFitHeight(200);
						temp.setFitWidth(200);
						alert.setGraphic(temp);
						alert.showAndWait();
					});
				}
				
			}
			//Contains user's fines that are not paid.
			if(fines.isEmpty() == false) {
				Label dummy = new Label("");
				historyVBox.getChildren().add(dummy);
				
				Label gap = new Label("Πρόστιμα");
				gap.setFont(new Font(16));
				gap.setPadding(new Insets(0, 0, 0, 20));
				historyVBox.getChildren().add(gap);
				
				HBox titlesHBox = new HBox();
				titlesHBox.setPrefSize(450, 20);
				titlesHBox.setSpacing(30);
				titlesHBox.setPadding(new Insets(0, 0, 0, 20));
				
				Label date = new Label("Ημερομηνία");
				date.setFont(new Font(15));
				Label time = new Label("Ώρα");
				time.setFont(new Font(15));
				Label bus = new Label("Λεωφορείο");
				bus.setFont(new Font(15));
				Label price = new Label("Αντίτιμο");
				price.setFont(new Font(15));
				Label paid= new Label("Πληρώθηκε");
				paid.setFont(new Font(15));
				
				titlesHBox.getChildren().add(price);
				titlesHBox.getChildren().add(date);
				titlesHBox.getChildren().add(time);
				titlesHBox.getChildren().add(bus);
				titlesHBox.getChildren().add(paid);

				historyVBox.getChildren().add(titlesHBox);
				
				for (Fine fine : fines) {
					HBox hbox = new HBox();
					hbox.setSpacing(30);
					hbox.setPadding(new Insets(10, 0, 0, 20));
					
					Label priceLabel = new Label(Double.toString(fine.getPrice()));
					priceLabel.setPrefWidth(60);
					String dateTime = fine.getDate_time();
					Label timeLabel = new Label(dateTime.substring(11, 13) + ":" + dateTime.substring(13, 15) + ":" + dateTime.substring(15, 17));
					timeLabel.setPrefWidth(32);
					Label dateLabel = new Label(dateTime.substring(0, 10));
					dateLabel.setPrefWidth(81);
					Label busLabel = new Label(fine.getBus());
					busLabel.setPrefWidth(78);
					Label paidLabel = new Label();
					paidLabel.setText((fine.isPaid()==true)?"Ναι":"Όχι");
					paidLabel.setPrefWidth(25);
					
					hbox.getChildren().add(priceLabel);
					hbox.getChildren().add(dateLabel);
					hbox.getChildren().add(timeLabel);		
					hbox.getChildren().add(busLabel);
					hbox.getChildren().add(paidLabel);
					historyVBox.getChildren().add(hbox);					
				}
				
			}
			
		}

	}

	//Initializing HistoryController.
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		historystatusLabel.setText("");
		usernameMenu.setText(Main.loginUser.getUsername());
		balanceMenu.setText(Double.toString(Main.loginUser.getBalance()));
		
	}
	
	//Generating setters and getters.
	public Label getHistorystatusLabel() {
		return historystatusLabel;
	}

	public VBox getHistoryVBox() {
		return historyVBox;
	}

	public VBox getNavBarVBox() {
		return navBarVBox;
	}
	
}