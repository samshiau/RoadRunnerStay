package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditReservationsAsUserController implements Initializable{
	
	@FXML private Button bookIt;
	@FXML ImageView imageViewResults;
	@FXML private ListView<String> resultsList;
	MainController mainController = new MainController();
	String whichHotel;
	@FXML private ListView<String> reservationsList;
	
	
	@FXML private Button start, back;
	@FXML private TextField hotelNameInput, fromDateInput, toDateInput, minPriceInput, maxPriceInput;
	String hotelNameInputStr, fromDateInputStr, toDateInputStr, minPriceInputStr, maxPriceInputStr;
	@FXML ImageView imageView, imageView2, imageView3;
	@FXML CheckBox pool, gym, spa, businessOffice;
	
	
	boolean isTheUserLoggedIn;
	boolean poolSelected = false, gymSelected = false, spaSelected = false, businessOfficeSelected = false; 
	private static ArrayList<Hotel> results;
	private ArrayList<Image> images = new ArrayList<Image>();
	int count = 0, count2 = 1, count3 = 2;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Scanner inFile1 = null;
		String token1 = "";
		try {
			inFile1 = new Scanner(new File("resutls.txt")).useDelimiter(",\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> temps = new ArrayList<String>();
		while (inFile1.hasNext()) {
		      // find next line
		      token1 = inFile1.next();
		      temps.add(token1);
		    }
		    inFile1.close();

		    String[] tempsArray = temps.toArray(new String[0]);

		    for (String s : tempsArray) {
		    	reservationsList.getItems().add(s);
		    }
	}
	
	public void resultsListSelected() {
		whichHotel = resultsList.getSelectionModel().getSelectedItem();
	}

	public void userInput() {
		// check which and if checkboxes are selected
		if(pool.isSelected())
			poolSelected = true;
		if(gym.isSelected())
			gymSelected = true;
		if(spa.isSelected()) 
			spaSelected = true;
		if(businessOffice.isSelected()) 
			businessOfficeSelected = true;
		
		// get user input in TextFields
		hotelNameInputStr = hotelNameInput.getText();
		fromDateInputStr = fromDateInput.getText();
		toDateInputStr = toDateInput.getText();
		minPriceInputStr = minPriceInput.getText();
		maxPriceInputStr = maxPriceInput.getText();	
	}

	@FXML 
	public void changeScreenResult(ActionEvent event) throws IOException {
		userInput();
		
		// Gets the user-checked amenities.
		boolean[] amenityChecks = new boolean[4];
		amenityChecks[0] = gymSelected;
		amenityChecks[1] = spaSelected;
		amenityChecks[2] = poolSelected;
		amenityChecks[3] = businessOfficeSelected;
		
		// Parses doubles from the min/max price text fields. In case there is an error in the format or there is no
		// input for each of the prices, the minimum and maximum value of 0 is used and the search query will not use
		// them.
		double minPrice;
		double maxPrice;
		try {
			minPrice = Double.parseDouble(minPriceInputStr);
		}
		catch (NumberFormatException e) {
			minPrice = 0.0;
		}
		catch (NullPointerException e) {
			minPrice = 0.0;
		}
		
		try {
			maxPrice = Double.parseDouble(maxPriceInputStr);
		}
		catch (NumberFormatException e) {
			maxPrice = 0.0;
		}
		catch (NullPointerException e) {
			maxPrice = 0.0;
		}
		
		
		// Gets the results from the database.
		HotelDBManager searcher = new HotelDBManager();
		results = searcher.search(hotelNameInputStr, amenityChecks, fromDateInputStr, toDateInputStr, minPrice, maxPrice);
		//getResultsArray().addAll(results);
		searcher.closeManager();
		for (int i = 0; i < results.size()/2; i++) {
			whichHotel =   results.get(i).getName();
			resultsList.getItems().addAll(whichHotel);
		}   
//		resultsList.getItems().clear();
	}
	
	
	@FXML 
	public void bookItButton(ActionEvent event) throws IOException {
		//userInput();
		HotelDBManager test = new HotelDBManager();
		String userId = "user";
		int hotelId = 1;
		String roomType = "queen"; 
		String startDate = "2021-04-04"; 
		String endDate = "2021-04-05";
		int rc = test.bookReservation(userId, hotelId, roomType, startDate, endDate);
		if (rc != 0) {
			System.out.println("Booking failed.");
		}
		test.closeManager();
		resultsList.getItems().clear();
	}
	
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenProfile(event);
	}
	

}
