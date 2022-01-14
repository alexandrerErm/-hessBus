import java.io.Serializable;
import java.util.Random;

//Abstract class that models system users (passengers/ticket inspectors)
public abstract class User implements Serializable {
	
	//Attributes
	protected String username;	//used on login process
	protected String password;	//used on login process
	protected String userNum;
	
	//Constructor
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		setUserNum();
	}
	
	//Constructor used when updating Passenger, so as to make a copy of him by value
	public User(String username, String password, String userNum) {
		this.username = username;
		this.password = password;
		this.userNum = userNum;
	}
	
	//Getters and setters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserNum() {
		return userNum;
	}

	//Setting user's unique number randomly based on systems nano time
	public void setUserNum() {
		Random random = new Random(System.nanoTime());
		int randomInt = random.nextInt(1000000000);
		this.userNum= Integer.toString(randomInt);
	}

}
