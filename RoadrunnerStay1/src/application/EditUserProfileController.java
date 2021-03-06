package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EditUserProfileController implements Initializable{
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private TextField nameInput;
	@FXML private TextField emailInput;
	@FXML private TextField positionInput;
	@FXML private Label passwordLabel;
	@FXML private Label nameLabel;
	@FXML private Label emailLabel;
	@FXML private Label companyNameLabel;
	@FXML private Label empPositionLabel, empPositionLabel2;
	@FXML private Label supportedHotelsLabel;

	SwitchScenesController changeScene = new SwitchScenesController(); 
	HotelDBManager connection = new HotelDBManager();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	String OldPassword = whoIsLogin.passWord ;
	
	@FXML private ComboBox hotelNameComboBox;
	
	ObservableList<String> options = 
		    FXCollections.observableArrayList(
		        "The Magnolia All Suites",
		        "The Lofts at Town Centre",
		        "Park North Hotel",
		        "The Courtyard Suites",
		        "The Regency Rooms",
		        "Town Inn Budget Rooms",
		        "The Comfy Motel Place",
		        "Sun Palace Inn",
		        "HomeAway Inn",
		        "Rio Inn"
		        );
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {		
		setUpComboBox();
		// show current user information
		nameLabel.setText(user.getName());
		passwordLabel.setText(OldPassword);
		emailLabel.setText(user.getEmail());
		if(user.isEmployee()) {
			companyNameLabel.setText(user.getCompanyName());
			empPositionLabel.setText(user.getPosition());
			companyNameLabel.setVisible(true);
			supportedHotelsLabel.setVisible(true);
			empPositionLabel.setVisible(true);
			empPositionLabel2.setVisible(true);
			hotelNameComboBox.setVisible(true);
			positionInput.setVisible(true);
		} else {
			companyNameLabel.setVisible(false);
			supportedHotelsLabel.setVisible(false);
			empPositionLabel.setVisible(false);
			empPositionLabel2.setVisible(false);
			hotelNameComboBox.setVisible(false);
			positionInput.setVisible(false);
		}
		
	}
	void showNewInfo() {
		nameLabel.setText(user.getName());
		passwordLabel.setText(user.getPassword());
		emailLabel.setText(user.getEmail());
		if(user.isEmployee()) {
			companyNameLabel.setText(user.getCompanyName());
			empPositionLabel.setText(user.getPosition());
		}
	}
	
	private void setUpComboBox() {
		hotelNameComboBox.setItems(options);
		hotelNameComboBox.setVisibleRowCount(4);
	}
	
	@FXML 
	public void saveChanges(ActionEvent event) throws IOException {
		String passWord = passwordInput.getText();
		String name = nameInput.getText();
		String email = emailInput.getText();
		String companyName = (String) hotelNameComboBox.getValue();
		String position = positionInput.getText();
		
		// clear textfields
		passwordInput.clear();
		nameInput.clear();
		emailInput.clear();
		positionInput.clear();
		
		if(passWord.isEmpty()) {
			user.setPassword(OldPassword);
		} else {
			user.setPassword(passWord);			
		}
		if(!name.isEmpty())
			user.setName(name);

		if(!email.isEmpty())
			user.setEmail(email);
	
		if(!(companyName == null))
			user.setCompanyName(companyName);

		if(!position.isEmpty())
			user.setPosition(position);
		
		HotelDBManager connection = new HotelDBManager();
		connection.updateUser(user);
		showNewInfo();

	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("MainRoadrunnerStay.fxml", "MainRoadrunnerStay: Home");
		changeScene.changeScreen(event);
	}
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		changeScene.setSceneInfo("Profile.fxml", "Profile");
		changeScene.changeScreen(event);
	}
}
