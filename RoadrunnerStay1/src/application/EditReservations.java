package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class EditReservations implements Initializable{
	
	
	
	@FXML private TextField numRooms;
	@FXML private TextField pricePerNight;
	
	@FXML public ComboBox<String> comboBoxWeekendDifferential;
	@FXML public ComboBox<String> comboBoxRoomType;
	
	@FXML private ListView<String> resultList;
	
	String percent; 
	String roomType;
	
	ObservableList<String> WeekendDifferentialoptions = 
		    FXCollections.observableArrayList(
		        "15%","20%","25%","35%");
	
	ObservableList<String> roomTypeOptions = 
		    FXCollections.observableArrayList(
		        "Standard","Queen","King");

	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		// setup dropdown menus
		comboBoxWeekendDifferential.setItems(WeekendDifferentialoptions);
		comboBoxRoomType.setItems(roomTypeOptions);
		
		
		
		
		
		
		
		
		
		
		// using as temporay holder for listview will replace with getters
		////////////////////////////////////////
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
	    	resultList.getItems().add(s);
	    }
	    ////////////////////////////////////////
	}
	
	@FXML
	public void weekendDifferential(ActionEvent event) {
		percent =comboBoxWeekendDifferential.getValue();
	}
	@FXML
	public void roomType(ActionEvent event) {
		roomType =comboBoxRoomType.getValue();
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
		comboBoxWeekendDifferential.valueProperty().set(null);
		comboBoxRoomType.valueProperty().set(null);
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
}
