package application;

import java.sql.*;

/**
 * Part of the model package. This class manages the hotel database based on user actions.
 * 
 * @author Pablo Cruz
 *
 */
public class HotelDBManager {
	private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    /**
     * Constructor: Establishes and initializes the connection to the database.
     */
	public HotelDBManager() {
		String url = "jdbc:mysql://174.138.182.12:3306/roadrunn_hotelbooksdb";
		
		try {
			// Establishes the connection.
			connect = DriverManager.getConnection(url, "roadrunn_enduser", "knZi3,_J92HN");
			connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			// Creates the statement to allow SQL queries and updates to execute.
			statement = connect.createStatement();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the user data to the database.
	 * 
	 * @param username	the entered user name.
	 * @param password	the entered password.
	 * @param name		the entered name.
	 * @param email		the entered email.
	 * @param company	the entered company (can be empty).
	 * @param position	the entered position (can be empty).
	 */
	public void addUser(String username, String password, String name, String email, String company, String position) {
		String encryptPass = Encryption.encrypt(password, username);	// Encrypt the password for the database.
		
		try {
			// Inserts the new user with the passed credentials into the User table of the database.
			statement.executeUpdate("INSERT INTO `User` (`userId`, `password`, `name`, `emailAddress`)" +
												"values (\"" + username + "\", \""
															+ encryptPass + "\", \""
															+ name + "\", \""
															+ email + "\");");
			// FIXME: Delete this line once account creation is successful (i.e., credentials must be on database).
			System.out.println("Account creation successful!");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes the search query on hotels based on the keyword string passed to the parameter.
	 * 
	 * @param keywords the string containing keywords separated by a space.
	 */
	public void search(String keywords) {
		// TODO: Write the code to execute the search based on the criteria string passed to the string. Return statements
		// will be dealt with later.
	}
	
	/**
	 * Closes the statement and connection. Call this to close the connection to the database.
	 */
	public void closeManager() {
		try {
			statement.close();
			connect.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the connection object.
	 * 
	 * @return connection.
	 */
	public Connection getConnection() {
		return connect;
	}
	
	/**
	 * Updates the database connection.
	 * 
	 * @param url the URL to connect to. The URL must have the "jdbc:mysql://" protocol. 
	 */
	public void setConnect(String url) {
		try {
			connect.close();
			connect = DriverManager.getConnection(url, "roadrunn_enduser", "password");
			connect.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the SQL statement. A statement can be a query or an update.
	 * 
	 * @return the statement object.
	 */
	public Statement getStatement() {
		return statement;
	}
	
	/**
	 * Updates the statement for the manager object.
	 * 
	 * @param stmt the new statement. This statement can be a query or an update.
	 */
	public void setStatement(Statement stmt) {
		try {
			statement.close();
			statement = stmt;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current prepared statement.
	 * 
	 * @return the prepared statement.
	 */
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	
	/**
	 * Updates the prepared statement for the current connection.
	 * 
	 * @param newPrepStatement the new prepared statement to set.
	 */
	public void setPreparedStatement(PreparedStatement newPrepStatement) {
		preparedStatement = newPrepStatement;
	}
	
	/**
	 * Gets the result set of a query.
	 * 
	 * @return the result set returned from the last executed query.
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	/**
	 * Updates the result set.
	 * 
	 * @param newResultSet the new result set returned from the last executed query.
	 */
	public void setResultSet(ResultSet newResultSet) {
		resultSet = newResultSet;
	}
}
