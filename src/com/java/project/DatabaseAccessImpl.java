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
			property.load(new FileReader("C:\\Users\\steve\\eclipse-workspace-photon\\JavaDay9M\\src\\main\\resources\\project.properties"));
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

		//Statement st=con.createStatement();
		//int rowcount = st.executeUpdate("insert into bankUser (username, passwrd, userType, fullName, address) values ('sebenner','ack','c','Steven Benner', '14507 Oakwood Pl Ne, Albuquerque, NM 87123')");
		//con.commit();
		con.close();
	}
	
	@Override
	public void addAccount(Customer c, Account a) throws SQLException {
		Connection con=DriverManager.getConnection(url, username, password);
		//con.prepareCall("call proc1(?,?,?,?,?)");
		CallableStatement cs = con.prepareCall("call proc1(?,?,?,?)");
		cs.setString(1, c.getUsername());
		//cs.setInt(2, a.getAccountId());
		cs.setString(2, a.getType());
		cs.setDouble(3, a.getAmount());
		cs.setString(4, a.getStatus());
		cs.executeQuery();
		//con.commit();
		con.close();
		
	}

	/*@Override
	public void setAmount(Account a) {
		// TODO Auto-generated method stub
		
	}*/
	
	public static void main(String[] args) throws SQLException {
		//DatabaseAccessImpl dai = new DatabaseAccessImpl();
		//dai.addUser();
	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean usernameExists(String username) throws SQLException {
		Connection con=DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from bankUser where username = ?");
		st.setString(1, username);
		//st.setString(2, c.getPassword());
		//st.setString(3, "c");
		//st.setString(4, c.getFullName());
		//st.setString(5, c.getAddress());
		ResultSet output = st.executeQuery();
		output.getString("username");
		//st.executeUpdate();
		return output.getString("username").isEmpty();
	}




}
