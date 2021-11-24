package application;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
	public long getDateDifference() {
		try {
			SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-mm-dd");
			Date start = sqlFormat.parse(startDate);
			Date end = sqlFormat.parse(endDate);
			long diff = end.getTime() - start.getTime();
			
			return TimeUnit.MILLISECONDS.toDays(diff);
		}
		catch(ParseException e) {
			return 0;
		}
	}
	
	/**
	 * Calculates and returns and updates {@code this.totalCost} the total cost for the reservation.
	 * 
	 * @param costOfRoom	the cost of the booked room.
	 * @param weekendDiff	the hotel weekend differential.
	 * @return				the total cost for the reservation.
	 */
	public double calculateCost(double costOfRoom, float weekendDiff) {
		//SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-mm-dd");
		//Date start = sqlFormat.parse(startDate);
		//Date end = sqlFormat.parse(endDate);
		String[] startDateArr = startDate.split("-");
		String[] endDateArr = endDate.split("-");
		LocalDate startLocal = LocalDate.of(Integer.parseInt(startDateArr[0]),
				Integer.parseInt(startDateArr[1]), Integer.parseInt(startDateArr[2]));
		LocalDate endLocal = LocalDate.of(Integer.parseInt(endDateArr[0]),
				Integer.parseInt(endDateArr[1]), Integer.parseInt(endDateArr[2]));
		long numDays = getDateDifference();
		double cost = 0.0;
		
		for (LocalDate date = startLocal; date.isEqual(endLocal); date = date.plusDays(1)) {
			if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				cost += costOfRoom * weekendDiff;
			}
		}
		totalCost = cost;
		
		return cost;
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