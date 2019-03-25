package com.java.project;

import java.sql.SQLException;
//import java.util.List;

public interface DatabaseAccess {
public void addUser(Customer c) throws SQLException;
public void addAccount(Customer c, Account a) throws SQLException;
public boolean login(String username, String password);
public boolean usernameExists(String username) throws SQLException;
//public void setAmount(Account a);
/*public void withdraw();
public void deposit();
public void transfer();
public Account getAccountInformation();
public void getAccountBalances();
public void getPersonalInformation();
public void editPersonalInformation();
public void editAccountInformation();
public void deleteAccount(Object o);
public List<Integer> getaccounts(String username);*/
}
