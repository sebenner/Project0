package com.java.project;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends User implements Serializable{
	private static final long serialVersionUID = 1L;
	//int ssn;
	//initial deposit required to create account
	 //public void viewAccountsOwned() {
		 
	 //}
	public Customer(String username, String password, String fullName, String address) {
		super(username, password, fullName, address);
	}
	 
	public void view() {
		this.toString();
	}

	@Override
	public void printMainMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int menuItemNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
}