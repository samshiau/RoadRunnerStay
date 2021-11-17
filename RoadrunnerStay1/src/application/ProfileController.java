package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProfileController implements Initializable{
	
	@FXML private ListView<String> reservationsList;
	@FXML private Button userEditReservation;
	@FXML private Button employeeEditReservation;
	@FXML private Label wecomeUser;
	HotelDBManager connection = new HotelDBManager();
	
	LoginController whoIsLogin = new LoginController();	
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	public ArrayList<Reservation> getReservation;
	String reservationInfo;
	boolean hasReservation = false;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wecomeUser.setText("Wecome: " + user.getUserId());
		
		getReservation = connection.getReservationsByUser(user.getUserId());
		// show message if user does not have reservation else show reservation  
		if(getReservation.size() == 0) {
			hasReservation = false;
			reservationsList.getItems().addAll("Sorry but it looks like you don't have any reservations");
		} else {
			hasReservation = true;
			for (int i = 0; i < getReservation.size(); i++) {
				reservationInfo = getReservation.get(i).toString();
				reservationsList.getItems().addAll(reservationInfo);
			}	
		}
		
		if(user.isEmployee()) {
			employeeEditReservation.setVisible(true);
			userEditReservation.setVisible(false);
			
		} else {
			employeeEditReservation.setVisible(false);
			userEditReservation.setVisible(true);
			
		}
		 

	}

	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	
	@FXML
	public void changeScreenEditReservations(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenEditReservations(event);	
	}
	@FXML
	public void changeScreenEditReservationsAsUser(ActionEvent event) throws IOException {
		if(hasReservation) {
			SwitchScenesController change = new SwitchScenesController();
			change.changeScreenEditReservationsAsUser(event);				
		}
		/* TODO show error message telling user they cannot edit because they do not have a 
		 * Reservation to edit
		 */
	}
	@FXML
	public void changeScreenEditUserProfile(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenEditUserProfile(event);	
	}
	
	
}
