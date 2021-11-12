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
	@FXML private Label passwordLabel;
	@FXML private Label nameLabel;
	@FXML private Label emailLabel;
	@FXML private Label companyNameLabel;
	@FXML private Label empPositionLabel;

	HotelDBManager connection = new HotelDBManager();
	LoginController whoIsLogin = new LoginController();
	User user = whoIsLogin.returnUserThatIsLoggedIn();
	String OldPassword = whoIsLogin.passWord ;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {		
		// show current user information
		nameLabel.setText(user.getName());
		passwordLabel.setText(OldPassword);
		emailLabel.setText(user.getEmail());
		if(user.isEmployee()) {
			companyNameLabel.setText(user.getCompanyName());
			empPositionLabel.setText(user.getPosition());
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
		
		if(passWord.isEmpty()) {
			user.setPassword(OldPassword);
		} else {
			user.setPassword(passWord);			
		}
		if(!name.isEmpty())
			user.setName(name);

		if(!email.isEmpty())
			user.setEmail(email);
	
		if(!companyName.isEmpty())
			user.setCompanyName(companyName);

		if(!position.isEmpty())
			user.setPosition(position);
		
		HotelDBManager connection = new HotelDBManager();
		connection.updateUser(user);
		showNewInfo();

	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
}
