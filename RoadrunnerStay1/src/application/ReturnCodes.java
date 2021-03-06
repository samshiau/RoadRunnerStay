package application;

/**
 * Package: application
 * This class defines the values of the return codes for each query executed within the program. The one and only
 * return code indicating success is RC_OK is 0, so 0 is the success return code. Therefore if any method returns
 * a non-zero return code, an error has occurred.
 * 
 * @author Pablo Cruz
 */
public class ReturnCodes {
	public static final int RC_OK = 0;
	public static final int RC_MISSING_ENTRY = 1;
	public static final int RC_INVALID_CRED = 2;
	public static final int RC_DUP_USER = 3;
	public static final int RC_EMAIL_SYN_WRONG = 4;
	public static final int RC_DATE_SYN_WRONG = 5;
	public static final int RC_NO_MORE_ROOMS = 6;
	public static final int RC_DUP_RESERVATION = 7;
	public static final int RC_TOO_MANY_ROOMS = 8;
	public static final int RC_MISC_ERR = 99;
	
	/**
	 * Returns the string representation of the return code integer passed to the parameter.
	 * 
	 * @param rc 	return code.
	 * @return		the string representation of the return code.
	 */
	public static String getRcAsString(int rc) {
		switch (rc) {
			case RC_OK:				// This case is to be used for testing purposes.
				return "Success.";
			case RC_MISSING_ENTRY:
				return "One or more required entries are missing.";
			case RC_INVALID_CRED:
				return "Incorrect username or password.";
			case RC_MISC_ERR:
				return "An unknown error occurred, please try again.";
			case RC_DUP_USER:
				return "Account associated with this username already exists.";
			case RC_EMAIL_SYN_WRONG:
				return "Email syntax is incorrect.";
			case RC_DATE_SYN_WRONG:
				return "The date format is incorrect.";
			case RC_NO_MORE_ROOMS:
				return "There are not enough rooms of this type for this reservation.";
			case RC_DUP_RESERVATION:
				return "You have already booked a reservation for this hotel.";
			case RC_TOO_MANY_ROOMS:
				return "Too many rooms are booked for this hotel.";
			default:
				return "Unknown return code: " + rc + ".";
		}
	}
}