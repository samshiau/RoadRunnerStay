package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	
	
	
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private Label loginLabel;
	static boolean admin;
	static boolean isLoggedIn = false;
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	
	@FXML 
	public void Login(ActionEvent event) throws IOException, InterruptedException {
		// get user input store in string
		String username = userNameInput.getText();
		//String passWord = passwordInput.getText();
		String passWord = passwordInput.getText();
				
		// print user input
		System.out.println("username is: "+ username + "\n");
		System.out.println("passWord is: "+ passWord + "\n");
				
		// clear textfields
		userNameInput.clear();
		passwordInput.clear();
		
		if(username.equals("user") && passWord.equals("pass")) {
			loginLabel.setText("Login Success");
			admin = false;
			isLoggedIn = true;
			changeScreenLogin(event);	
		}
		
		if(username.equals("admin") && passWord.equals("pass")) {
			loginLabel.setText("Login Success");
			admin = true;
			isLoggedIn = true;
			
			changeScreenLogin(event);
		} else {
			loginLabel.setText("Login Failed");
		}
		


////////////////////////////////////////////////////////////
		// This code is what will retrieve user data from the database. A new user object is created and they will
		// be able to book, edit, and cancel reservations.
		HotelDBManager connection = new HotelDBManager();
		int rc = connection.login(username, passWord);
		if (rc != ReturnCodes.RC_OK) {
			String rcStr = ReturnCodes.getRcAsString(rc);
			// TODO: Delete the below line and display the error to the status.
			System.out.println(rcStr);
		}
		else {
			// Handle the case where the log-in is successful.
			System.out.println("Authentication successful!");
			String[] userAttributes = connection.getUserAttributes(username);
			User user = new User(username, userAttributes[0], userAttributes[1]);
			
			// TODO: Work with obtained user data.
		}
		connection.closeManager();

	}
		////////////////////////////////////////////////////////////
}
