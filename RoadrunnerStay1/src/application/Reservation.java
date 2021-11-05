package application;

/**
 * Package: application
 * The class to represent a hotel reservation booked by the end user.
 * 
 * @author Pablo Cruz
 */
public class Reservation {
	private String userId;
	private int hotelId;
	private String startDate;
	private String endDate;
	private double totalCost;
	
	/**
	 * Constructor: Creates and initializes a new Reservation object.
	 * 
	 * @param u	the username.
	 * @param h	the hotel ID.
	 * @param s	the start date.
	 * @param e	the end date.
	 * @param t	the total cost.
	 */
	public Reservation(String u, int h, String s, String e, double t) {
		this.userId = u;
		this.hotelId = h;
		this.startDate = s;
		this.endDate = e;
		this.totalCost = t;
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
	 * Gets the hotel ID.
	 * 
	 * @return the hotel ID.
	 */
	public int getHotelId() {
		return hotelId;
	}
	
	/**
	 * Gets the start date.
	 * 
	 * @return the start date.
	 */
	public String getStartDate() {
		return startDate;
	}
	
	/**
	 * Gets the end date.
	 * 
	 * @return the end date.
	 */
	public String getEndDate() {
		return endDate;
	}
	
	/**
	 * Gets the total cost.
	 * 
	 * @return the total cost.
	 */
	public double getTotalCost() {
		return totalCost;
	}
}
