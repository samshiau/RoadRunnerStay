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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditReservationsAsUserController implements Initializable{
	
	@FXML private Button updateButton, cancelButton;
	@FXML private ListView<String> reservationsList;
	@FXML private TextField roomsInput, fromDateInput, toDateInput;
	
	HotelDBManager connection = new HotelDBManager();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	
	public ArrayList<Reservation> getReservation;

	String roomsInputStr, fromDateInputStr, toDateInputStr;
	String whichReservation;
	String reservationInfo;
	String hotelName;
	int reservationNum;
	
 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateArrayList();
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
		roomsInputStr = roomsInput.getText();
		fromDateInputStr = fromDateInput.getText();
		toDateInputStr = toDateInput.getText();
	}

	// when updatebutton is pressed update change on DB	
	@FXML 
	public void updateButton(ActionEvent event) throws IOException {
		userInput();
		
		// user has to select a booking for updating else show error message
		System.out.println(hotelName);
		if(hotelName == null) {
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Select a booking");
			noInput.setHeaderText("Please select a booking");
			noInput.setContentText("Hurry limited space available!");
			noInput.showAndWait();
		} else {
			// updates the results in the database
			connection.editReservation(user.getUserId(), hotelName, "queen", 4, fromDateInputStr, toDateInputStr);
			// clear textfields
			roomsInput.clear();
			fromDateInput.clear();
			toDateInput.clear();
			updateArrayList();
		}

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
