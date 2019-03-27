

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends User {	
	
	public Employee(String username, String password, String name, String address) {
		super(username, password, name, address);
	}
	
	public void approveApplication() {
		//viewApplication();
	}

	@Override
	public void printMainMenu() {
		System.out.println("1. View Personal Information");
		System.out.println("2. View Account Balances");
		System.out.println("3. View Account Information");
		System.out.println("4. Approve Accounts");
		System.out.println("5. Exit");
	}

	@Override
	public int menuItemNumber(int x) throws MainMenuException {
		switch (x) {
		case 1:
			return 5;
		case 2:
			return 6;
		case 3:
			return 7;
		case 4:
			return 9;
		case 5:
			return 8;
		default:
			throw new MainMenuException("Invalid Input");
		}
	}
}
