package application;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class MainController  implements Initializable{
	
	@FXML private Button profileButton, loginButton, logOutButton, createAccountButton, searchButton;
	@FXML private TextField hotelNameInput, fromDateInput, toDateInput, minPriceInput, maxPriceInput;
	String hotelNameInputStr, fromDateInputStr, toDateInputStr, minPriceInputStr, maxPriceInputStr;
	@FXML ImageView imageView, imageView2, imageView3;
	@FXML CheckBox pool, gym, spa, businessOffice;
	
	
	boolean isTheUserLoggedIn;
	boolean poolSelected = false, gymSelected = false, spaSelected = false, businessOfficeSelected = false; 
	private static ArrayList<Hotel> results;
	private ArrayList<Image> images = new ArrayList<Image>();
	int count = 0, count2 = 1, count3 = 2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		addJPGs();
		slideshow();
		checkIfLogin();
	}
	void checkIfLogin(){
		if(LoginController.isLoggedIn) {
			loginButton.setVisible(false);
			createAccountButton.setVisible(false);
			
		} else {
			profileButton.setVisible(false);
			logOutButton.setVisible(false);
		}
	}
	public void logoutButtton() {
		loginButton.setVisible(true);
		createAccountButton.setVisible(true);
		profileButton.setVisible(false);
		logOutButton.setVisible(false);
	}
	
	// addes all ".jpg"s to images arraylist
	public void addJPGs() {
		//Creating a File object for directory
		File directoryPath = new File("./src");
		//Creating filter for jpg files
		FilenameFilter jpgFilefilter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".jpg")) {
					return true;
				} else {
					return false;
				}
			}
		};
		String imageFilesList[] = directoryPath.list(jpgFilefilter);  
		for(String fileName : imageFilesList) {
			images.add(new Image(fileName));
		}			
	}
	
	// controls the three picture slideshow
	public void slideshow() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
			imageView.setImage(images.get(count));
			imageView2.setImage(images.get(count2));
			imageView3.setImage(images.get(count3));
			count++; count2++; count3++;
			if(count == images.size())
				count = 0;
			if(count2 == images.size())
				count2 = 0;
			if(count3 == images.size())
				count3 = 0;
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	public void userInput() {
		// check which and if checkboxes are selected
		if(pool.isSelected())
			poolSelected = true;
		if(gym.isSelected())
			gymSelected = true;
		if(spa.isSelected()) 
			spaSelected = true;
		if(businessOffice.isSelected()) 
			businessOfficeSelected = true;
		
		// get user input in TextFields
		hotelNameInputStr = hotelNameInput.getText();
		fromDateInputStr = fromDateInput.getText();
		toDateInputStr = toDateInput.getText();
		minPriceInputStr = minPriceInput.getText();
		maxPriceInputStr = maxPriceInput.getText();	
	}


	@FXML 
	public void changeScreenResult(ActionEvent event) throws IOException {
		userInput();
		
		// Gets the user-checked amenities.
		boolean[] amenityChecks = new boolean[4];
		amenityChecks[0] = gymSelected;
		amenityChecks[1] = spaSelected;
		amenityChecks[2] = poolSelected;
		amenityChecks[3] = businessOfficeSelected;
		
		// Parses doubles from the min/max price text fields. In case there is an error in the format or there is no
		// input for each of the prices, the minimum and maximum value of 0 is used and the search query will not use
		// them.
		double minPrice;
		double maxPrice;
		try {
			minPrice = Double.parseDouble(minPriceInputStr);
		}
		catch (NumberFormatException e) {
			minPrice = 0.0;
		}
		catch (NullPointerException e) {
			minPrice = 0.0;
		}
		
		try {
			maxPrice = Double.parseDouble(maxPriceInputStr);
		}
		catch (NumberFormatException e) {
			maxPrice = 0.0;
		}
		catch (NullPointerException e) {
			maxPrice = 0.0;
		}
		
		
		// Gets the results from the database.
		HotelDBManager searcher = new HotelDBManager();
		results = searcher.search(hotelNameInputStr, amenityChecks, fromDateInputStr, toDateInputStr, minPrice, maxPrice);
		getResultsArray().addAll(results);
		searcher.closeManager();
		
		// TODO: Display the received results from the database that are stored in the results variable. Sometimes the list may not have any results.
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenResult(event);
	}
	
	public static ArrayList<Hotel> getResultsArray(){
		return results;
	}
	
	@FXML 
	public void changeScreenCreateAccount(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenCreateAccount(event);
	}
	
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenLogin(event);
	}
	
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		isTheUserLoggedIn = LoginController.isLoggedIn;
		if(isTheUserLoggedIn) {
			SwitchScenesController change = new SwitchScenesController();
			change.changeScreenProfile(event);
		}
		else {
			changeScreenLogin(event);
		}
	}
	

}
