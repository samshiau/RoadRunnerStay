package application;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Package: application
 * The class to represent a hotel object from the database.
 * 
 * @author Pablo Cruz
 */
public class Hotel {
	private String name;
	private String location;
	private String address;
	private String[] amenities;
	private InputStream imageStream;
	private int numRoomsStandard;
	private int numRoomsQueen;
	private int numRoomsKing;
	private double priceStandard;
	private double priceQueen;
	private double priceKing;
	private float weekendDiff;
	
	/**
	 * Constructor: Creates a new hotel object with the following parameters:
	 * 
	 * @param n		name of hotel.
	 * @param l		location of hotel.
	 * @param a		address of hotel.
	 * @param amn	hotel amenities.
	 * @param nrs	number of standard rooms.
	 * @param nrq	number of queen rooms.
	 * @param nrk	number of king rooms.
	 * @param ps	price for standard room.
	 * @param pq	price for queen room.
	 * @param pk	price for king room.
	 * @param wd	weekend differential.
	 */
	public Hotel(String n, String l, String a, String[] amn, int nrs, int nrq, int nrk, double ps, double pq, double pk, float wd, InputStream i) {
		this.name = n;
		this.location = l;
		this.address = a;
		this.amenities = amn;
		this.numRoomsStandard = nrs;
		this.numRoomsQueen = nrq;
		this.numRoomsKing = nrk;
		this.priceStandard = ps;
		this.priceQueen = pq;
		this.priceKing = pk;
		this.weekendDiff = wd;
		this.imageStream = i;
	}
	
	/**
	 * Finds the hotel object within the passed array list to return the hotel object with the given
	 * name.
	 * 
	 * @param hotels	the hotel list, this should be the list of hotels returned from the results query.
	 * @param name		the hotel name to search for.
	 * @return			the found hotel object if found, or {@code null} if no such hotel exists.
	 */
	public static Hotel getHotelByName(ArrayList<Hotel> hotels, String name) {
		for (int i = 0; i < hotels.size(); i++) {
			if (hotels.get(i).getName().equals(name)) {
				return hotels.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Calculates the total number of rooms.
	 * 
	 * @return total number of rooms.
	 */
	public int getNumberOfRooms() {
		return numRoomsStandard + numRoomsQueen + numRoomsKing;
	}
	
	/**
	 * Gets the hotel name.
	 * 
	 * @return the name of the hotel.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the hotel location.
	 * 
	 * @return the location of the hotel.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Gets the hotel address.
	 * 
	 * @return the address of the hotel.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Gets the hotel amenities.
	 * 
	 * @return the available amenities of the hotel.
	 */
	public String[] getAmenities() {
		return amenities;
	}
	
	/**
	 * Gets the image object as an InputStream to set for the JavaFX image.
	 * 
	 * @return the InputStream object representing the image file.
	 */
	public InputStream getImageStream() {
		return imageStream;
	}
	
	/**
	 * Gets the number of standard rooms.
	 * 
	 * @return the number of available standard rooms.
	 */
	public int getNumRoomsStandard() {
		return numRoomsStandard;
	}
	
	/**
	 * Gets the number of queen rooms.
	 * 
	 * @return the number of available queen rooms.
	 */
	public int getNumRoomsQueen() {
		return numRoomsQueen;
	}
	
	/**
	 * Gets the number of king rooms.
	 * 
	 * @return the number of available king rooms.
	 */
	public int getNumRoomsKing() {
		return numRoomsKing;
	}
	
	/**
	 * Gets the price of a standard room.
	 * 
	 * @return the price for a standard room.
	 */
	public double getPriceStandard() {
		return priceStandard;
	}
	
	/**
	 * Gets the price of a queen room.
	 * 
	 * @return the price for a queen room.
	 */
	public double getPriceQueen() {
		return priceQueen;
	}
	
	/**
	 * Gets the price of a king room.
	 * 
	 * @return the price for a king room.
	 */
	public double getPriceKing() {
		return priceKing;
	}
	
	/**
	 * Gets the weekend differential.
	 * 
	 * @return the weekend differential for the hotel.
	 */
	public float getWeekendDifferential() {
		return weekendDiff;
	}
}
