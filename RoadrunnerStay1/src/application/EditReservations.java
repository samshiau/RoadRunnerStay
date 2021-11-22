package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class EditReservations implements Initializable{
	@FXML private TextField numRoomsStandard, numRoomsQueen, numRoomsKing;
	@FXML private TextField pricePerNightStandard, pricePerNightQueen, pricePerNightKing;
	
	@FXML private Label nameOfHotel;
	

	@FXML private ListView<String> resultList;
	@FXML CheckBox pool, gym, spa, businessOffice;
	@FXML RadioButton radio15; 
	@FXML RadioButton radio20; 
	@FXML RadioButton radio25;
	@FXML RadioButton radio35;
	
	ToggleGroup radioGroup = new ToggleGroup();	
	SwitchScenesController changeScene = new SwitchScenesController(); 
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	
	public static ArrayList<Hotel> results;
	
	String percent; 
	String numberOfRoomsStandard, numberOfRoomsQueen, numberOfRoomsKing;
	String roomPricePerNightStandard, roomPricePerNightQueen, roomPricePerNightKing;
	boolean poolSelected = false, gymSelected = false, spaSelected = false, businessOfficeSelected = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		// show name of hotel
		nameOfHotel.setText(user.getCompanyName());
		
		// setup radio functionality
		radioButtonSetup();
		
		// setup arraylist to show user's current hotel info
		getHotelInfo();
	}
	// used for the radio button toggles	
	public void radioButtonSetup() {		
		radio15.setToggleGroup(radioGroup);
		radio20.setToggleGroup(radioGroup);
		radio25.setToggleGroup(radioGroup);
		radio35.setToggleGroup(radioGroup);

		// set "15%" as default
		radio15.setSelected(true);
	}
		
	// fills the arraylist with hotel info of the current user
	public void getHotelInfo() {
		boolean[] amenityChecks = new boolean[4]; 
		amenityChecks[0] = false;
		amenityChecks[1] = false;
		amenityChecks[2] = false;
		amenityChecks[3] = false;
 
		HotelDBManager searcher = new HotelDBManager();
		results = searcher.search(user.getCompanyName(), amenityChecks, "2021-04-04", "2021-04-15", 1, 9000);
		for(int i =0; i < results.size(); i++) {
			String [] amenitiesList = results.get(i).getAmenities();
			
			String hotelResults = results.get(i).getName() +
					"\nNumber of Standard Rooms: " + results.get(i).getNumRoomsStandard() +
					"\tPrice: $" + results.get(i).getPriceStandard() +
					"\nNumber of Queen Rooms: " + results.get(i).getNumRoomsQueen() +
					"\t\tPrice: $" + results.get(i).getPriceQueen() +
					"\nNumber of King Rooms: " + results.get(i).getNumRoomsKing() +
					"\t\tPrice: $" + results.get(i).getPriceKing() +
					"\nAmenities: " + Arrays.toString(amenitiesList);
			// load info to arraylist to display 
			resultList.getItems().addAll(hotelResults);
		}
		searcher.closeManager();
	}
	
	// get user input
	private void getUserInput() {
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
		numberOfRoomsStandard = numRoomsStandard.getText();
		numberOfRoomsQueen = numRoomsQueen.getText();
		numberOfRoomsKing = numRoomsKing.getText();
		roomPricePerNightStandard = pricePerNightStandard.getText();
		roomPricePerNightQueen = pricePerNightQueen.getText();
		roomPricePerNightKing = pricePerNightKing.getText();
		
		// get user input from radio buttons
		RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
		percent = selectedRadioButton.getText().toLowerCase();
	}

	@FXML 
	public void saveChanges(ActionEvent event) throws IOException {
		getUserInput();
		// if textfield is empty show error message
		if(		numberOfRoomsStandard.isEmpty() ||
				numberOfRoomsQueen.isEmpty() ||
				numberOfRoomsKing.isEmpty() ||
				roomPricePerNightStandard.isEmpty() ||
				roomPricePerNightQueen.isEmpty() ||
				roomPricePerNightKing.isEmpty()){
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Enter all information");
			noInput.setHeaderText("Please enter all the required information");			
			noInput.showAndWait();
		} else {
		
			// gets the user-checked amenities
			boolean[] amenityChecks = new boolean[4];
			amenityChecks[0] = gymSelected;
			amenityChecks[1] = spaSelected;
			amenityChecks[2] = poolSelected;
			amenityChecks[3] = businessOfficeSelected;
		
			// gets the user room numbers
			int[] numRoomsPerType = new int[3];
			numRoomsPerType[0] = Integer.parseInt(numberOfRoomsStandard);
			numRoomsPerType[1] = Integer.parseInt(numberOfRoomsQueen);
			numRoomsPerType[2] = Integer.parseInt(numberOfRoomsKing);
		
			// gets the price per night
			double[] roomPricePerType = new double[3];
			roomPricePerType[0] = Double.parseDouble(roomPricePerNightStandard);
			roomPricePerType[1] = Double.parseDouble(roomPricePerNightQueen);
			roomPricePerType[2] = Double.parseDouble(roomPricePerNightKing);
			
		
			/* send data to database */
			HotelDBManager connection = new HotelDBManager();
		
			// call editReservation to modify the reservation.
			connection.editHotel(user.getCompanyName(),
							     amenityChecks,
							     numRoomsPerType,
							     roomPricePerType,
							     Float.parseFloat(percent));
			connection.closeManager();
		
			// clear textfields
		
			// update arraylist
			getHotelInfo();
		}
	}

	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("MainRoadrunnerStay.fxml", "MainRoadrunnerStay: Home");
		changeScene.changeScreen(event);
	}
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("Profile.fxml", "Profile");
		changeScene.changeScreen(event);
	}
}
