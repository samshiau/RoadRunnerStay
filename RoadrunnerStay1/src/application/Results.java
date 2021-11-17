package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Results implements Initializable{
	
	@FXML private Button bookIt;
	@FXML ImageView imageViewResults;
	@FXML private ListView<String> resultsList;
	MainController mainController = new MainController();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	HotelDBManager connection = new HotelDBManager();
	String whichHotel;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (int i = 0; i < MainController.getResultsArray().size()/2; i++) {
			whichHotel = MainController.getResultsArray().get(i).getName();
			resultsList.getItems().addAll(whichHotel);
		}
	}
	
	public void resultsListSelected() {
		whichHotel = resultsList.getSelectionModel().getSelectedItem();
		//showImage(whichHotel);
		
		Hotel hotel = Hotel.getHotelByName(MainController.getResultsArray(), whichHotel);
		System.out.println(hotel.getNumberOfRooms());
		showImage(hotel.getImageStream());
	}
	
	public void showImage(InputStream image) {
		try {
			Image hotelimage = new Image(image);
			imageViewResults.setImage(hotelimage);
		}
		catch (NullPointerException e) {
			// handles the case where the hotel object does not have an image.
			Image hotelImage = new Image("./NoImageAvailable.png");
			imageViewResults.setImage(hotelImage);
		}
	}
	@FXML 
	public void bookItButton(ActionEvent event) throws IOException, InterruptedException {
		// if user is not logged in show error message
		if(!LoginController.isLoggedIn) {
			 			 
			Alert userNotLoggedIn = new Alert(AlertType.NONE);
			userNotLoggedIn.setTitle("Please Login");
			userNotLoggedIn.setHeaderText("Plesae login to book hotel");
			userNotLoggedIn.setContentText("Hurry limited space available!");
			// setting the icon for the alert message
			ImageView icon = new ImageView(this.getClass().getResource("./roadrunnerIcon.png").toString());
			icon.setFitHeight(75);
			icon.setFitWidth(75);
			userNotLoggedIn.setGraphic(icon);
			
			
			ButtonType goToLogin = new ButtonType("Go to Login");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			userNotLoggedIn.getButtonTypes().setAll(goToLogin, buttonTypeCancel);
			Optional<ButtonType> result = userNotLoggedIn.showAndWait();
			// if user clicks on "goToLogin" it will take them to the login screen
			if(result.get() == goToLogin) {
				//TimeUnit.SECONDS.sleep(1);
				changeScreenLogin(event);
			}
			
		} else {
			// gather user's information and send for booking
			String userID = user.getUserId();
			int hotelId = connection.getHotelId(whichHotel);
		
			String roomType = "queen";
		
			String startDate = mainController.getStartDate();
			String endDate = mainController.getEndDate();
		
			int rc = connection.bookReservation(userID,
												hotelId,
												roomType,
												startDate, 
												endDate);
		
			if (rc != 0) {
				System.out.println("Booking failed.");
			}
			connection.closeManager();
			}
	}
	
	

	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		closeImages(MainController.getResultsArray());
		change.changeScreenonHome(event);
	}
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenLogin(event);
	}
	
	public void closeImages(ArrayList<Hotel> hotels) {
		// Releases the InputStream references for the results images. Call this when switching scenes from
		// this page.
		for (int i = 0; i < hotels.size(); i++) {
			try {
				hotels.get(i).getImageStream().close();
			}
			catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			catch (NullPointerException e) {
				continue;
			}
		}
	}
}
