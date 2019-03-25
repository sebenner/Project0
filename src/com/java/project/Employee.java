package com.java.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee extends User {	
	
	public Employee(String username, String password, String name, String address) {
		super(username, password, name, address);
	}
	
	public void approveApplication() {
		//viewApplication();
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
