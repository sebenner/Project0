package com.java.project;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		DatabaseAccessImpl dai = new DatabaseAccessImpl();
		//Customer c1 = new Customer("sebenner","pass5","Steven Benner","701 S. Nedderman Dr., Arlington, Texas 76019");
		InputStream stream = System.in;
		Scanner scanner = new Scanner(stream);
		System.out.println("Welcome to Project 0 Bank");
		System.out.println("If you an existing customer, type 0. If you would like to register, type 1.");
		String username = "user";
		String password = "";
		User currUser = null;
		int newUser;
		while (true) {
			String newUserS = scanner.nextLine();
			if (newUserS.equals("0") || newUserS.equals("1")) {
				newUser = Integer.parseInt(newUserS);
				break;
			}
			System.out.println("Invalid Input, Please type '0' if you are an existing customer or type '1' if you would like to register.");
		}
		if (newUser == 0) {
			boolean login = false;
			while (!login) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				System.out.print("Password: ");
				password = scanner.nextLine();
				//if dai.usernameExists(username)
				//TODO check if user exists in database
				if (!login) {
					System.out.println("The username or password is incorrect.");
				}
			}
			
		}
		else {
			while (true) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				if (!dai.usernameExists(username)) {
					break;
				}
				break;
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

			/*System.out.print("Social Security Number: ");
			int ssn = Integer.parseInt(scanner.nextLine());*/
			// drivers license number and expiration date, email address
			
			Customer c = new Customer(username, password, fullName, address);
			currUser = c;
			System.out.println("Thank you for registering");
		}
		
		currUser.printMainMenu();
		
		int action = 0;
		
		//
		switch (action) {
			case 0:
				
			case 1:
			
		}
		
		//System.out.println(currUser);

		scanner.close();
	}
}







//Methods:  View account information, View Account Balances, View Personal Information,
//			Edit account information, Edit personal information
//			Withdraw, Deposit, Transfer,						Done
//			Approve/Deny account, Cancel account
//			Apply for Account, Apply for Joint Account			Done
