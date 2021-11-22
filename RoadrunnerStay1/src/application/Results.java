package application;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Results implements Initializable{
	
	@FXML private Button bookIt;
	@FXML ImageView imageViewResults;
	@FXML private ListView<String> resultsList;
	@FXML RadioButton standardRadio; 
	@FXML RadioButton queenRadio; 
	@FXML RadioButton kingRadio;
	@FXML Label hotelNameLabel;
	@FXML TextField numRoomsTextField;
	
	MainController mainController = new MainController();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	HotelDBManager connection = new HotelDBManager();
	ArrayList<Hotel> results = MainController.getResultsArray();
	ToggleGroup radioGroup = new ToggleGroup();
	
	int numRooms = 1;
	String whichHotel;
	String roomType;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// setup radio functionality
		radioButtonSetup();
		for (int i = 0; i < MainController.getResultsArray().size()/2; i++) {
			whichHotel = MainController.getResultsArray().get(i).getName();
			resultsList.getItems().addAll(whichHotel);
		}
	}
	
	// used for the radio button toggles	
	public void radioButtonSetup() {		
		standardRadio.setToggleGroup(radioGroup);
		queenRadio.setToggleGroup(radioGroup);
		kingRadio.setToggleGroup(radioGroup);
		// set standard room type as default
		standardRadio.setSelected(true);
	}
	
	public void resultsListSelected() throws IOException {

		whichHotel = resultsList.getSelectionModel().getSelectedItem();
		//showImage(whichHotel);
		
		System.out.println("Getting hotel: " + whichHotel);
		
		Hotel hotel = Hotel.getHotelByName(results, whichHotel);
		System.out.println(hotel.getNumberOfRooms());
		showImage(hotel.getImageStream());
	}
	
	public void showImage(InputStream image) throws IOException {
		try {
			if (image.markSupported()) {
				image.reset();
			}
			Image hotelimage = new Image(image);
			imageViewResults.setImage(hotelimage);
		}
		catch (NullPointerException e) {
			// handles the case where the hotel object does not have an image.
			Image hotelImage = new Image("./NoImageAvailable.png");
			imageViewResults.setImage(hotelImage);
		}
		catch (IOException e) {
			Image hotelImage = new Image("./NoImageAvailable.png");
			imageViewResults.setImage(hotelImage);
		}
	}
	
	// get users input for booking
	public void userInput() {
		numRooms = Integer.parseInt(numRoomsTextField.getText());
		RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
		roomType = selectedRadioButton.getText().toLowerCase();
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
		// if user is an employee they cannot book show error 
		} else if(user.isEmployee()) {
			Alert noInput = new Alert(AlertType.ERROR);
			noInput.setTitle("Cannot book as Employee");
			noInput.setHeaderText("Please sign in as a customer to book hotel");
			noInput.setContentText("Hurry limited space available!");
			noInput.showAndWait();
		}
		// if user is logged in let them book	
		else {
			// gather user's information and send for booking
			userInput();
			String userID = user.getUserId();
			int hotelId = connection.getHotelId(whichHotel);			
			String startDate = mainController.getStartDate();
			String endDate = mainController.getEndDate();	  
		
//			int rc = connection.bookReservation(userID,
//												hotelId,
//												roomType,
//												startDate, 
//												endDate,
//												numRooms);
			int rc = connection.bookReservation("user67",
					8,
					"standard",
					"2021-04-05", 
					"2021-04-06",
					1);
			// clear textfield
			numRoomsTextField.clear();
		
			if (rc != 0) {
				System.out.println("Booking failed.");
			}
			connection.closeManager();
			}
	}
	
	

	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		closeImages();
		change.changeScreenonHome(event);
	}
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		closeImages();
		change.changeScreenLogin(event);
	}
	
	public void closeImages() {
		System.out.println("Closing images...");
		for (int i = 0; i < results.size(); i++) {
			try {
				results.get(i).getImageStream().close();
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
