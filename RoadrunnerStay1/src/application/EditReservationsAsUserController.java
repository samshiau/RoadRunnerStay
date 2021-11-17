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
	
	@FXML private Button updateButton;
	@FXML private ListView<String> reservationsList;
	@FXML private TextField roomsInput, fromDateInput, toDateInput;
	
	HotelDBManager connection = new HotelDBManager();
	LoginController whoIsLogin = new LoginController();	
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	
	public ArrayList<Reservation> getReservation;

	String roomsInputStr, fromDateInputStr, toDateInputStr;
	String whichReservation;
	static String reservationInfo;
 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateArrayList();
	}
	
	public void updateArrayList(){
		// clear arraylist in case updated
		reservationsList.getItems().clear();
		// fill the arraylist with current reservation the user has
		getReservation = connection.getReservationsByUser(user.getUserId());		
		for (int i = 0; i < getReservation.size(); i++) {
			reservationInfo = getReservation.get(i).toString();
			reservationsList.getItems().addAll(reservationInfo);
		}
		
	}
	// get which is selected
	public void resultsListSelected() {
		whichReservation = reservationsList.getSelectionModel().getSelectedItem();
		System.out.println(whichReservation);
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
		// printing for testing
		System.out.println(roomsInputStr);
		System.out.println(fromDateInputStr);
		System.out.println(toDateInputStr);
		// Gets the results from the database.
		connection.editReservation(user.getUserId(), "Rio Inn", "queen", 4);
		updateArrayList();

	}
	
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		connection.closeManager();
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenProfile(event);
	}
}
