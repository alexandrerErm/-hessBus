import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fine implements Serializable {

	String date_time;
	Passenger owner;
	double price;	
	String  inspector_num;
	String bus;
	boolean paid;
	//Fine's class constructor
	public Fine(Passenger owner, String inspector_num, String bus) {
		this.date_time = new SimpleDateFormat("yyyy/MM/dd HHmmss").format(Calendar.getInstance().getTime());
		this.owner = owner;
		countPrice();
		this.inspector_num = inspector_num;
		this.bus = bus;
		this.paid = false;
	}
	
	//Changes the status of fine if is paid
	public void finePaid()
	{
		this.paid = true;
	}
	//Default price for all fines
	public void countPrice()
	{
		this.price = 30*owner.getCheck();
	}
	
	//Generating setters and getters
	public Passenger getOwner() {
		return owner;
	}

	public double getPrice() {
		return price;
	}

	public String getDate_time() {
		return date_time;
	}

	public String getBus() {
		return bus;
	}

	public boolean isPaid() {
		return paid;
	}
	
}
