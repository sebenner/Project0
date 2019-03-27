package com.java.project;

import java.sql.SQLException;
import java.util.List;
//import java.util.List;

public interface DatabaseAccess {

	public void addUser(Customer c) throws SQLException;

	public void addAccount(String uName1, String type, String uName2, Double amount) throws SQLException;

	public User login(String username, String password) throws SQLException;

	public boolean usernameExists(String username) throws SQLException;

	public String userType(String username) throws SQLException;

	public List<Account> pendingAccounts() throws SQLException;
	
	public void setStatus(Account a) throws SQLException;
	
	public Account getAccount(int accountNum) throws SQLException;
	
	public boolean withdraw(float amount, int accountNum) throws SQLException;

	public boolean deposit(float amount, int accountNum) throws SQLException;

	public boolean accountIdExists(int uNum) throws SQLException;
	
	boolean isAccountOwner(int aNum, String uName) throws SQLException;
	/*
	 * public void transfer(); public Account getAccountInformation(); public void
	 * getAccountBalances(); public void getPersonalInformation(); public void
	 * editPersonalInformation(); public void editAccountInformation(); public void
	 * deleteAccount(Object o); public List<Integer> getaccounts(String username);
	 */


}
