package com.java.project;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseAccessImpl implements DatabaseAccess, Cloneable {

	private static String url, username, password;
	private static DatabaseAccessImpl obj;

	static {
		try {
			Properties property = new Properties();
			property.load(new FileReader(
					"C:\\Users\\steve\\eclipse-workspace-photon\\Project0\\resources\\project.properties"));
			System.out.println(property.getProperty("driver"));
			url = property.getProperty("url");
			username = property.getProperty("username");
			password = property.getProperty("password");
			Class.forName(property.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot find your driver class!");
		} catch (IOException e) {
			System.out.println("Cannot read your property file!");
		}
	}

	private DatabaseAccessImpl() {
	}

	public void addUser(Customer c) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement(
				"insert into bankUser (username, passwrd, userType, fullName, address) values (?,?,?,?,?)");
		st.setString(1, c.getUsername());
		st.setString(2, c.getPassword());
		st.setString(3, "c");
		st.setString(4, c.getFullName());
		st.setString(5, c.getAddress());
		st.executeUpdate();
		con.close();
	}

	@Override
	public void addAccount(Customer c, Account a) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		CallableStatement cs = con.prepareCall("call proc1(?,?,?,?)");
		cs.setString(1, c.getUsername());
		cs.setString(2, a.getType());
		cs.setDouble(3, a.getAmount());
		cs.setString(4, a.getStatus());
		cs.executeQuery();
		// con.commit();
		con.close();

	}

	@Override
	public User login(String uName, String passwrd) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from bankUser where username = ? and passwrd = ?");
		st.setString(1, uName);
		st.setString(2, passwrd);
		ResultSet output = st.executeQuery();
		if (output.next()) {
			User temp = User.getInstance(uName, passwrd, output.getString(3), output.getString(4), output.getString(5));
			con.close();
			return temp;
		} else {
			con.close();
			return null;
		}
	}

	@Override
	public boolean usernameExists(String uName) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from bankUser where username = ?");
		st.setString(1, uName);
		ResultSet output = st.executeQuery();
		// con.close();
		return output.next();// output.getString("username").isEmpty();
	}

	@Override
	public boolean withdraw(float amount, int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select amount from useraccount where acid = ?");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();
		float balance = 0;

		if (output.next()) {
			balance = output.getFloat("amount");
		} else {
			System.out.println("Account balance not found");
		}

		if (balance != 0) {
			balance = balance - amount;
			st = con.prepareStatement("update useraccount set amount=? where acid=?");
			st.setFloat(1, balance);
			st.setInt(2, accountNum);
			st.executeUpdate();
			return true;
		} else {
			System.out.println("Account balance 0");
		}
		con.close();
		return false;
	}

	@Override
	public boolean deposit(float amount, int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select amount from useraccount where acid = ?");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();
		float balance = 0;

		if (output.next()) {
			balance = output.getFloat("amount");
		} else {
			System.out.println("Account balance not found");
		}

		if (balance != 0) {
			balance = balance + amount;
			st = con.prepareStatement("update useraccount set amount=? where acid=?");
			st.setFloat(1, balance);
			st.setInt(2, accountNum);
			st.executeUpdate();
			return true;
		} else {
			System.out.println("Account balance 0");
		}
		con.close();
		return false;
	}


	// Singleton necessities:
	public static DatabaseAccessImpl getInstance() {
		if (obj == null) {
			synchronized (DatabaseAccessImpl.class) {
				if (obj == null) {
					obj = new DatabaseAccessImpl();
				}
			}
		}
		return obj;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return obj;
	}

}
