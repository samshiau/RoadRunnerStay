package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class EditReservationsAsUserController implements Initializable{
	
	@FXML private Button updateButton, cancelButton;
	@FXML private ListView<String> reservationsList;
	@FXML private TextField roomsInput, fromDateInput, toDateInput;
	@FXML RadioButton standardRadio; 
	@FXML RadioButton queenRadio; 
	@FXML RadioButton kingRadio;
	ToggleGroup radioGroup = new ToggleGroup();
	
	HotelDBManager connection = new HotelDBManager();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	
	public ArrayList<Reservation> getReservation;

	//String roomsInputStr, fromDateInputStr, toDateInputStr;
	String fromDateInputStr, toDateInputStr;
	String whichReservation;
	String reservationInfo;
	String hotelName;
	String roomType;
	Integer newNumRooms;
	int reservationNum;
	
 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateArrayList();
		// setup radio functionality
		radioButtonSetup();
	}
	// used for the radio button toggles	
	public void radioButtonSetup() {		
		standardRadio.setToggleGroup(radioGroup);
		queenRadio.setToggleGroup(radioGroup);
		kingRadio.setToggleGroup(radioGroup);
		// set standard room type as default
		standardRadio.setSelected(true);
	}
	
	// fill the arraylist with current reservation the user has
	public void updateArrayList(){
		// clear arraylist in case updated
		reservationsList.getItems().clear();
		getReservation = connection.getReservationsByUser(user.getUserId());
		for (int i = 0; i < getReservation.size(); i++) {
			reservationNum = i;
			reservationInfo = getReservation.get(i).toString();
			reservationsList.getItems().addAll(reservationInfo);
		}
		
	}
	// get which row is selected
	public void resultsListSelected() {
		whichReservation = reservationsList.getSelectionModel().getSelectedItem();
		// get name depending on which row is selected
		hotelName =  getReservation.get(reservationNum).getHotelName();
	}

	// get user input in TextFields
	public void userInput() {
		newNumRooms = Integer.parseInt(roomsInput.getText());
		//roomsInputStr = roomsInput.getText();
		fromDateInputStr = fromDateInput.getText();
		toDateInputStr = toDateInput.getText();
		RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
		roomType = selectedRadioButton.getText().toLowerCase();
	}

	// when updatebutton is pressed update change on DB	
	@FXML 
	public void updateButton(ActionEvent event) throws IOException {
		userInput();
		System.out.println(newNumRooms);
		
		// user has to select a booking for updating else show error message
		if(hotelName == null) {
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Select a booking");
			noInput.setHeaderText("Please select a booking");
			noInput.setContentText("Hurry limited space available!");
			noInput.showAndWait();
		// user has to fill out all inputs
		} else if(newNumRooms.equals(null)||
				fromDateInputStr.isEmpty()||
				toDateInputStr.isEmpty() ||
				roomType == null) {
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Enter all information");
			noInput.setHeaderText("Please enter all the required information");			
			noInput.showAndWait();
		}
		// if hotel is selected and user has all input then update DB
		else {
			// updates the results in the database
			connection.editReservation(user.getUserId(), hotelName, roomType, newNumRooms, fromDateInputStr, toDateInputStr);
			// clear textfields
			roomsInput.clear();
			fromDateInput.clear();
			toDateInput.clear();
			updateArrayList();
		} System.out.println();

	}
	// when cancelbutton is pressed update change on DB	
	@FXML 
	public void cancelButton(ActionEvent event) throws IOException {
		
		// user has to select a booking for updating else show error message
		System.out.println(hotelName);
		if(hotelName == null) {
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Select a booking");
			noInput.setHeaderText("Please select a booking to cancel");
			noInput.setContentText("You can always book a new one");
			noInput.showAndWait();
		} else {
			// cancel booking
			connection.cancelReservation(user.getUserId(), hotelName);
			updateArrayList();
		}

	}
	
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		connection.closeManager();
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenProfile(event);
	}
}
