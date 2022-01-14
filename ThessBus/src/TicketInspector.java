import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.zxing.NotFoundException;

//Class that extends User superclass and models ticket inspectors
public class TicketInspector extends User implements Serializable {

	//Attributes
	public String name;
	public String inspector_num; 
	public HashMap<String, Integer> durations = new HashMap<>();	//estimated bus routes durations

	//Constructor
	public TicketInspector(String aName, String aPassword, String username) {
		super(username, aPassword);
		this.name = aName;

		durations.put("01", 80);   durations.put("01Á", 80);  durations.put("02", 65);   durations.put("02Á", 70);
		durations.put("03", 60);   durations.put("04Á", 85);  durations.put("04Â", 85);  durations.put("04Å", 90);
		durations.put("05", 62);   durations.put("06", 65);   durations.put("07", 60);   durations.put("08", 65);
		durations.put("09", 50);   durations.put("09Á", 54);  durations.put("09Â", 57);  durations.put("10", 64);
		durations.put("11", 60);   durations.put("11Â", 63);  durations.put("12", 70);   durations.put("14", 65);
		durations.put("14Á", 65);  durations.put("15", 35);   durations.put("15Á", 40);  durations.put("14", 40);
		durations.put("17", 57);   durations.put("18", 70);   durations.put("19", 70);   durations.put("19Á", 68);
		durations.put("20", 65);   durations.put("21", 56);   durations.put("21Á", 60);  durations.put("22", 55);
		durations.put("23", 70);   durations.put("24", 65);   durations.put("25", 67);   durations.put("25Á", 68);
		durations.put("26", 75);   durations.put("27", 70);   durations.put("27Á", 72);  durations.put("27Â", 75);
		durations.put("28Á", 82);  durations.put("28Â", 80);  durations.put("29", 75);   durations.put("29Á", 78);
		durations.put("30", 50);   durations.put("31", 70);   durations.put("32", 78);   durations.put("32Á", 78);
		durations.put("33", 40);   durations.put("33Á", 42);  durations.put("34", 45);   durations.put("35", 30);
		durations.put("36", 45);   durations.put("36Á", 45);  durations.put("36Â", 45);  durations.put("36Å", 45);
		durations.put("36Ç", 55);  durations.put("36Ê", 50);  durations.put("36Í", 40);  durations.put("36Ñ", 40);
		durations.put("36Ô", 40);  durations.put("36Õ", 40);  durations.put("36Æ", 45);  durations.put("37", 40);
		durations.put("38", 56);   durations.put("39", 54);   durations.put("39Á", 40);  durations.put("40", 40);
		durations.put("40Á", 40);  durations.put("40Ê", 40);  durations.put("42", 52);   durations.put("42Á", 53);
		durations.put("42Â", 40);  durations.put("43", 46);   durations.put("45", 40);   durations.put("45Á", 40);
		durations.put("45Â", 48);  durations.put("50", 40);   durations.put("51", 45);   durations.put("51Á", 47);
		durations.put("52", 40);   durations.put("52Á", 48);  durations.put("53", 40);   durations.put("54", 455);
		durations.put("54Á", 43);  durations.put("55", 40);   durations.put("55Á", 56);  durations.put("55Â", 40);
		durations.put("55Å", 54);  durations.put("55Ê", 55);  durations.put("55Ì", 57);  durations.put("55Í", 58);
		durations.put("55Ñ", 40);  durations.put("56", 55);   durations.put("56Á", 55);  durations.put("57", 56);
		durations.put("58", 60);   durations.put("59", 62);   durations.put("59Á", 60);  durations.put("59Â", 60);
		durations.put("59Ê", 60);  durations.put("60Á", 56);  durations.put("60Â", 57);  durations.put("60Å", 58);
		durations.put("61", 56);   durations.put("61Á", 57);  durations.put("64", 46);   durations.put("64Á", 46);
		durations.put("64Â", 47);  durations.put("64Å", 45);  durations.put("64Ê", 45);  durations.put("66", 49);
		durations.put("67", 52);   durations.put("67Á", 55);  durations.put("67Â", 55);  durations.put("68Á", 53);
		durations.put("68Â", 53);  durations.put("69Á", 55);  durations.put("69Â", 54);  durations.put("69Ç", 54);
		durations.put("69Ê", 54);  durations.put("69Í", 54);  durations.put("69Ñ", 53);  durations.put("69Ô", 53);
		durations.put("70", 55);   durations.put("70Á", 54);  durations.put("71", 58);   durations.put("71Á", 58);
		durations.put("72", 48);   durations.put("72Á", 47);  durations.put("72Â", 47);  durations.put("76", 45);
		durations.put("76Á", 46);  durations.put("76Â", 47);  durations.put("77", 53);   durations.put("77Á", 54);
		durations.put("77Â", 53);  durations.put("77Å", 53);  durations.put("77Ç", 53);  durations.put("77Ê", 54);
		durations.put("79", 48);   durations.put("79Á", 48);  durations.put("79Â", 48);  durations.put("80", 45);
		durations.put("80Á", 45);  durations.put("80Â", 45);  durations.put("80Å", 46);  durations.put("80Æ", 45);
		durations.put("81", 47);   durations.put("81Á", 47);  durations.put("81Â", 47);  durations.put("81Å", 47);
		durations.put("81Ê", 46);  durations.put("82Á", 51);  durations.put("82Â", 50);  durations.put("82Å", 52);
		durations.put("82Ê", 50);  durations.put("82Ì", 48);  durations.put("82Í", 49);  durations.put("83", 47);
		durations.put("83Á", 44);  durations.put("83Â", 45);  durations.put("83Ì", 46);  durations.put("83Í", 46);
		durations.put("83Ô", 47);  durations.put("83×", 47);  durations.put("84Á", 50);  durations.put("84Â", 51);
		durations.put("85", 46);   durations.put("85Á", 47);  durations.put("85Â", 47);  durations.put("85Ç", 47);
		durations.put("85Ê", 48);  durations.put("85Ì", 48);  durations.put("85Í", 48);  durations.put("85Ô", 48);
		durations.put("85×", 47);  durations.put("85Æ", 47);  durations.put("86", 48);   durations.put("86Á", 48);
		durations.put("86Â", 48);  durations.put("86Å", 48);  durations.put("86Æ", 49);  durations.put("86Ç", 49);
		durations.put("86Ê", 47);  durations.put("86Ë", 47);  durations.put("86Ì", 50);  durations.put("86Í", 49);
		durations.put("86Ñ", 48);  durations.put("86Ô", 48);  durations.put("86×", 49);  durations.put("86Õ", 49);
		durations.put("86Ö", 51);  durations.put("87Á", 51);  durations.put("87Â", 50);  durations.put("87Ã", 50);
		durations.put("87Å", 50);  durations.put("87Æ", 50);  durations.put("87Ç", 52);  durations.put("87È", 51);
		durations.put("87Ë", 51);  durations.put("87Ì", 51);  durations.put("87Í", 50);  durations.put("87Ð", 50);
		durations.put("87Ñ", 50);  durations.put("87Ô", 53);  durations.put("87Ö", 50);  durations.put("87×", 50);
		durations.put("88", 43);   durations.put("88Á", 43);  durations.put("88Â", 44);  durations.put("88Å", 46);
		durations.put("88Ç", 44);  durations.put("88Ê", 42);  durations.put("88Ì", 43);  durations.put("88Í", 43);
		durations.put("89Á", 48);  durations.put("89Â", 49);  durations.put("89Å", 49);  durations.put("89Ê", 49);
		durations.put("89Í", 53);  durations.put("90", 54);   durations.put("90Á", 55);  durations.put("90Â", 50);
		durations.put("90Å", 53);  durations.put("90Ê", 52);  durations.put("91Á", 43);  durations.put("91Â", 40);
		durations.put("91Å", 44);  durations.put("91Ç", 45);  durations.put("91Ê", 45);  durations.put("91Í", 40);
		durations.put("91Ô", 46);  durations.put("91×", 44);  durations.put("91Õ", 42);  durations.put("92", 41);
		durations.put("92Á", 41);  durations.put("92Å", 41);  durations.put("92Ñ", 41);  durations.put("Í1", 69);
		durations.put("Í1Á", 65);  durations.put("×1", 70);
		
	}
	
