

//import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
abstract public class User /*implements Serializable*/{
	//private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String fullName;
	private String address;
	
	abstract public void printMainMenu();
	
	abstract public int menuItemNumber(int x) throws MainMenuException;
	
	public static User getInstance(String username, String password, String type, String fullName, String address) {
		switch (type) {
		case "c":
			return new Customer(username, password, fullName, address);
		case "e":
			return new Employee(username, password, fullName, address);
		case "b":
			return new BankAdmin(username, password, fullName, address);
		
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}
		
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User e = (User) obj;
			return this.username == e.getUsername();
		}
		else {
			System.out.println("Test");
			return false;
		}
	}

}
