package application;

/**
 * Package: application
 * This class represents the User object. There should only be one user per session as this class represents the
 * user who is currently logged in. Once the login is successful, a new User object is initiated, and methods defined
 * here can be invoked.
 * 
 * @author Pablo Cruz
 */
public class User {
	private String userId;
	private String password;
	private String name;
	private String email;
	private String companyName;
	private String empPosition;
	
	/**
	 * Constructor: Initializes an employee user.
	 * 
	 * @param u	the username of the employee.
	 * @param n	the name of the employee.
	 * @param e	the email of the employee.
	 * @param c the user's company name.
	 * @param p	the user's position.
	 * @param pass	the user's password.
	 */
	public User(String u, String n, String e, String c, String p) {
		this.userId = u;
		this.name = n;
		this.email = e;
		this.companyName = c;
		this.empPosition = p;
	}
	
	/**
	 * Constructor: Initializes a customer user;
	 * @param u	the username of the customer.
	 * @param n the name of the customer.
	 * @param e	the email of the customer.
	 */
	public User(String u, String n, String e) {
		this.userId = u;
		this.name = n;
		this.email = e;
		this.companyName = "";
		this.empPosition = "";
	}
	
	/**
	 * Determines if the logged in user is an employee. If not, then they are a customer.
	 * 
	 * @return {@code true} if both company and position fields are not empty, {@code false} otherwise. 
	 */
	public boolean isEmployee() {
		//return !companyName.equals("") || !empPosition.equals("");
		if(!companyName.isEmpty() || !empPosition.isEmpty()) {
			return true;
		}
		return false;
		//return !companyName.isEmpty() || !empPosition.isEmpty();
		
	}
	
	/**
	 * Gets the username.
	 * 
	 * @return the username.
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Updates the username.
	 * 
	 * @param uid the new username.
	 */
	public void setUserId(String uid) {
		userId = uid;
	}
	
	/**
	 * Returns the decrypted user password.
	 * 
	 * @return user-entered password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Updates the user's password.
	 * 
	 * @param pass
	 */
	public void setPassword(String pass) {
		password = pass;
	}
	
	/**
	 * Gets the user's name.
	 * 
	 * @return the user's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Updates the user's name.
	 * 
	 * @param n the new name of the user.
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Gets the user's email.
	 * 
	 * @return the user's email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Updates the user's email.
	 * 
	 * @param e
	 */
	public void setEmail(String e) {
		email = e;
	}
	
	/**
	 * Gets the user's employer name, if the user is an employee.
	 * 
	 * @return the company name.
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * Updates the user's employer.
	 * 
	 * @param c the new company name.
	 */
	public void setCompanyName(String c) {
		companyName = c;
	}
	
	/**
	 * Gets the position of the user, if they are an employee.
	 * 
	 * @return the user's position.
	 */
	public String getPosition() {
		return empPosition;
	}
	
	/**
	 * Updates the user's employee position if they have one.
	 * 
	 * @param p the new position.
	 */
	public void setPosition(String p) {
		empPosition = p;
	}
}
