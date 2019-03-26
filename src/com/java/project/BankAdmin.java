package com.java.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BankAdmin extends Employee{
	//private static final long serialVersionUID = 1L;

	public BankAdmin(String username, String password, String name, String address) {
		super(username, password, name, address);
	}
	
	@Override
	public void printMainMenu() {
		System.out.println("1. Withdraw");
		System.out.println("2. Deposit");
		System.out.println("3. Transfer");
		System.out.println("4. View Personal Information");
		System.out.println("5. View Account Balances");
		System.out.println("6. View Account Information");
		System.out.println("7. Edit Personal Information");
		System.out.println("8. Edit Account Information");
		System.out.println("9. Approve Accounts");
		System.out.println("10. Cancel Accounts");
		System.out.println("11. Exit");
	}

	@Override
	public int menuItemNumber(int x) throws MainMenuException {
		switch (x) {
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		case 4:
			return 5;
		case 5:
			return 6;
		case 6:
			return 7;
		case 7:
			return 10;
		case 8:
			return 11;
		case 9:
			return 9;
		case 10:
			return 12;
		case 11:
			return 8;
		default:
			throw new MainMenuException("Nope");
		}
	}
}
