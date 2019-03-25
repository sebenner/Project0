package com.java.project;

import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseAccessImpl implements DatabaseAccess{

	static String url, username, password;
	static {
		try {
			Properties property = new Properties();
			property.load(new FileReader("C:\\Users\\steve\\eclipse-workspace-photon\\Project0\\resources\\project.properties"));
			System.out.println(property.getProperty("driver"));
			url = property.getProperty("url");
			username = property.getProperty("username");
			password = property.getProperty("password");
			Class.forName(property.getProperty("driver"));
		}
		catch (ClassNotFoundException e){
			System.out.println("Cannot find your driver class!");
		}
		catch (IOException e) {
			System.out.println("Cannot read your property file!");
		}
	}
	
	public void addUser(Customer c) throws SQLException {
		Connection con=DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("insert into bankUser (username, passwrd, userType, fullName, address) values (?,?,?,?,?)");
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
		Connection con=DriverManager.getConnection(url, username, password);
		CallableStatement cs = con.prepareCall("call proc1(?,?,?,?)");
		cs.setString(1, c.getUsername());
		cs.setString(2, a.getType());
		cs.setDouble(3, a.getAmount());
		cs.setString(4, a.getStatus());
		cs.executeQuery();
		//con.commit();
		con.close();
		
	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	//Does not work correctly!!!
	@Override
	public boolean usernameExists(String uName) throws SQLException {
		Connection con=DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from bankUser where username = ?");
		st.setString(1, uName);
		ResultSet output = st.executeQuery();
		//con.close();
		return output.next();//output.getString("username").isEmpty();
	}

}
