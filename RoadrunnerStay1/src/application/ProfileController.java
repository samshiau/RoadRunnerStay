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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

public class ProfileController implements Initializable{
	
	@FXML private ListView<String> reservationsList;
	@FXML private Button userEditReservation;
	@FXML private Button employeeEditReservation;
	@FXML private Label wecomeUser;
	SwitchScenesController changeScene = new SwitchScenesController(); 
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
			String noReservatoin = "\n\n---------------------------------------------" +
					               "\n################################" + 	
					               "\nSorry but it looks like you don't have any " +
								   "\nreservations on file." +
								   "\n################################" +
								   "\n---------------------------------------------\n\n\n\n\n\n\n"; 
			reservationsList.getItems().addAll(noReservatoin);
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
		changeScene.setSceneInfo("MainRoadrunnerStay.fxml", "MainRoadrunnerStay: Home");
		changeScene.changeScreen(event);
	}
	
	@FXML
	public void changeScreenEditReservations(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("EditReservations.fxml", "Edit Reservations");
		changeScene.changeScreen(event);
	}
	@FXML
	public void changeScreenEditReservationsAsUser(ActionEvent event) throws IOException {
		if(hasReservation) {
			changeScene.setSceneInfo("EditReservationsAsUSer.fxml", "Edit Reservations");
			changeScene.changeScreen(event);				
		} else {
			// show error message telling user they cannot edit because they do not have a 
			// Reservation to edit
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("No Reservation to Edit");
			noInput.setHeaderText("It looks like you don't have a reservation to edit");
			noInput.setContentText("Book one hurry limited space available!");
			noInput.showAndWait();
		}
		 
	}
	@FXML
	public void changeScreenEditUserProfile(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("EditUserProfile.fxml", "Edit Profile");
		changeScene.changeScreen(event);
	}
	
	
}
