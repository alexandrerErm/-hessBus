import java.io.IOException;
import java.io.Serializable;

import com.google.zxing.WriterException;

public class Card extends Product implements Serializable {

	private int duration;
	private boolean flag = false;
	
	//Card's class constructor
	public Card(double price,Passenger owner, String type, int duration) {
		super(price, owner, type);
		this.duration = duration;
	}
	
	//Using this method from static QrCode class to generate unique qr code for card.
	public void setQR () throws WriterException, IOException {
		super.setQR();
	}
	//Checks card's validation.
	public boolean isValid() {
		String currentYear = getCurrentTime().substring(0, 3);
		String currentMonth = getCurrentTime().substring(5, 7);
		
		int theYear = Integer.parseInt(currentYear);
		int theMonth = Integer.parseInt(currentMonth);
		
		String purchaseYear = getDate_time().substring(0, 3);
		String purchaseMonth = getDate_time().substring(5, 7);
		
		int expireYear = Integer.parseInt(purchaseYear);
		int expireMonth = Integer.parseInt(purchaseMonth);
		
		if(duration == 1) {
			expireMonth ++;
		}
		else if(duration == 3) {
			expireMonth = expireMonth + 3;
		}
		else if(duration == 6) {
			expireMonth = expireMonth + 6;
		}
		else if(duration == 12) {
			expireMonth = expireMonth + 12;
		}
			
		if(expireMonth > 12) {
			expireMonth = expireMonth - 13;
			expireYear ++;
		}
		
		if(theMonth > expireMonth || theYear > expireYear) {
			return false;
		}
		else
			return true;
		
	}	
	
	//These methods are used for a Label which contains a message if the card has expired.
	public boolean isFlag() {
		return flag;
	}
	
	public void setFlagTrue()
	{
		flag = true;
	}
	
	//Generating getters and setters.
	public int getDuration() {
		return duration;
	}

}
