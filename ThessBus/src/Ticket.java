import java.io.IOException;
import java.io.Serializable;

import com.google.zxing.WriterException;

public class Ticket extends Product implements Serializable {

	private int no_of_routes;
	private int remaining_routes;
	private String bus;
	private String validation_date_time;
	
	
	public Ticket(double price, Passenger owner, String type, int no_of_routes, String bus) {
		super(price, owner, type);
		this.no_of_routes = no_of_routes;
		this.remaining_routes = this.no_of_routes - 1;
		this.bus = bus;
	}
	
	//set the current time to the validation time 
	public void setValidation_date_time() {
		this.validation_date_time = getCurrentTime();
		
	}
	
	
	@Override
	public void setQR () throws WriterException, IOException {
		super.setQR();
	}
	
	public void printQR() {
		super.printQR();
	}
	
	//Checks if the ticket is valid 
    public boolean isValid() {
		
    	String hoursText = getCurrentTime().substring(11, 13);
    	String minutesText = getCurrentTime().substring(13, 15);
    	int time = Integer.parseInt(hoursText) * 3600 + Integer.parseInt(minutesText) * 60;

    	String purchaseHoursText = getDate_time().substring(11, 13);
    	String purchaseMinutesText = getDate_time().substring(13, 15);
    	int purchaseTime = Integer.parseInt(purchaseHoursText) * 3600 + Integer.parseInt(purchaseMinutesText) * 60;

    	int duration = 0;
    		// set the duration of the ticket
    		switch(no_of_routes) {
    		case 2:
    			duration = 70 * 60;
    			break;
    		case 3:
    			duration = 90 * 60;
    			break;
    		case 4:
    			duration = 120 * 60;
    			break;
    		}
    		
    		if(time > purchaseTime + duration) 
    			return false;
    		else 
    			return true;
    		
    	
    }


	//if the tickets is multi way 
	public void Refresh_num_of_routes() {
		remaining_routes = remaining_routes - 1;
	}

	//Getters and setters
	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public void setNo_of_routes(int no_of_routes) {
		this.no_of_routes = no_of_routes;
	}

	public String getValidation_date_time() {
		return validation_date_time;
	}

	public int getNo_of_routes() {
		return no_of_routes;
	}

	public int getRemaining_routes() {
		return remaining_routes;
	}
	
}
