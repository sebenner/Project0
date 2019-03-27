

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends User{
	//int ssn;
	//initial deposit required to create account
	public Customer(String username, String password, String fullName, String address) {
		super(username, password, fullName, address);
	}
	 
	public void view() {
		this.toString();
	}

	@Override
	public void printMainMenu() {
		System.out.println("1. Apply for Account");
		System.out.println("2. Withdraw");
		System.out.println("3. Deposit");
		System.out.println("4. Transfer");
		System.out.println("5. View Personal Information");
		System.out.println("6. View Account Balances");
		System.out.println("7. View Account Information");
		System.out.println("8. Exit");
	}

	@Override
	public int menuItemNumber(int x) throws MainMenuException {
		if(x<9) {
			return x;
		}
		else {
			throw new MainMenuException("Invalid Input");
		}
	}
}