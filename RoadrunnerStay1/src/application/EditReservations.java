package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditReservations implements Initializable{
	
	
	
	@FXML private TextField numRooms;
	@FXML private TextField pricePerNight;
	
	@FXML public ComboBox<String> comboBox;
	String percent; 
	
	ObservableList<String> options = 
		    FXCollections.observableArrayList(
		        "15%","20%","25%","35%");
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		comboBox.setItems(options);
	}
	
	@FXML
	public void weekendDifferential(ActionEvent event) {
		percent =comboBox.getValue();
	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenProfile(event);
	}
	
	@FXML 
	public void saveChanges(ActionEvent event) throws IOException {
		// get user input store in string
		String numberOfRooms = numRooms.getText();
		String roomPricePerNight = pricePerNight.getText();	
		
		
		// print what will be sent to database (for testing)
		System.out.println(numberOfRooms);
		System.out.println(roomPricePerNight);
		System.out.println(percent);
		
		/* send data to database */
		HotelDBManager connection = new HotelDBManager();
		// TODO: Call editReservation to modify the reservation.
		connection.closeManager();
		
		// clear textfields
		numRooms.clear();
		pricePerNight.clear();
		comboBox.valueProperty().set(null);
	}
}
