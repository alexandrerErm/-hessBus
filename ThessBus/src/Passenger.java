import java.util.ArrayList;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.Serializable;

//Class that extends User superclass and models passengers
public class Passenger extends User implements Serializable{

	//Attributes
	private String email;
	private String cardNum;
	private String id;
	private String phoneNum;
	private String passport;
	private double balance;
	private int check;	//Checks if the passenger is reduced. Price * 2 for normal passenger. Price * 1 for reduced
	private ArrayList<Product> products = new ArrayList<>();	//ArrayList that keeps track of passenger's purchased
																//products for as long as the passenger is logged in
	private ArrayList<Fine> fines = new ArrayList<>();	//ArrayList that keeps track of the fines that are issued 
														//to the passenger for as long as the passenger is logged ins
	
	//Constructor
	public Passenger(String username, String password, String email, String cardNum,
					 String id, String phoneNum, String passport, double balance) {
		super(username, password);
		
		this.email = email;
		this.cardNum = cardNum;
		this.id = id;
		this.phoneNum = phoneNum;
		this.balance = balance;	
		this.passport = passport;
		
		this.check = (this.passport.isEmpty())?2:1;
	}
	
	//Constructor used when updating Passenger, so as to make a copy of him by value
	public Passenger(String username, String password, String email, String cardNum, 
					 String id, String phoneNum, String passport, double balance, String userNum) {
		super(username, password, userNum);
		
		this.email = email;
		this.cardNum = cardNum;
		this.id = id;
		this.phoneNum = phoneNum;
		this.balance = balance;	
		this.passport = passport;
		
		this.check = (this.passport.isEmpty())?2:1;
	}
	
	//Method called when passenger changes his account settings
	public void setNewData(String username, String password, String email, String cardNum, String id, String phoneNum, String passport) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.cardNum = cardNum;
		this.id = id;
		this.phoneNum = phoneNum;
		this.passport = passport;
		
		this.check = (this.passport.isEmpty())?2:1;
	}
	
	public int countNotValidCards() {
		int count = 0;
		for (Product product : products) {
			if(product instanceof Card && ((Card) product).isValid() == false)
				count++;
		}
		return count;
	}
	
	//Method called when StartScreen is loaded. Counts the number of multi-way tickets
	//validated less times than they could. Decides whether the passenger should be informed accordingly
	public int countMultiWayNotValidatedTickets() {
		int count = 0;
		for (Product product : products) {
			if(product instanceof Ticket && ((Ticket)product).getRemaining_routes() > 0)
				count++;
		}
		return count;
	}
	
	//Method called when StartScreen is loaded. Counts the number of passenger's unpaid fines
	//so that he can be up-to-date and able to pay them
	public int countUnpaidFines() {
		int count = 0;
		for(Fine fine: fines) {
			 if(fine.isPaid() == false)
				 count++;
		}
		return count;
	}
	
	//Method that updates passenger's balance every time a deposit is made
	public void updateBalance(double additionalMoney) {
		balance += additionalMoney;
	}
	
	//Method used for reducing passenger's balance whenever he decides to buy a product
	public void reduceBalance(double reducedMoney) {
		balance -= reducedMoney;
	}

	//Method that refreshes passenger's products ArrayList when a product is purchased
	public void addProduct(Product aProduct) throws WriterException, IOException {
		//Generating qr code for the product, so that it is easier for the ticket inspector to check it browsing this image 
		QRcode.generateQRCodeImage(aProduct.getProduct_num(), 350, 350, ".//QrcodeImages/"+Main.loginUser.getUserNum()+".png");
		products.add(aProduct);
	}
	
	//Getters and setters
	public void setFines(ArrayList<Fine> fines) {
		this.fines = fines;
	}

	public int getCheck() {
		return check;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public double getBalance() {
		return balance;
	}
	
	public String getPassport() {
		return passport;
	}

	public String getEmail() {
		return email;
	}

	public String getCardNum() {
		return cardNum;
	}

	public String getId() {
		return id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public ArrayList<Fine> getFines() {
		return fines;
	}

}