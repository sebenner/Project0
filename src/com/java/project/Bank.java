package com.java.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Bank implements Serializable{
	private static final long serialVersionUID = 1L;
	String name = "Project 0 Bank";
	Set<User> users;
	Set<Account> accounts;
	Set<CustomerAccount> customerAccounts;
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		Bank bank;
		DatabaseAccessImpl dai = new DatabaseAccessImpl();
		Customer c1 = new Customer("sebenner","pass5","Steven Benner","701 S. Nedderman Dr., Arlington, Texas 76019");
		dai.addUser(c1);
		dai.addAccount(c1, new Account(1,"checking",0.0));
		dai.addAccount(c1, new Account(1,"savings",0.0));
		try {
			FileInputStream fin = new FileInputStream("bank.txt");
			ObjectInputStream in = new ObjectInputStream(fin);
			bank = (Bank) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			new File("bank.txt");
			bank = new Bank();
			bank.users = new HashSet<>();
			bank.accounts = new HashSet<>();
			bank.customerAccounts = new HashSet<>();
			bank.users.add(new BankAdmin("payal", "pass1", "Payal", "701 S. Nedderman Dr., Arlington, Texas 76019"));
			bank.users.add(new Employee("matt", "pass2", "Matt", "701 S. Nedderman Dr., Arlington, Texas 76019"));
			bank.users.add(new Employee("david", "pass3", "David", "701 S. Nedderman Dr., Arlington, Texas 76019"));
		}
		
		System.out.println(new Employee("david", "pass3", "David", "701 S. Nedderman Dr., Arlington, Texas 76019"));
		
		
		
		
		InputStream stream = System.in;
		Scanner scanner = new Scanner(stream);
		System.out.println("Welcome to " + bank.name);
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
		//int register = scanner.nextInt();
		if (newUser == 0) {
			boolean login = false;
			while (!login) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				System.out.print("Password: ");
				password = scanner.nextLine();
				Iterator<User> userIt = bank.users.iterator();
				while(userIt.hasNext() && !login) {
					User user = userIt.next();
					if (user.getUsername().equals(username)
						&& user.getPassword().equals(password)) {
						System.out.println("user correct");
						currUser = user;
						login = true;
					}
				}
				if (!login) {
					System.out.println("The username or password is incorrect.");
				}
				/*
				if(username.equals("sebenner") && password.equals("reee")) {
					System.out.println("Welcome " + username);
					break;
				}
				else {
					System.out.println("Incorrect Username or Password");
				}*/
			}
			
		}
		else {
			boolean uniqueUsername = false;
			while (!uniqueUsername) {
				System.out.print("Username: ");
				username = scanner.nextLine();
				uniqueUsername = true;
				Iterator<User> userIt = bank.users.iterator();
				while(userIt.hasNext() && uniqueUsername) {
					if (userIt.next().getUsername().equals(username)) {
						System.out.println("Username already exists");
						uniqueUsername = false;
						break;
					}
				}
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
			bank.users.add(c);
			currUser = c;
			System.out.println("Thank you for registering");
		}
		
		currUser.printMainMenu();
		
		int action = 0;
		
		switch (action) {
			case 0:
				
			case 1:
			
		}
		if(currUser instanceof Customer) {
			System.out.println("Is a customer");
			//cu
		}
		else if(currUser instanceof BankAdmin) {
			System.out.println("Is a bank admin");
		}
		else if(currUser instanceof Employee) {
			System.out.println("Is an employee");
		}
		
		System.out.println(currUser);
		
		//TODO check if user is Customer, Employee, or BankAdmin
		//TODO login
		//TODO check if customer has any open account
		scanner.close();
		
		FileOutputStream fout = new FileOutputStream("bank.txt");
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(bank);
		out.close();
		/*out.writeObject(bank.employees);
		out.writeObject(bank.customers);
		out.writeObject(bank.accounts);*/
	}
}







//Methods:  View account information, View Account Balances, View Personal Information,
//			Edit account information, Edit personal information
//			Withdraw, Deposit, Transfer,						Done
//			Approve/Deny account, Cancel account
//			Apply for Account, Apply for Joint Account			Done