	//Method called when a ticket inspector has scanned a qr code,
	//so as to decode it and return the product number encrypted in it
	public Product browseQR(String filepath_of_qr) throws FileNotFoundException, NotFoundException, IOException {			
		String product_num;
		Product product;
		
		product_num = QRcode.decodeQRCodeImage(filepath_of_qr);
		
		product = (Product) FileManager.search(product_num, "Products.dat");
		
		return product;
	}
	
	//Method indicating whether a passenger should be fined depending on his product's data
	//duration = {70, 90, 120, 1, 3, 6, 12} depending on the product (multi-way ticket or card)
	//bus, validation_date_time = 0, null if product = card
	//validation_date_time = null if the checked product is a one-way ticket
	public boolean ticketValidation(String date_time, int duration, String bus, String validation_date_time, double price) throws ParseException {
		Set<Integer> foo = new HashSet<>();
		foo.add(1);
		foo.add(3);
		foo.add(6);
		foo.add(12);
		
		String dates = date_time.substring(0,10);
		String times  = date_time.substring(11, 19);
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		Date time = null;
			
		date = sdf.parse(dates);
		time = sdf2.parse(times);
		
		Date current_date;
		current_date = sdf.parse(sdf.format(new Date()));
		Date current_time;
		current_time = sdf2.parse(sdf2.format(new Date()));
		
		//Checking if the product is a card
		if(foo.contains(duration)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			Date expiration_date = cal.getTime();
			expiration_date = sdf.parse(sdf.format(expiration_date));
			
			//Valid card
			if((expiration_date.after(current_date)) || (expiration_date.equals(current_date))) {
				return true;
			}
			else //Not valid card
				return false;
			
		}
		
		//If the product the inspector checking is a ticket
		if(date.equals(current_date)) {
		
			//Checking whether a passenger should be fined on 'N1' or 'N1A' where he should have validated a specific ticket
			if((bus.equals("N1") || bus.equals("N1A")) && (price != 2.0))
				return false;
			
			//One-way tickets test
			if(validation_date_time.equals("-") == false) {
				String validation_times = validation_date_time.substring(11, 19);
				Date validation_time = null;
				validation_time = sdf2.parse(validation_times);
				long diff = validation_time.getTime() - time.getTime();
			
				if(diff/(1000*60) <= duration) {
					
					long diff2 = current_time.getTime() - validation_time.getTime();
					if(diff2/(1000*60) > durations.get(bus))
						return false;
					return true;
				}
				return false;
			}
			//Multi-way tickets test
			else {
				long diff2 = current_time.getTime() - time.getTime();
				if(diff2/(1000*60) > durations.get(bus))
					return false;
				return true;
			}
		
		}
		else
			return false;
	}

}
