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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int menuItemNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
}
