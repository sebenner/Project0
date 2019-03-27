

import java.io.InputStream;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Bank {
	static Logger logger = Logger.getLogger(Bank.class);
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
				User tempUser = dai.login(username, password, false);
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

		while (true) {

		int mMChoice = 0;
		while (true) {
			currUser.printMainMenu();

			try {
				String tempMMChoice = scanner.nextLine();
				if (tempMMChoice.equals("1") || tempMMChoice.equals("2") || tempMMChoice.equals("3")
						|| tempMMChoice.equals("4") || tempMMChoice.equals("5") || tempMMChoice.equals("6")
						|| tempMMChoice.equals("7") || tempMMChoice.equals("8") || tempMMChoice.equals("9")
						|| tempMMChoice.equals("10")) {
					mMChoice = currUser.menuItemNumber(Integer.parseInt(tempMMChoice));
					break;
				}
			} catch (MainMenuException e) {
			}
			System.out.println("Invalid Input");
		}
		switch (mMChoice) {
		case 1: // Apply for Account
			applyForAccount(dai, scanner, currUser);
			break;
		case 2: // Withdraw
			withdraw(dai, scanner, currUser);
			break;
		case 3: // Deposit
			deposit(dai, scanner, currUser);
			break;
		case 4: // Transfer
			transfer(dai, scanner, currUser);
			break;
		case 5: // View Personal Information
			System.out.println(currUser);	
			break;
		case 6: // View Account Balances
			viewBalance(dai,scanner, currUser);
			break;
		case 7: // View Account Info
				viewAllAccounts(dai,scanner, currUser);
			break;
		case 8: // Exit
			scanner.close();
			System.exit(0);
			break;
		case 9: // Approve Accounts
			approveAccounts(dai, scanner);
			break;
		case 10: // Edit Personal Information
			editPersonalInfo(dai, scanner);
			break;
		case 11: // Edit Account Information
			editAccountInfo(dai, scanner);			
			break;
		}
		}
	}
	public static String applyForAccount(DatabaseAccessImpl dai, Scanner scanner, User currUser) throws SQLException {
		String type;
		double deposit = 0.0;
		String jointOwner = "";
		while (true) {
			System.out.println("Type of bank account: (Savings, Checking, Joint)");
			type = scanner.nextLine();
			if (type.toLowerCase().equals("savings") || type.toLowerCase().equals("checking")) {
				break;
			} else if (type.toLowerCase().equals("joint")) {
				while (true) {
					System.out.println("Who are you sharing this joint account with?");
					System.out.print("username: ");
					jointOwner = scanner.nextLine();
					if (dai.usernameExists(jointOwner) && !jointOwner.equals(currUser.getUsername())) {
						System.out.println("usertype = " + dai.userType(jointOwner));
						if (dai.userType(jointOwner).equals("c")) {
							break;
						}
						System.out.println("User is not a customer");
					} else {
						System.out.println("Invalid username");
					}
				}
				break;
			}
			System.out.println("Invalid bank account type");
		}
		while (true) {
			System.out.println("Initial Deposit:");
			try {
				Double tempDeposit = scanner.nextDouble();
				scanner.nextLine();
				if (tempDeposit > 0.0) {
					deposit = tempDeposit;
					break;
				} else {
					System.out.println("Initial deposit must be greater than 0.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input");
			}
			break;
		}
		dai.addAccount(currUser.getUsername(), type, jointOwner, deposit);
		System.out.println("Bank Account created");
		return type;
	}
	public static void withdraw(DatabaseAccessImpl dai, Scanner scanner, User currUser) throws SQLException {
		int accId = -1;
		boolean accountExists = false, isOwner = false, isNumber = false;
		while (!isNumber || !accountExists || !isOwner) {
			isNumber = false;
			accountExists = false;
			
			System.out.println("Which account would you like to withdraw from?");
			System.out.print("Account Id: ");
			String accIdStr = scanner.nextLine();
			try {
				accId = Integer.parseInt(accIdStr);
				isNumber = true;
			}
			catch (NumberFormatException e){
				System.out.println("Invalid Input");
			}
			if (isNumber) {
				accountExists = dai.accountIdExists(accId);
				if (accountExists) {
					isOwner = dai.isAccountOwner(accId, currUser.getUsername()) ||
								(currUser instanceof BankAdmin);
					if (!isOwner) {
						System.out.println("You don't own that account.");
					}
				}
				else {
					System.out.println("That account doesn't exist.");
				}
			}
		}

		System.out.println("Account Found.");

		String amount = "";
		do {
			System.out.println("How much would you like to withdraw?");
			amount = scanner.nextLine();
		} while (!amount.matches("-?\\d+(\\.\\d+)?"));

		Account temp = new Account(accId);
		try {
			temp.withdraw(Float.parseFloat(amount));
		} catch (AccountException e) {
			System.out.println(e);
		}
	}
	public static void deposit(DatabaseAccessImpl dai, Scanner scanner, User currUser) throws SQLException {
		String accIdStr;
		String amount;
		Account temp;
		accIdStr = "";
		int accId = -1;
		boolean isNumber = false, accountExists = false, isOwner = false;
		do {
			isNumber = false;
			accountExists = false;
			System.out.println("Which account would you like to deposit to?");
			System.out.print("Account Id: ");
			accIdStr = scanner.nextLine();
			
			try {
				accId = Integer.parseInt(accIdStr);
				isNumber = true;
			}
			catch (NumberFormatException e){
				System.out.println("Invalid Input");
			}
			// check if account exists
			accountExists = dai.accountIdExists(Integer.parseInt(accIdStr));
			if (accountExists) {
				// check if account is owned by logged in user
				isOwner = dai.isAccountOwner(accId, currUser.getUsername()) ||
						(currUser instanceof BankAdmin);
				if (!isOwner) {
					System.out.println("You don't own that account.");
				}
			}
			else {
				System.out.println("That account doesn't exist.");
			}

		} while (!isNumber || !accountExists || !isOwner);

		System.out.println("Account Found.");

		amount = "";
		do {
			System.out.println("How much would you like to deposit?");
			amount = scanner.nextLine();
		} while (!amount.matches("-?\\d+(\\.\\d+)?"));

		temp = new Account(accId);
		try {
			temp.deposit(Float.parseFloat(amount));
		} catch (AccountException e) {
			System.out.println(e);
		}
	}
	public static void transfer(DatabaseAccessImpl dai, Scanner scanner, User currUser) throws SQLException {
		String accIdStr;
		String amount;
		Account temp;
		int accId = -1;
		boolean isNumber = false, accountExists = false, isOwner = false;
		do {
			isNumber = false;
			accountExists = false;
			isOwner = false;
			System.out.println("Which account would you like to transfer FROM?");
			System.out.print("Account Id: ");
			accIdStr = scanner.nextLine();
			
			try {
				accId = Integer.parseInt(accIdStr);
				isNumber = true;
			}
			catch (NumberFormatException e){
				System.out.println("Invalid Input");
			}
			// check if account exists
			accountExists = dai.accountIdExists(accId);
			if (accountExists) {
				// check if account is owned by logged in user
				isOwner = dai.isAccountOwner(accId, currUser.getUsername()) ||
							(currUser instanceof BankAdmin);
				if (!isOwner) {
					System.out.println("You don't own that account.");
					}
			}
			else {
				System.out.println("That account doesn't exist.");
			}

		} while (!isNumber || !accountExists || !isOwner);
			// check if account exists
			//if (!dai.accountIdExists(Integer.parseInt(accId))) {
			//	System.out.println("That account doesn't exist.");
			//}
			// check if account is owned by logged in user
			//if (!dai.isAccountOwner(Integer.parseInt(accId), currUser.getUsername())/* ||
			//	!(currUser instanceof BankAdmin)*/) {
			//	System.out.println("You don't own that account.");
			//}
		//} while (!accId.matches("-?\\d+(\\.\\d+)?") && !dai.accountIdExists(Integer.parseInt(accId))
		//		&& !dai.isAccountOwner(Integer.parseInt(accId), currUser.getUsername()));

		System.out.println("Account Found.");

		String accId2Str = "";
		int accId2 = -1;
		do {
			isNumber = false;
			accountExists = false;
			isOwner = false;
			System.out.println("Which account would you like to transfer TO?");
			System.out.print("Account Id: ");
			accId2Str = scanner.nextLine();
			try {
				accId2 = Integer.parseInt(accId2Str);
				isNumber = true;
			}
			catch (NumberFormatException e){
				System.out.println("Invalid Input");
			}
			// check if account exists
			accountExists = dai.accountIdExists(accId2);
			if (accountExists) {
				// check if account is owned by logged in user
				/*isOwner = dai.isAccountOwner(accId2, currUser.getUsername()) ||
							(currUser instanceof BankAdmin);
				if (!isOwner) {
					System.out.println("You don't own that account.");
					}*/
			}
			else {
				System.out.println("That account doesn't exist.");
			}

		} while (!isNumber || !accountExists);
		/*
			// check if account exists
			if (!dai.accountIdExists(Integer.parseInt(accId2))) {
				System.out.println("That account doesn't exist.");
			}
			// check if account is owned by logged in user
			if (!dai.isAccountOwner(Integer.parseInt(accId2), currUser.getUsername())) {
				System.out.println("You don't own that account.");
			}
		} while (!accId.matches("-?\\d+(\\.\\d+)?") && !dai.accountIdExists(Integer.parseInt(accId))
				&& !dai.isAccountOwner(Integer.parseInt(accId2), currUser.getUsername()));
		*/
		System.out.println("Account Found.");

		amount = "";
		do {
			System.out.println("How much would you like to transfer?");
			amount = scanner.nextLine();
		} while (!amount.matches("-?\\d+(\\.\\d+)?"));

		temp = new Account(accId);
		Account temp2 = new Account(accId2);
		try {
			temp.transfer(temp2, Float.parseFloat(amount));
		} catch (AccountException e) {
			System.out.println(e);
		}
	}
	public static void viewBalance(DatabaseAccessImpl dai,Scanner scanner, User currUser)throws SQLException{
		String accId = "";
		do {
			System.out.println("Which account balance would you like to view?");
			System.out.print("Account Id: ");
			accId = scanner.nextLine();
			// check if account exists
			if (!dai.accountIdExists(Integer.parseInt(accId))) {
				System.out.println("That account doesn't exist.");
			}
			// check if account is owned by logged in user
			if (!dai.isAccountOwner(Integer.parseInt(accId), currUser.getUsername())) {
				System.out.println("You don't own that account.");
			}
		} while (!accId.matches("-?\\d+(\\.\\d+)?") && !dai.accountIdExists(Integer.parseInt(accId))
				&& !dai.isAccountOwner(Integer.parseInt(accId), currUser.getUsername()));
		if (!dai.checkAccountBalance(Integer.parseInt(accId), true)) {
			System.out.println("Status of Account: "
					+ DatabaseAccessImpl.getInstance().checkAccountStatus(Integer.parseInt(accId)));
		}
	}
	public static void viewAllAccounts(DatabaseAccessImpl dai,Scanner scanner, User currUser) throws SQLException{
		List<Account> accounts = DatabaseAccessImpl.getInstance()
		.returnAccountsByUsername(currUser.getUsername());
		for (Account a : accounts) {
			System.out.println(a);
		}
	}
	public static void approveAccounts(DatabaseAccessImpl dai, Scanner scanner) throws SQLException {
		List<Account> pendingList = dai.pendingAccounts();
		ListIterator<Account> lIterator = pendingList.listIterator();
		while (lIterator.hasNext()) {
			logger.debug("Iterator loop");
			Account currAccount = lIterator.next();
			String decision;
			while(true) {
				logger.debug("Approve/Deny loop");
				System.out.println(currAccount);
				System.out.println("1. Approve");
				System.out.println("2. Deny");
				decision = scanner.nextLine();
				if (decision.equals("1")) {
					currAccount.setStatus("active");
					break;
				}
				else if (decision.equals("2")) {
					currAccount.setStatus("denied");
					break;
				}
				System.out.println("Invalid Input");
			}
			dai.setStatus(currAccount);
		}
	}
	public static void editPersonalInfo(DatabaseAccessImpl dai, Scanner scanner) throws SQLException {
		while (true) {
			System.out.println("Which personal account do you want to edit?");
			System.out.print("Username: ");
			String uName = scanner.nextLine();
			User changeUser = dai.login(uName, "", true);
			boolean userChanged = false;
			if (changeUser != null) {
				boolean returnToMM = false;
				while (!returnToMM) {
					System.out.println("1. Change Password");
					System.out.println("2. Change Full Name");
					System.out.println("3. Change Adress");
					System.out.println("4. Return to main menu");
					String pChoiceStr = scanner.nextLine();
					if (pChoiceStr.equals("1") || pChoiceStr.equals("2") || pChoiceStr.equals("3")
							|| pChoiceStr.equals("4")) {
						int pChoice = Integer.parseInt(pChoiceStr);
						switch (pChoice) {
						case 1:
							System.out.print("Password: ");
							String newPassword = scanner.nextLine();
							changeUser.setPassword(newPassword);
							userChanged = true;
							break;
						case 2:
							System.out.print("Full Name: ");
							String newFullName = scanner.nextLine();
							changeUser.setFullName(newFullName);
							userChanged = true;
							break;
						case 3:
							System.out.print("Address: ");
							String newAddress = scanner.nextLine();
							changeUser.setAddress(newAddress);
							userChanged = true;
							break;
						case 4:
							returnToMM = true;
							break;
						}
					}
					else {
						System.out.println("Invalid Input");
					}
				}
				if (userChanged) {
					dai.updateUser(changeUser);
				}
				break;
			}
			else {
				System.out.println("Username not found");
			}
		}
	}
	public static void editAccountInfo(DatabaseAccessImpl dai, Scanner scanner) throws SQLException {
		Account currAcc;
		while(true) {
			System.out.println("Which bank account do you want to edit?");
			System.out.print("Account ID: ");
			String accId3String = scanner.nextLine();
			int accId3=0;
			boolean valid = true;
			try {
				accId3 = Integer.parseInt(accId3String);
			}
			catch (NumberFormatException e){
				valid = false;
				System.out.println("Invalid input");
			}
			if (valid) {
				currAcc = dai.getAccount(accId3);
				if (currAcc != null) {
					while(true) {
						System.out.println(currAcc.toString());
						System.out.println("What status do you want to change the account to?");
						System.out.println("(active, frozen, closed)");
						String newStatus = scanner.nextLine();
						if (newStatus.toLowerCase().equals("active") || newStatus.toLowerCase().equals("frozen"))
							{
							currAcc.setStatus(newStatus);
							dai.setStatus(currAcc);
							break;
						}
						else if (newStatus.toLowerCase().equals("closed")){
							currAcc.setAmount(0);
							dai.setAmount(currAcc);
							currAcc.setStatus(newStatus);
							dai.setStatus(currAcc);
							break;
						}
						else {
							System.out.println("Invalid Input");
						}
					}
					
					break;
				}
				else {
					System.out.println("Account Id not found");
				}
			}
		}
	}
}
