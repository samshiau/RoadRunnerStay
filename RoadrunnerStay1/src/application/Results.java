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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Results implements Initializable{
	
	@FXML private Button bookIt;
	@FXML ImageView imageViewResults;
	@FXML private ListView<String> resultsList;
	MainController mainController = new MainController();
	String whichHotel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (int i = 0; i < MainController.getResultsArray().size()/2; i++) {
			whichHotel =   MainController.getResultsArray().get(i).getName();
			resultsList.getItems().addAll(whichHotel);
		}   
	}
	
	public void resultsListSelected() {
		whichHotel = resultsList.getSelectionModel().getSelectedItem();
		showIamge(whichHotel);
	}
	
	public void showIamge(String hotelName) {
		Image hotelimage = new Image("/"+hotelName+".jpg", true);
		imageViewResults.setImage(hotelimage);
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
		//HotelDBManager.bookReservation(userId, hotelId, roomType, startDate, endDate);
	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
}
