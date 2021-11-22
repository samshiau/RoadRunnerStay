package application;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
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
	private String hotelName;
	private String roomType;
	private int numRooms;
	
	/**
	 * Constructor: Creates and initializes a new Reservation object.
	 * 
	 * @param u	the username.
	 * @param h	the hotel ID.
	 * @param s	the start date.
	 * @param e	the end date.
	 * @param t	the total cost.
	 * @param r the room type.
	 * @param n the number of rooms to book.
	 */
	public Reservation(String u, int h, String s, String e, double t, String r, int n) {
		this.userId = u;
		this.hotelId = h;
		this.startDate = s;
		this.endDate = e;
		this.totalCost = t;
		this.roomType = r;
		this.numRooms = n;
	}
	
	@Override
	public String toString() {
	    return ("Hotel: "+this.getHotelName()+
	    		"\nStart date is: "+ this.getStartDate() +
	    		"\nEnd date is: "+ this.getEndDate() +
	    		String.format("\nTotal Cost is: $%.2f", this.getTotalCost()) + 
	    		"\nRoom type is: " + this.getRoomType() + 
	    		"\nNumber of rooms is: " + this.getNumRooms());
	}
	
	/**
	 * Calculates the number of days in between the start and end dates.
	 * 
	 * @return the number of days in between the start date and the end date.
	 */
	public int getDateDifference() {
		try {
			SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-mm-dd");
			Date startDate = sqlDateFormat.parse(this.startDate);
			Date endDate = sqlDateFormat.parse(this.endDate);
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			startCal.setTime(startDate);
			endCal.setTime(endDate);
			int startDay = startCal.get(Calendar.DATE), endDay = endCal.get(Calendar.DATE);
			int startMonth = startCal.get(Calendar.MONTH), endMonth = endCal.get(Calendar.MONTH);
			int startYear = startCal.get(Calendar.YEAR), endYear = endCal.get(Calendar.YEAR);
			int difference = 0;
			
			// Gets the differences
			if (endYear - startYear != 0) {
				difference += (endYear - startYear) * 365;
			}
			if (endMonth - startMonth != 0) {
				difference += (endMonth - startMonth) * 30;
			}
			difference += endDay + startDay;
			
			return difference;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
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
	 * Gets the hotel name.
	 * 
	 * @return the hotel name.
	 */
	public String getHotelName() {
		HotelDBManager connection = new HotelDBManager();
		hotelName = connection.getHotelName(hotelId);
		return hotelName;
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
	
	/**
	 * Gets the type of room.
	 * 
	 * @return room type.
	 */
	public String getRoomType() {
		return roomType;
	}
	
	/**
	 * Gets the number of rooms.
	 * 
	 * @return the number of rooms.
	 */
	public int getNumRooms() {
		return numRooms;
	}
}