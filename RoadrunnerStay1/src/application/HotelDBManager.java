package application;

import java.io.IOException;
import java.io.InputStream;
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
		
		System.out.println(company);
		
		// Checks to make sure required entries are entered.
		if (username.equals("") || password.equals("") || name.equals("") || email.equals("")) {
			return ReturnCodes.RC_MISSING_ENTRY;
		}
		else if (!syntaxIsCorrect) {
			// Ensures the email syntax is correct.
			return ReturnCodes.RC_EMAIL_SYN_WRONG;
		}
		
		// Validates the entered name by checking to make sure only alphabetic and spaces are entered.
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isAlphabetic(name.charAt(i)) && !Character.isWhitespace(name.charAt(i))) {
				return ReturnCodes.RC_INVALID_CRED;
			}
		}
		
		// Trims leading and trailing whitespace from the username and names in place.
		username = username.trim(); name = name.trim();
		
		String encryptPass = Encryption.encrypt(password, username);	// Encrypt the password for the database.
		
		try {
			if (company == null || position.equals("")) {
				// Inserts a new customer with the passed credentials into the User table of the database.
				statement.executeUpdate("INSERT INTO `User` (`userId`, `password`, `name`, `emailAddress`)" +
													"VALUES (\"" + username + "\", \""
																+ encryptPass + "\", \""
																+ name + "\", \""
																+ email + "\");");
				System.out.println("Added customer");
			}
			else {
				// Adds a new employee given the credentials which includes the company and position.
				statement.executeUpdate("INSERT INTO `User` (`userId`, `password`, `userType`, `name`, `emailAddress`, " +
										"`companyName`, `empPosition`) " +
										"VALUES (\"" + username + "\", \""
													+ encryptPass + "\", \""
													+ "EMPLOYEE\", \""
													+ name + "\", \""
													+ email + "\", \""
													+ company + "\", \""
													+ position + "\");");
				
				System.out.println("Added employee");
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
			
			// Fills the user company name and position with empty strings if the returned value was null
			// when they were retrieved. 
			if (userAttributes[2] == null) {
				userAttributes[2] = "";
			}
			if (userAttributes[3] == null) {
				userAttributes[3] = "";
			}
			
			return userAttributes;
		}
		catch (SQLException e) {
			// In case there was some kind of error accessing the database.
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Edits the employee's hotel attributes and sends the modified data to the database. For the {@code int} and {@code double} arrays
	 * for the room types, index 0 is for a standard room, index 1 is for a queen room, and index 2 is for a king room.
	 * 
	 * @param name				the name of the hotel to update.
	 * @param amenities			the array for the hotel amenities.
	 * @param numRoomsPerType	the array for the number of rooms for the hotel.
	 * @param roomPricePerType	the array for the room prices.
	 * @param weekendDiff		the hotel's weekend differential.
	 * @return RC_OK 			if changes to the reservation were saved successfully. Otherwise RC_MISC_ERR to indicate if some
	 * 							other error occurred.
	 */
	public int editHotel(String name, boolean[] amenities, int[] numRoomsPerType, double[] roomPricePerType, float weekendDiff) {
		String amenityStr = "h.amenities = (\"";
		try {
			// TODO: Write the code to update the hotel attributes based on the passed parameters.
			preparedStatement = connect.prepareStatement("UPDATE Hotel h SET h.amenities = ?, h.numRoomsStandard = ?, h.numRoomsQueen = ?, h.numRoomsKing = ?, " +
															"h.rmPriceStandard = ?, h.rmPriceQueen = ?, h.rmPriceKing = ?, h.wkndDiff = ? WHERE h.name = ?;");
			preparedStatement.setString(9, name);
			
			// TODO: Set the rest of the parameters for the prepared statement where index range is from 1 to 8 inclusively. Index 9 is already handled.
			
			// Builds the string to set the amenity set.
			if (amenities[0]) {
				// Appends "gym".
				amenityStr += "gym";
			}
			if (amenities[1]) {
				// Appends "spa".
				if (amenities[0]) amenityStr += ",";
				amenityStr += "spa";
			}
			if (amenities[2]) {
				// Appends "pool".
				if (amenities[1] || amenities[0]) amenityStr += ",";
				amenityStr += "pool";
			}
			if (amenities[3]) {
				// Appends "business office".
				if (amenities[2] || amenities[1] || amenities[0]) amenityStr += ",";
				amenityStr += "business office";
			}
			amenityStr += "\")";
			preparedStatement.setString(1, amenityStr);
			
			// Sets the number of rooms for each type.
			preparedStatement.setInt(2, numRoomsPerType[0]);
			preparedStatement.setInt(3, numRoomsPerType[1]);
			preparedStatement.setInt(4, numRoomsPerType[2]);
			
			// Sets the room prices.
			preparedStatement.setDouble(5, roomPricePerType[0]);
			preparedStatement.setDouble(6, roomPricePerType[1]);
			preparedStatement.setDouble(7, roomPricePerType[2]);
			
			// Sets the weekend differential.
			preparedStatement.setFloat(8, weekendDiff);
			
			// Executes the update.
			preparedStatement.executeUpdate();
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
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
													"h.rmPriceStandard, h.rmPriceQueen, h.rmPriceKing, h.wkndDiff, h.image FROM Hotel h WHERE ");
			boolean amenityIsSelected = amenities[0] || amenities[1] || amenities[2] || amenities[3];
			boolean priceIsEntered = (minPrice > 0 || maxPrice > 0);
			
			// Check for invalid character input such as a semicolon in the hotel name to prevent an SQL injection attack.
			if (hotelName.indexOf(';') != -1) {
				throw new SQLException("Invalid character entry in hotel name.");
			}
			
			// FIXME: Use the next highest room price if king or queen sizes are unavailable.
			
			// Build the query string from the parameters passed. First check for null values and do not search for them.
			
			// Checks for an entry to search for the hotel by name.
			if (!hotelName.equals("")) {
				query.append("h.name = \"" + hotelName + "\" ");
			}
			
			// Builds the amenity set if any of them are checked, otherwise amenities are not searched for in the query.
			if (amenityIsSelected) {
				if (!hotelName.equals("")) query.append("AND ");
				
				query.append("h.amenities = (\"");
				if (amenities[0]) {
					// Appends "gym".
					query.append("gym");
				}
				if (amenities[1]) {
					// Appends "spa".
					if (amenities[0]) query.append(",");
					query.append("spa");
				}
				if (amenities[2]) {
					// Appends "pool".
					if (amenities[1] || amenities[0]) query.append(",");
					query.append("pool");
				}
				if (amenities[3]) {
					// Appends "business office".
					if (amenities[2] || amenities[1] || amenities[0]) query.append(",");
					query.append("business office");
				}
				query.append("\") ");
			}
			
			// Checks if any of the previous entries have obtained user input to place a logical AND for searching
			// the hotels.
			if ((!hotelName.equals("") || amenityIsSelected) && priceIsEntered) query.append("AND ");
			
			// Restricts the price range, if there is one.
			if (minPrice > 0) {
				// Uses the standard room price to check minimum price since it is the cheapest option.
				query.append("h.rmPriceStandard >= " + minPrice);
			}
			if (maxPrice > 0) {
				// Uses the king room price to check the maximum price since it is the most expensive option.
				if (minPrice > 0) query.append("AND ");
				query.append("h.rmPriceKing <= " + maxPrice);
			}
			
			// TODO: Once dates are worked out, append a string that will search the dates.
			
			// Concludes the query string.
			query.append(";");
			resultSet = statement.executeQuery(query.toString());
			
			// Gets the data obtained and stores them into the resultSet.
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				String availableAmenities = resultSet.getString("amenities");
				String[] amenityList;
				if (availableAmenities != null) {
					amenityList = availableAmenities.split(",");
				}
				else {
					amenityList = new String[1];
				}
				String location = resultSet.getString("location");
				String address = resultSet.getString("address");
				Blob imageBlob = resultSet.getBlob("image");
				int roomsStandard = resultSet.getInt("numRoomsStandard");
				int roomsQueen = resultSet.getInt("numRoomsQueen");
				int roomsKing = resultSet.getInt("numRoomsKing");
				double priceStandard = resultSet.getDouble("rmPriceStandard");
				double priceQueen = resultSet.getDouble("rmPriceQueen");
				double priceKing = resultSet.getDouble("rmPriceKing");
				float weekendDiff = resultSet.getFloat("wkndDiff");
				
				try {
					// Converts the image blob into an InputStream so JavaFX can be able to load it.
					InputStream image = imageBlob.getBinaryStream();
					
					// Adds the new hotel to the result.
					results.add(new Hotel(name, location, address, amenityList,
											roomsStandard, roomsQueen, roomsKing,
											priceStandard, priceQueen, priceKing,
											weekendDiff, image));
					image.close();
				}
				catch (NullPointerException e) {
					// Adds a hotel to the results if it does not contain an image.
					results.add(new Hotel(name, location, address, amenityList,
											roomsStandard, roomsQueen, roomsKing,
											priceStandard, priceQueen, priceKing,
											weekendDiff, null));
				}
			}
			return results;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Books a new hotel reservation for the user with userId for hotel hotelId. Not only is the reservation added,
	 * but the hotel number of rooms of the room type is also modified to protect the integrity of the database.
	 * 
	 * @param userId	the username.
	 * @param hotelId	the hotel identification number.
	 * @param roomType	the room type the user booked.
	 * @param startDate	the start date of the reservation.
	 * @param endDate	the end date of the user.
	 * @param numRooms	the number of rooms the user booked.
	 * @return			{@code RC_OK} if the reservation is successful, {@code RC_DATE_SYN_WRONG} if the entered date
	 * 					is not in a correct format, or {@code RC_MISC_ERR} if some other error occurred.
	 */
	public int bookReservation(String userId, int hotelId, String roomType, String startDate, String endDate, int numRooms) {
		Pattern dateSyntax = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Matcher startDateCorrect = dateSyntax.matcher(startDate), endDateCorrect = dateSyntax.matcher(endDate);
		Date startSqlDate, endSqlDate;
		StringBuilder query = new StringBuilder("SELECT ");
		int rmPriceCol = 8;
		double totalCost = 0.0;
		
		// Checks for date syntax (valid syntax: MM/DD/YYYY) before proceeding with the booking.
		if (!startDateCorrect.find() || !endDateCorrect.find()) {
			System.out.println("Date syntax incorrect");
			return ReturnCodes.RC_DATE_SYN_WRONG;
		}
		
		// Converts the date format of the hotel into the format SQL can store it as.
		startSqlDate = Date.valueOf(startDate);
		endSqlDate = Date.valueOf(endDate);
		
		// Attempts to insert the book into the reservation table.
		try {
			preparedStatement = connect.prepareStatement("INSERT INTO `Reservation` (`userId`, `hotelId`, `startDate`, `endDate`, "
														+ "`totalCost`, 'roomType`, `numRooms`) "
														+ "VALUES (?, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, userId);
			preparedStatement.setInt(2, hotelId);
			preparedStatement.setDate(3, startSqlDate);
			preparedStatement.setDate(4, endSqlDate);
			preparedStatement.setString(5, roomType);
			preparedStatement.setInt(6, numRooms);
			
			// Calculates the total cost of the room.
			int numDays = endSqlDate.compareTo(startSqlDate);
			
			// Appends the room type attribute based on the user's room type selection.
			switch (roomType) {
				case "standard":
					// Gets the price of a standard room for one night. 
					query.append("h.rmPriceStandard, h.numRoomsStandard, ");
					//statement.executeUpdate("UPDATE Hotel h SET h.numRoomsStandard = h.numRoomsStandard - " + numRooms +
					//						" WHERE h.hotelId = " + hotelId + ";");
					break;
					
				case "queen":
					// Gets the price of a queen room for one night. 
					query.append("h.rmPriceQueen, h.numRoomsQueen, ");
					rmPriceCol += 1;
					//statement.executeUpdate("UPDATE Hotel h SET h.numRoomsQueen = h.numRoomsQueen - " + numRooms + 
					//						" WHERE h.hotelId = " + hotelId + ";");
					break;
					
				default:
					// Gets the price of a king room for one night. 
					query.append("h.rmPriceKing, h.numRoomsKing, ");
					rmPriceCol += 2;
					//statement.executeUpdate("UPDATE Hotel h SET h.numRoomsKing = h.numRoomsKing - " + numRooms +
					//						" WHERE h.hotelId = " + hotelId + ";");
					break;
			}
			
			// Obtains the data of the hotel to book from the database.
			query.append("h.wkndDiff FROM Hotel h WHERE h.hotelId = \"" + hotelId + "\";");
			
			System.out.println(query.toString());
			resultSet = statement.executeQuery(query.toString());
			
			while (resultSet.next()) {
				System.out.println("Column index = " + rmPriceCol);
				double costPerRoom = resultSet.getDouble(1);
				int numRoomsAvailable = resultSet.getInt(2);
				float weekendDiff = resultSet.getFloat("wkndDiff");
				
				// Ensures there are enough rooms of the selected type available.
				if (numRoomsAvailable - numRooms < 0) {
					return ReturnCodes.RC_NO_MORE_ROOMS;
				}
				totalCost = numDays * costPerRoom * (1 * weekendDiff);
			}
			
			// Updates the hotel rooms based on the user's selection.
			switch (roomType) {
				case "standard":
					statement.executeUpdate("UPDATE Hotel h SET h.numRoomsStandard = h.numRoomsStandard - " + numRooms +
											" WHERE h.hotelId = " + hotelId + ";");
					break;
					
				case "queen":
					statement.executeUpdate("UPDATE Hotel h SET h.numRoomsQueen = h.numRoomsQueen - " + numRooms + 
											" WHERE h.hotelId = " + hotelId + ";");
					break;
					
				default:
					statement.executeUpdate("UPDATE Hotel h SET h.numRoomsKing = h.numRoomsKing - " + numRooms +
											" WHERE h.hotelId = " + hotelId + ";");
					break;
			}
			
			// Sets the total cost and then adds the reservation to the database.
			preparedStatement.setDouble(5, totalCost);
			// FIXME: Delete the below line once debugged.
			System.out.println(preparedStatement.toString());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			System.out.println("Book successful!");
			
			return ReturnCodes.RC_OK;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
		}
	}
	
	/**
	 * Loads all the reservations of the user passed to the parameter and stores them into an ArrayList of type
	 * Reservation.
	 * 
	 * @param username 	the user name.
	 * @return			the ArrayList of booked reservations by the user.
	 */
	public ArrayList<Reservation> getReservationsByUser(String username) {
		ArrayList<Reservation> results = new ArrayList<>();
		
		try {
			preparedStatement = connect.prepareStatement("SELECT r.hotelId, r.startDate, r.endDate, r.totalCost, " + 
															"r.roomType, r.numRooms " +
															"FROM Reservation r WHERE r.userId = ?;");
			
			// Sets the username to search for.
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				int hotelId = resultSet.getInt("hotelId");
				String startDate = resultSet.getDate("startDate").toString();
				String endDate = resultSet.getDate("endDate").toString();
				double totalCost = resultSet.getDouble("totalCost");
				String roomType = resultSet.getString("roomType");
				int numRooms = resultSet.getInt("numRooms");
				
				results.add(new Reservation(username, hotelId, startDate, endDate, totalCost, roomType, numRooms));
			}
			preparedStatement.close();
			
			return results;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Modifies a booked reservation in the database.
	 * 
	 * @param userId		the username.
	 * @param hotelName		the hotel ID.
	 * @param roomType		the type of room to book.
	 * @param newNumRooms	the number of rooms for the reservation.
	 * @param startDate		the start date.
	 * @param endDate		the end date.
	 * @return				{@code RC_OK} if the reservation was successfully changed, or {@code RC_DATE_SYN_WRONG} if the user entered
	 * 						an invalid date, otherwise {@code RC_MISC_ERR} if some other error occurred.
	 */
	public int editReservation(String userId, String hotelName, String roomType, int newNumRooms, String startDate, String endDate) {
		Pattern dateSyntax = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Matcher startDateCorrect = dateSyntax.matcher(startDate), endDateCorrect = dateSyntax.matcher(endDate);
		Date startSqlDate, endSqlDate;
		String getHotelQuery = "SELECT h.hotelId, ";
		String pNumRoomsQuery = "SELECT r.numRooms FROM Reservation r where r.userId = \"" + userId + "\" AND r.hotelId = ";
		double totalCost;
		float roomCost = (float) 0.0;
		int hotelId = 0;
		int pNumRooms = 0;
		
		// Checks the input date syntax.
		if (!startDateCorrect.find() || !endDateCorrect.find()) {
			return ReturnCodes.RC_DATE_SYN_WRONG;
		}
		
		// Gets the SQL date values of the start and end dates as strings.
		startSqlDate = Date.valueOf(startDate);
		endSqlDate = Date.valueOf(endDate);
		
		try {
			preparedStatement = connect.prepareStatement("UPDATE Reservation r SET r.totalCost = ?, r.roomType = ?, r.numRooms = ?" +
															"WHERE r.hotelId = ? AND r.userId = ?;");
			
			hotelId = getHotelId(hotelName);
			pNumRoomsQuery += hotelId + ";";
			
			// Gets the hotel attributes.
			switch (roomType) {
				case "standard":
					getHotelQuery += "h.rmPriceStandard ";
					break;
				case "queen":
					getHotelQuery += "h.rmPriceQueen ";
					break;
				case "king":
					getHotelQuery += "h.rmPriceKing ";
					break;
			}
			
			// Get the previous number of rooms from the user's current reservation.
			resultSet = statement.executeQuery(pNumRoomsQuery);
			while (resultSet.next()) {
				pNumRooms = resultSet.getInt("numRooms");
			}
			
			// Gets the hotel ID and cost for the room type based on the above switch-case to change the number of rooms.
			getHotelQuery += "FROM Hotel h WHERE h.hotelId = " + hotelId + ";";
			resultSet = statement.executeQuery(getHotelQuery);
			while (resultSet.next()) {
				hotelId = resultSet.getInt("hotelId");
				roomCost = resultSet.getFloat(2);
			}
			
			// Calculates the new total cost after the edit.
			totalCost = Math.abs(pNumRooms - newNumRooms) * roomCost * endSqlDate.compareTo(startSqlDate);
			
			// Sets the values for each parameter in the prepared statement.
			preparedStatement.setDouble(1, totalCost);
			preparedStatement.setString(2, roomType);
			preparedStatement.setInt(3, newNumRooms);
			preparedStatement.setInt(4, hotelId);
			preparedStatement.setString(5, userId);
			
			// Execute the statement.
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
		}
	}
	
	/**
	 * Removes a Reservation record with the matching username and hotel name from the database.
	 * 
	 * @param username	the user name to search for.
	 * @param hotelName	the hotel name to search for.
	 * @return			{@code RC_OK} if the deletion was successful, otherwise returns {@code RC_MISC_ERR}.
	 */
	public int cancelReservation(String username, String hotelName) {
		try {
			String updateRoomsStatement = "UPDATE Hotel h SET h.";
			String userRoomsQuery = "SELECT r.roomType, r.numRooms FROM Reservation r WHERE r.hotelId = ";
			String roomType = "";
			int numRooms = 0;
			
			preparedStatement = connect.prepareStatement("DELETE FROM Reservation WHERE " +
															"userId = ? AND hotelId = ?;");
			
			// gets the ID of the hotel to be able to access it in the Reservation table.
			int hotelId = getHotelId(hotelName);
			
			// Adds the rest of the query with the user's username.
			userRoomsQuery += hotelId + " AND r.userId = \"" + username + "\";";
			
			// Gets the data from the user reservation query.
			resultSet = statement.executeQuery(userRoomsQuery);
			while (resultSet.next()) {
				roomType = resultSet.getString("roomType");
				numRooms = resultSet.getInt("numRooms");
			}
			
			// Adds the number of rooms of the particular room from the reservation back to the Hotel table.
			switch (roomType) {
				case "standard":
					updateRoomsStatement += "numRoomsStandard = h.numRoomsStandard + ";
					break;
				case "queen":
					updateRoomsStatement += "numRoomsQueen = h.numRoomsQueen + ";
					break;
				case "king":
					updateRoomsStatement += "numRoomsKing = h.numRoomsKing + ";
					break;
			}
			updateRoomsStatement += numRooms + ";";
			statement.executeUpdate(updateRoomsStatement);
			
			// Set the values of the parameters of the prepared statement.
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, hotelId);
			
			// Executes the deletion.
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
		}
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
	 * Gets and returns the hotel name given the hotel's ID.
	 * 
	 * @param hotelId 	the key ID.
	 * @return			the name of the hotel with the corresponding ID.
	 */
	public String getHotelName(int hotelId) {
		String hotelName = "";
		
		try {
			resultSet = statement.executeQuery("SELECT h.name FROM Hotel h WHERE h.hotelId = " + hotelId + ";");
			
			while (resultSet.next()) {
				hotelName = resultSet.getString("name");
			}
			
			return hotelName;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Returns a hotel object given the hotel name, which is the company name for the employee.
	 * 
	 * @param empCompany
	 * @return
	 */
	public Hotel getHotelByEmpCompany(String empCompany) {
		return null;
	}
	
	/**
	 * Sends the user updated attributes to the database.
	 * @param user	the user to update
	 * @return		{@code RC_OK} if the update is successful, otherwise RC_MISC_ERR if some other error
	 * 				occurs.
	 */
	public int updateUser(User user) {
		Pattern emailSyntax = Pattern.compile("[A-Za-z0-9]@[A-Za-z]+\\.[a-z]");
		Matcher matcher = emailSyntax.matcher(user.getEmail());
		String encPass;
		String userType;
		
		try {
			preparedStatement = connect.prepareStatement("UPDATE User u SET " +
															"u.password = ?, " +
															"u.name = ?, " +
															"u.userType = ?, " +
															"u.emailAddress = ?, " +
															"u.companyName = ?, " +
															"u.empPosition = ? " +
															"WHERE u.userId = ?;");
			
			// Validates all user input that needs to be validated.
			
			// Checks email syntax.
			if (!matcher.find()) {
				return ReturnCodes.RC_EMAIL_SYN_WRONG;
			}
			
			// Checks name syntax.
			for (int i = 0; i < user.getName().length(); i++) {
				if (!Character.isAlphabetic(user.getName().charAt(i)) && !Character.isWhitespace(user.getName().charAt(i))) {
					return ReturnCodes.RC_INVALID_CRED;
				}
			}
			
			// Encrypts the password before it can be sent to the database. 
			encPass = Encryption.encrypt(user.getPassword(), user.getUserId());
			
			// Determines if the user is a customer or an employee.
			if (user.getCompanyName().equals("") || user.getPosition().equals("")) {
				userType = "CUSTOMER";
			}
			else {
				userType = "EMPLOYEE";
			}
			
			// Fills in the data to update.
			preparedStatement.setString(1, encPass);
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, userType);
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.setString(5, user.getCompanyName());
			preparedStatement.setString(6, user.getPosition());
			preparedStatement.setString(7, user.getUserId());
			
			// Executes the update.
			preparedStatement.executeUpdate();
			
			return ReturnCodes.RC_OK;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return ReturnCodes.RC_MISC_ERR;
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
