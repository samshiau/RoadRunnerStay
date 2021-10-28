package application;

import java.sql.*;
import java.util.regex.*;
import java.util.ArrayList;

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
		System.out.println("testing");
		System.out.println("testing23");
		
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
	 * @returns			RC_OK if the account creation is successful. RC_MISSING_ENTRY if a required field is missing,
	 * 					or RC_MISC_ERR if some other error occurred.
	 */
	public int addUser(String username, String password, String name, String email, String company, String position) {
		Pattern emailSyntax = Pattern.compile("[A-Za-z0-9]@[A-Za-z]+\\.[a-z]");
		Matcher matcher = emailSyntax.matcher(email);
		boolean syntaxIsCorrect = matcher.find();
		
		// Checks to make sure required entries are entered.
		if (username.equals("") || password.equals("") || name.equals("") || email.equals("")) {
			return ReturnCodes.RC_MISSING_ENTRY;
		}
		else if (!syntaxIsCorrect) {
			// Ensures the email syntax is correct.
			return ReturnCodes.RC_EMAIL_SYN_WRONG;
		}
		
		String encryptPass = Encryption.encrypt(password, username);	// Encrypt the password for the database.
		
		try {
			if (company.equals("") || position.equals("")) {
				// Inserts a new customer with the passed credentials into the User table of the database.
				statement.executeUpdate("INSERT INTO `User` (`userId`, `password`, `name`, `emailAddress`)" +
													"VALUES (\"" + username + "\", \""
																+ encryptPass + "\", \""
																+ name + "\", \""
																+ email + "\");");
			}
			else {
				// Adds a new employee given the credentials which includes the company and position.
				statement.executeUpdate("INSERT INTO `User` (`userId`, `password`, `userType`, `name`, `emailAddress`)" +
										"VALUES (\"" + username + "\", \""
													+ encryptPass + "\", \""
													+ "EMPLOYEE\", \""
													+ name + "\", \""
													+ email + "\");");
			}
			// FIXME: Delete this line once account creation is successful (i.e., credentials must be on database).
			System.out.println("Account creation successful!");
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLIntegrityConstraintViolationException e) {
			// If a duplicate username exists in the database.
			return ReturnCodes.RC_DUP_USER;
		}
		catch (SQLException e) {
			// If any other SQLException occurs.
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
		}
	}
	
	/**
	 * Logs the user into the system, but first checks if the user exists and that their password matches.
	 * 
	 * @param username	the entered username.
	 * @param password	the entered password.
	 * @return			RC_OK if the login was successful, RC_MISSING_ENTRY if either of the text fields are empty,
	 * 					RC_INVALID_CRED if the username is not found or the password is incorrect, or RC_MISC_ERR if
	 * 					some other error occurred.
	 */
	public int login(String username, String password) {
		String foundUser = "";
		String foundPass = "";
		String decryptPass;
		
		try {
			// Ensures the fields the user entered are not empty.
			if (username.equals("") || password.equals("")) {
				return ReturnCodes.RC_MISSING_ENTRY;
			}
			
			// Only stores the username and password into the result set as to not waste resources during authentication.
			resultSet = statement.executeQuery("SELECT DISTINCT u.userId, u.password FROM User u WHERE u.userId = \"" + username + "\";");
			if (resultSet == null) {
				// The case if the username was not found in the database.
				return ReturnCodes.RC_INVALID_CRED;
			}
			
			// Retrieves the user credentials from the database.
			while (resultSet.next()) {
				foundUser = resultSet.getString("userId");
				foundPass = resultSet.getString("password");
			}
			
			// Decrypts the password and compares it to the user's entered password.
			decryptPass = Encryption.decrypt(foundPass, foundUser);
			if (!decryptPass.equals(password)) {
				// The case where the user enters the incorrect password.
				return ReturnCodes.RC_INVALID_CRED;
			}
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLException e) {
			// the case where some other error occurred.
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
		}
	}
	
	/**
	 * Retrieves the user data from the data given the unique username.
	 * 
	 * @param	userKey the username. This is the primary key to access a user's profile.
	 * @return			a string array with 4 elements where element 0 is the name, 1 is the email, 2 is the company
	 * 					name, and 3 is the position.
	 */
	public String[] getUserAttributes(String userKey) {
		String[] userAttributes = new String[4];
		
		try {
			// Executes the SQL query to get the user attributes for the application.
			resultSet = statement.executeQuery("SELECT u.name, u.emailAddress, u.companyName, u.empPosition FROM User u" + 
												" WHERE u.userId = \"" + userKey + "\";");
			
			// Stores each attribute into the array.
			while (resultSet.next()) {
				userAttributes[0] = resultSet.getString("name");
				userAttributes[1] = resultSet.getString("emailAddress");
				userAttributes[2] = resultSet.getString("companyName");
				userAttributes[3] = resultSet.getString("empPosition");
			}
			
			return userAttributes;
		}
		catch (SQLException e) {
			// In case there was some kind of error accessing the database.
			e.printStackTrace();
		}
		return null;
	}
	
	public void getCheckBoxes(boolean poolSelected, boolean gymSelected, boolean spaSelected, boolean bOSelected) {
		
	}
	
	/**
	 * Edits a hotel reservation for the user.
	 * 
	 * @return RC_OK if changes to the reservation were saved successfully. Otherwise RC_MISC_ERR to indicate some
	 * 			other error occurred.
	 */
	public int editReservation() {
		// TODO: Write the code to get the user's reservations and make a change to one.
		return ReturnCodes.RC_MISC_ERR;
	}
	
	/**
	 * Executes the search query based on the user's input.
	 * 
	 * @param 	hotelName	the name of the hotel, this can be empty.
	 * @param 	amenities	the list of amenities.
	 * @param 	fromDate	start book date.
	 * @param 	toDate		end book date.
	 * @param	minPrice	minimum price to look for.
	 * @param 	maxPrice	maximum price to look for.
	 * @return				the ArrayList of results, or {@code null} if an error occurred while executing the query.
	 */
	public ArrayList<Hotel> search(String hotelName, boolean[] amenities, String fromDate, String toDate, double minPrice, double maxPrice) {
		try {
			ArrayList<Hotel> results = new ArrayList<>();
			StringBuilder query = new StringBuilder("SELECT h.name, h.amenities, h.location, h.address, h.numRoomsStandard, h.numRoomsQueen, h.numRoomsKing, " +
													"h.rmPriceStandard, h.rmPriceQueen, h.rmPriceKing, h.wkndDiff FROM Hotel h WHERE ");
			
			// Build the query string from the parameters passed. First check for null values.
			
			// TODO: Once the hotelName text field is binded, append a statement that will pick up results of
			// hotels with that name.
			
			// Builds the amenity set.
			query.append("h.amenities = (\"");
			if (amenities[0]) {
				query.append("gym");
			}
			if (amenities[1]) {
				if (amenities[0]) query.append(",");
				query.append("spa");
			}
			if (amenities[2]) {
				if (amenities[1] || amenities[0]) query.append(",");
				query.append("pool");
			}
			if (amenities[3]) {
				if (amenities[2] || amenities[1] || amenities[0]) query.append(",");
				query.append("business office");
			}
			query.append("\")");
			
			// Restricts the price range, if there is one.
			if (minPrice > 0) {
				// Uses the standard room price to check minimum price since it is the cheapest option.
				query.append(" AND h.rmPriceStandard >= " + minPrice);
			}
			if (maxPrice > 0) {
				// Uses the king room price to check the maximum price since it is the most expensive option.
				query.append(" AND h.rmPriceKing <= " + maxPrice);
			}
			
			// TODO: Once dates are worked out, append a string that will search the dates.
			
			// Concludes the query string.
			query.append(";");
			resultSet = statement.executeQuery(query.toString());
			
			// Gets the data obtained and stores them into the resultSet.
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				String availableAmenities = resultSet.getString("amenities");
				String[] amenityList = availableAmenities.split(",");
				String location = resultSet.getString("location");
				String address = resultSet.getString("address");
				int roomsStandard = resultSet.getInt("numRoomsStandard");
				int roomsQueen = resultSet.getInt("numRoomsQueen");
				int roomsKing = resultSet.getInt("numRoomsKing");
				double priceStandard = resultSet.getDouble("rmPriceStandard");
				double priceQueen = resultSet.getDouble("rmPriceQueen");
				double priceKing = resultSet.getDouble("rmPriceKing");
				float weekendDiff = resultSet.getFloat("wkndDiff");
				
				// Adds the new hotel to the result.
				results.add(new Hotel(name, location, address, amenityList, roomsStandard, roomsQueen, roomsKing, priceStandard, priceQueen, priceKing, weekendDiff));
			}
			return results;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Gets the ID of the hotel based on the name of the hotel. Be careful that even though this function returns an
	 * integer, it does NOT represent any of the return codes.
	 * 
	 * @param hotelName	the name of the hotel.
	 * @return			the ID of the hotel if found, {@code 0} otherwise since none of the hotels have IDs of 0.
	 */
	public int getHotelId(String hotelName) {
		int hotelId = 0;
		
		try {
			// Executes the query to get the hotel by name assuming there is only one hotel with that name.
			resultSet = statement.executeQuery("SELECT h.hotelId FROM Hotel h WHERE h.name = \"" + hotelName + "\";");

			// Gets the ID as an integer from the returned ResultSet. The value returned from getInt is 0 if that
			// value is NULL or the hotel with the passed name is not found.
			while (resultSet.next()) {
				hotelId = resultSet.getInt("hotelId");
			}
			
			return hotelId;
		}
		catch (SQLException e) {
			// This clause executes if there was an error processing the SQL query.
			e.printStackTrace();
			return 0;
		}
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
