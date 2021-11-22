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
import javafx.scene.text.Text;

public class CreateAccountController implements Initializable{
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private TextField nameInput;
	@FXML private TextField emailInput;
	@FXML private TextField companyNameInput;
	@FXML private TextField positionInput;
	@FXML private Label companyNameLabel;
	@FXML private Label positionLabel;
	
	//static String companyName;
	//static String position;
	

	@Override
	public void initialize(URL location, ResourceBundle resources ) {
	if(!AccountTypeController.isEmployee) {
		companyNameInput.setVisible(false);
		companyNameLabel.setVisible(false);
		positionInput.setVisible(false);
		positionLabel.setVisible(false);
		}
	}
	
	@FXML 
	public void createAccount(ActionEvent event) throws IOException {
	
		// get user input store in string
		String username = userNameInput.getText();
		//String passWord = passwordInput.getText();
		String passWord = passwordInput.getText();
		String name = nameInput.getText();
		String email = emailInput.getText();
		String companyName = companyNameInput.getText();
		String position = positionInput.getText();
		
		// print user input (for testing)
		System.out.println("username is: "+ username + "\n");
		System.out.println("passWord is: "+ passWord + "\n");
		System.out.println("name is: "+ name + "\n");
		System.out.println("email is: "+ email + "\n");
		System.out.println("Company Name is: "+ companyName + "\n");
		System.out.println("position is: "+ position + "\n");
		
		// clear textfields
		userNameInput.clear();
		passwordInput.clear();
		nameInput.clear();
		emailInput.clear();
		companyNameInput.clear();
		positionInput.clear();

		HotelDBManager connection = new HotelDBManager();
		int rc = connection.addUser(username, passWord, name, email, companyName, position);
		if (rc != ReturnCodes.RC_OK) {
			String rcStr = ReturnCodes.getRcAsString(rc);
			// TODO: Delete the below line and display the error to the status.
			System.out.println(rcStr);
		}
		connection.closeManager();
		changeScreenCreateAccount(event);
	}
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	@FXML 
	public void changeScreenCreateAccount(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
}
