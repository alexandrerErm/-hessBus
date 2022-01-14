import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import javafx.scene.image.Image;

public abstract class Product implements Serializable {
	
	//Attributes
	protected String date_time;
	protected Passenger owner;
	protected double price;
	protected String type;
	protected String product_num;
	protected byte[] QR_code;
	protected Image qrImage;
	
	//Constructor 
	public Product(double price, Passenger owner, String type) {
		this.date_time = getCurrentTime();
		this.owner = owner;
		this.price = price;
		this.type = type;
		setProduct_num();	
		try {
			setQR();
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Prints the QR image
	public void printQR() {
		qrImage = QRcode.printQRCode(QR_code);
	}
	
	public abstract boolean isValid() throws FileNotFoundException, NotFoundException, IOException;

	
	
	//Setters and Getters for the class Product
	public double getPrice() {
		return price;
	}

	public void setProduct_num() {
		this.product_num= date_time + owner.getUserNum();
	}

	public String getDate_time() {
		return date_time;
	}
	
	public Passenger getOwner() {
		return owner;
	}
	
	
	//Returns date and time 
	protected String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HHmmss");
		Calendar cal = Calendar.getInstance();
		return  dateFormat.format(cal.getTime());
	}
	
	public void setQR () throws WriterException, IOException {
		this.QR_code = QRcode.generateQRCode(product_num, 350, 350);
	}
	
	

	public void setPrice(double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public String getProduct_num() {
		return product_num;
	}

	public byte[] getQR_code() {
		return QR_code;
	}

	public Image getQrImage() {
		return qrImage;
	}
	
}
