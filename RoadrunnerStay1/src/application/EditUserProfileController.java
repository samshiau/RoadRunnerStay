package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EditUserProfileController implements Initializable{
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private TextField nameInput;
	@FXML private TextField emailInput;
	@FXML private TextField companyNameInput;
	@FXML private TextField positionInput;
	@FXML private Label nameLabel;
	@FXML private Label emailLabel;
	@FXML private Label companyNameLabel;
	@FXML private Label empPositionLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		HotelDBManager connection = new HotelDBManager();
		String username = "username"; // using for testing 
		String[] userAttributes = connection.getUserAttributes(username);
		
		// show current user information
		nameLabel.setText(userAttributes[0]);		
		emailLabel.setText(userAttributes[1]);
		companyNameLabel.setText(userAttributes[2]);
		empPositionLabel.setText(userAttributes[3]);
	}
	@FXML 
	public void saveChanges(ActionEvent event) throws IOException {
		String passWord = passwordInput.getText();
		String name = nameInput.getText();
		String email = emailInput.getText();
		String companyName = companyNameInput.getText();
		String position = positionInput.getText();
		
		// clear textfields
		passwordInput.clear();
		nameInput.clear();
		emailInput.clear();
		companyNameInput.clear();
		positionInput.clear();
		
		// add code to send to DB
	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
}
