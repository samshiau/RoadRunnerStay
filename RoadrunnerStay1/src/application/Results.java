package application;


import java.io.File;
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
	@FXML Label numRoomsLabel;
	@FXML Label totalCostLabel;
	
	@FXML Label standardPriceLabel;
	@FXML Label queenPriceLabel;
	@FXML Label kingPriceLabel;

	MainController mainController = new MainController();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	SwitchScenesController changeScene = new SwitchScenesController(); 
	HotelDBManager connection = new HotelDBManager();
	ArrayList<Hotel> results = MainController.getResultsArray();
	ToggleGroup radioGroup = new ToggleGroup();
	@FXML ImageView bookedCelebrationView;
	@FXML ImageView bookedCelebrationView2;
	
	// number of rooms that can be booked 
	int numRooms = 1;
	
	// price of rooms per hotel
	double getStandardPrice;
	double getQueenPrice;
	double getKingPrice;
	
	String whichHotel;
	String roomType;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		// setup radio functionality
		radioButtonSetup();
		
		// set default price
		standardPriceLabel.setText("$10");
		queenPriceLabel.setText("$25");
		kingPriceLabel.setText("$50");
		
		// set default total price
		totalCostLabel.setText("$100");
		
		for (int i = 0; i < MainController.getResultsArray().size()/2; i++) {
			whichHotel = MainController.getResultsArray().get(i).getName();

			resultsList.getItems().addAll(whichHotel);
		}
		numRoomsLabel.setText(String.valueOf(numRooms));
	}
	
	// used for to setup the radio button toggles	
	public void radioButtonSetup() {		
		standardRadio.setToggleGroup(radioGroup);
		queenRadio.setToggleGroup(radioGroup);
		kingRadio.setToggleGroup(radioGroup);
		// set standard room type as default
		standardRadio.setSelected(true);
	}
	
	// decrease number of rooms but not below 1 
	public void decreaseRoomNumber() {
		numRooms--;
		if(numRooms < 1) {
			numRooms = 1;
		}
		totalCost();
		numRoomsLabel.setText(String.valueOf(numRooms));
	}
	
	// increase number of rooms but not above 7
	public void increaseRoomNumber() {
		numRooms++;
		if(numRooms > 7) {
			numRooms = 7;
		}
		totalCost();
		numRoomsLabel.setText(String.valueOf(numRooms));
	}
	
	// shows total cost of booking depending on room type and number of rooms
	public void totalCost() {
		RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
		roomType = selectedRadioButton.getText().toLowerCase();
		double totalCostPrint;
		if(roomType.equals("standard")) {
			totalCostPrint = getStandardPrice * numRooms;
			totalCostLabel.setText("$" + String.valueOf(totalCostPrint));
		}
		if(roomType.equals("queen")) {
			totalCostPrint = getQueenPrice * numRooms;
			totalCostLabel.setText("$" + String.valueOf(totalCostPrint));
		}
		if(roomType.equals("king")) {
			totalCostPrint = getKingPrice * numRooms;
			totalCostLabel.setText("$" + String.valueOf(totalCostPrint));
		}
		
	}
	
	// what item is currently selected on the arraylist
	public void resultsListSelected() throws IOException {
		int arraylistIndex = resultsList.getSelectionModel().getSelectedIndex();

		whichHotel = resultsList.getSelectionModel().getSelectedItem();
		getStandardPrice = MainController.getResultsArray().get(arraylistIndex).getPriceStandard();
		getQueenPrice = MainController.getResultsArray().get(arraylistIndex).getPriceQueen();
		getKingPrice = MainController.getResultsArray().get(arraylistIndex).getPriceKing();
		
		// show price of rooms
		standardPriceLabel.setText("$" + String.valueOf(getStandardPrice));
		queenPriceLabel.setText("$" + String.valueOf(getQueenPrice));
		kingPriceLabel.setText("$" + String.valueOf(getKingPrice));
		
		Hotel hotel = Hotel.getHotelByName(results, whichHotel);
		System.out.println(hotel.getNumberOfRooms());
		showImage(hotel.getImageStream());
		totalCost();
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
			ImageView icon = new ImageView(this.getClass().getResource("./logoicon.png").toString());
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
		
			int rc = connection.bookReservation(userID,
												hotelId,
												roomType,
												startDate, 
												endDate,
												numRooms);
			if (rc != 0) {
				// TODO: SUGGESTION: Show an alert window as to why the booking failed.
				System.out.println("Booking failed.");
				return;
			}
		
			// if user is successful booking show confetti
			Image bookedConfetti = new Image("./booked.gif");
			bookedCelebrationView.toFront();
			bookedCelebrationView2.toFront();
			
			bookedCelebrationView.setImage(bookedConfetti);
			bookedCelebrationView2.setImage(bookedConfetti);
			// show user message and take them back to main menu
			Alert userNotLoggedInBook = new Alert(AlertType.NONE);
			userNotLoggedInBook.setTitle("Booking Complete");
			userNotLoggedInBook.setHeaderText("Your booking is now complete you can now return to the main menu");
			userNotLoggedInBook.setContentText("Enjoy the stay!");
			// setting the icon for the alert message
			ImageView icon = new ImageView(this.getClass().getResource("./logoicon.png").toString());
			icon.setFitHeight(75);
			icon.setFitWidth(75);
			userNotLoggedInBook.setGraphic(icon);			
			ButtonType goToLogin = new ButtonType("Go to Main Menu");
			//ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			userNotLoggedInBook.getButtonTypes().setAll(goToLogin);
			Optional<ButtonType> result = userNotLoggedInBook.showAndWait();
			// if user clicks on "goToLogin" it will take them to the login screen
			if(result.get() == goToLogin) {
				//TimeUnit.SECONDS.sleep(1);
				changeScreenHome(event);
			}
		}
		connection.closeManager();
		

	}
	
	

	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("MainRoadrunnerStay.fxml", "MainRoadrunnerStay: Home");
		closeImages();
		changeScene.changeScreen(event);
	}
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("Login.fxml", "Login");
		closeImages();
		changeScene.changeScreen(event);
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
