

import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseAccessImpl implements DatabaseAccess, Cloneable {

	private static String url, username, password;
	private static DatabaseAccessImpl obj;

	static {
		try {
			Properties property = new Properties();
			property.load(new FileReader(".\\src\\main\\resources\\project.properties"));
			// System.out.println(property.getProperty("driver"));
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

	@Override
	public void addUser(Customer c) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		System.out.println(con.getAutoCommit());
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
	public void addAccount(String uName1, String type, String uName2, Double amount) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		CallableStatement cs = con.prepareCall("call proc1(?,?,?,?)");
		cs.setString(1, uName1);
		cs.setString(2, type);
		cs.setString(3, uName2);
		cs.setDouble(4, amount);
		cs.executeQuery();
		// con.commit();
		con.close();

	}

	@Override
	public User login(String uName, String passwrd, boolean isAdmin) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st;
		if (!isAdmin) {
			st = con.prepareStatement("select * from bankUser where username = ? and passwrd = ?");
			st.setString(1, uName);
			st.setString(2, passwrd);
		}
		else {
			st = con.prepareStatement("select * from bankUser where username = ?");
			st.setString(1, uName);
		}
		ResultSet output = st.executeQuery();
		if (output.next()) {
			User temp = User.getInstance(uName, output.getString(2), output.getString(3), output.getString(4), output.getString(5));
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
		return output.next();
	}

	@Override
	public boolean accountIdExists(int aNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from useraccount where accid = ?");
		st.setInt(1, aNum);
		ResultSet output = st.executeQuery();
		// con.close();
		return output.next();// output.getString("username").isEmpty();
	}

	@Override
	public boolean isAccountOwner(int aNum, String uName) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select username from customeraccounts where accid=?");
		st.setInt(1, aNum);
		ResultSet output = st.executeQuery();
		boolean owner = false;

		if (output.next()) {
			do {
				owner = uName.matches(output.getString("username"));
				if (owner)
					break;
			} while (output.next());
		}
		// con.close();
		return owner;// output.getString("username").isEmpty();
	}

	@Override
	public List<Account> returnAccountsByUsername(String userName) throws SQLException{
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select customeraccounts.accid, useraccount.acctype, useraccount.amount, useraccount.status from customeraccounts left join useraccount on useraccount.accid = customeraccounts.accid where customeraccounts.username = ?");
		st.setString(1, userName);
		ResultSet output =st.executeQuery();

		Account temp;
		List<Account> list = new ArrayList<Account>();
		if(output.next()){
			do{
				temp = new Account(output.getInt("accid"), output.getString("acctype"), output.getDouble("amount"),
						output.getString("status"));
				list.add(temp);
			}while(output.next());
		}

		return list;
	}

	@Override
	public boolean withdraw(float amount, int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con
				.prepareStatement("select amount from useraccount where accid = ? and status = 'active'");
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
			st = con.prepareStatement("update useraccount set amount=? where accid=? and status = 'active'");
			st.setFloat(1, balance);
			st.setInt(2, accountNum);
			st.executeUpdate();
			obj.returnAccountBalance(accountNum);
			return true;
		} else {
			obj.returnAccountBalance(accountNum);
		}
		con.close();
		return false;
	}

	@Override
	public boolean deposit(float amount, int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con
				.prepareStatement("select amount from useraccount where accid = ? and status = 'active'");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();
		float balance = 0;

		if (output.next()) {
			balance = output.getFloat("amount");
		} else {
			System.out.println("Account balance not found");
			con.close();
			return false;
		}

		balance = balance + amount;
		st = con.prepareStatement("update useraccount set amount=? where accid=? and status = 'active'");
		st.setFloat(1, balance);
		st.setInt(2, accountNum);
		st.executeUpdate();

		obj.returnAccountBalance(accountNum);

		con.close();
		return true;

	}

	public boolean checkAccountBalance(int accountNum, boolean printToScreen) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con
				.prepareStatement("select amount from useraccount where accid = ? and status = 'active'");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();

		if (!output.next()) {
			System.out.println("Account balance not found");
			con.close();
			return false;
		}

		if (printToScreen) {
			System.out.printf("\nCurrent Balance of Account: #%d is $%,.2f\n", accountNum, output.getFloat("amount"));
		}
		con.close();
		return true;
	}

	public float returnAccountBalance(int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con
				.prepareStatement("select amount from useraccount where accid = ? and status = 'active'");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();
		float balance = 0.0f;

		if (output.next() && this.checkAccountBalance(accountNum, false)) {
			balance = output.getFloat("amount");
		}

		return balance;
	}

	public String checkAccountStatus(int accountNum) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select status from useraccount where accid = ?");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();

		String str = "";
		if (output.next()) {
			str = output.getString("status");
		}
		return str;
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

	@Override
	public String userType(String uName) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select userType from bankUser where username = ?");
		st.setString(1, uName);
		ResultSet output = st.executeQuery();
		output.next();
		return output.getString(1);
	}

	@Override
	public List<Account> pendingAccounts() throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from userAccount where status = 'pending'");
		ResultSet output = st.executeQuery();
		List<Account> pendingList = new ArrayList<>();
		while(output.next()) {
			pendingList.add(new Account(output.getInt(1),output.getString(2),
										output.getDouble(3),output.getString(4)));
		}
		return pendingList;
	}

	@Override
	public void setStatus(Account a) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement(
				"update userAccount set status = ? where accId = ?");
		st.setString(1, a.getStatus());
		st.setInt(2, a.getAccountId());
		st.executeUpdate();
		con.close();
	}

	@Override
	public Account getAccount(int accountNum) throws SQLException {
		Connection con=DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement("select * from userAccount where accId = ?");
		st.setInt(1, accountNum);
		ResultSet output = st.executeQuery();
		if (output.next()) {
			return new Account(output.getInt(1),output.getString(2),
					output.getDouble(3),output.getString(4));
		}
		else {
			return null;
		}
	}

	@Override
	public void setAmount(Account a) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement(
				"update userAccount set amount = ? where accId = ?");
		st.setDouble(1, a.getAmount());
		st.setInt(2, a.getAccountId());
		st.executeUpdate();
		con.close();
		
	}

	@Override
	public void updateUser(User u) throws SQLException {
		Connection con = DriverManager.getConnection(url, username, password);
		PreparedStatement st = con.prepareStatement(
				"update bankUser set passwrd = ?, fullname = ?, address = ? where username = ?");
		st.setString(1, u.getPassword());
		st.setString(2, u.getFullName());
		st.setString(3, u.getAddress());
		st.setString(4, u.getUsername());
		st.executeUpdate();
		con.close();
	}
}
