package com.java.project;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
	public static void main(String[] args) throws SQLException {
		DatabaseAccessImpl dai = DatabaseAccessImpl.getInstance();
		// Customer c1 = new Customer("sebenner","pass5","Steven Benner","701 S.
		// Nedderman Dr., Arlington, Texas 76019");
		InputStream stream = System.in;
		Scanner scanner = new Scanner(stream);
		System.out.println("Welcome to Project 0 Bank");
		// System.out.println("If you an existing customer, type 0. If you would like to
		// register, type 1.");
		System.out.println("1. Existing Customer");
		System.out.println("2. Register");
		System.out.println("3. Exit");
		String username = "user";
		String password = "";
		User currUser = null;
		int loginChoice;
		while (true) {
			String newUserS = scanner.nextLine();
			if (newUserS.equals("1") || newUserS.equals("2") || newUserS.equals("3")) {
				loginChoice = Integer.parseInt(newUserS);
				break;
			}
			System.out.println("Invalid Input");
		}
		if (loginChoice == 1) {
			while (true) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				System.out.print("Password: ");
				password = scanner.nextLine();
				User tempUser = dai.login(username, password);
				if (tempUser != null) {
					currUser = tempUser;
					break;
				} else {
					System.out.println("The username or password is incorrect.");
				}
			}

		} else if (loginChoice == 2) {
			while (true) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				if (!dai.usernameExists(username)) {
					break;
				}
				System.out.println("Username already exists");
			}

			String fullName = "";
			String address = "";
			while (password.equals("")) {
				System.out.print("Password: ");
				password = scanner.nextLine();
			}
			while (fullName.equals("")) {
				System.out.print("Full Name: ");
				fullName = scanner.nextLine();
			}
			while (address.equals("")) {
				System.out.print("Address: ");
				address = scanner.nextLine();
			}

			/*
			 * System.out.print("Social Security Number: "); int ssn =
			 * Integer.parseInt(scanner.nextLine());
			 */
			// drivers license number and expiration date, email address

			Customer c = new Customer(username, password, fullName, address);
			currUser = c;
			dai.addUser(c); // this one line is the diff between being a bank and being a scam
			System.out.println("Thank you for registering");
		} else {
			System.exit(0);
		}

		currUser.printMainMenu();

		int mMChoice = 0;
		while (true) {
			try {
				String tempMMChoice = scanner.nextLine();
				if (tempMMChoice.equals("1") || tempMMChoice.equals("2") || tempMMChoice.equals("3")
						|| tempMMChoice.equals("4") || tempMMChoice.equals("5") || tempMMChoice.equals("6")
						|| tempMMChoice.equals("7") || tempMMChoice.equals("8") || tempMMChoice.equals("9")
						|| tempMMChoice.equals("10") || tempMMChoice.equals("11")) {
					mMChoice = currUser.menuItemNumber(Integer.parseInt(tempMMChoice));
					break;
				}
			} catch (MainMenuException e) {
			}
			System.out.println("Invalid Input");
		}

		//
		switch (mMChoice) {
		case 1: // Apply for Account
			boolean knownType = false;
			String type;
			double deposit;
			while (!knownType) {
				// System.out.println("What type of bank account would you like to apply for?");
				System.out.println("Type of bank account: (Savings, Checking, Joint)");
				// System.out.println("(Savings, Checking, Joint)");
				type = scanner.nextLine();
				if (type.toLowerCase().equals("savings") || type.toLowerCase().equals("checking")) {
					// dai.addAccount(c, a);()
					break;
				} else if (type.toLowerCase().equals("joint")) {
					break;
				}
				System.out.println("Invalid bank account type");
			}
			while (true) {
				System.out.println("Initial Deposit:");
				/*
				 * try { Double tempDeposit = scanner.nextDouble(); } catch
				 */
				// Double.
				// Double.parseDouble(tempDeposit);
				break;
			}
			break;
		case 2: // Withdraw
			System.out.println("Which account would you like to withdraw from?");
			System.out.print("Account Id: ");
			String accId = scanner.nextLine();
			// TODO print account information if found and is owner of
			System.out.println("How much would you like to withdraw?");
			String amount = scanner.nextLine();
			// TODO call withdraw function in account and update amount in database if valid
			break;
		case 3: // Deposit
			break;
		case 4: // Transfer
			break;
		case 5: // View Personal Information
			break;
		case 6: // View Account Information
			break;
		case 7: // View Account Balances
			break;
		case 8: // Exit
			System.exit(0);
			break;
		case 9: // Approve Accounts
			break;
		case 10: // Edit Personal Information
			break;
		case 11: // Edit Account Information
			break;
		case 12: // Cancel Accounts
			break;
		}

		// System.out.println(currUser);

		scanner.close();
	}
}

//Methods:  View account information, View Account Balances, View Personal Information,
//			Edit account information, Edit personal information
//			Withdraw, Deposit, Transfer,						Done
//			Approve/Deny account, Cancel account
//			Apply for Account, Apply for Joint Account			Done
