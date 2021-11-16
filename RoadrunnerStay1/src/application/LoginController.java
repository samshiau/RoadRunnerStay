package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable{
	
	
	
	//public static final String user = null;
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private Label loginLabel;
	static boolean isLoggedIn = false;
	static String passWord;

	static User user;

	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		loginLabel.setVisible(false);
	}
	
	
	@FXML 
	public void Login(ActionEvent event) throws IOException, InterruptedException {
		// get user input store in string
		String username = userNameInput.getText();
		//String passWord = passwordInput.getText();
		//String 
		passWord = passwordInput.getText();
				
		// print user input
		System.out.println("username is: "+ username + "\n");
		System.out.println("passWord is: "+ passWord + "\n");
				
		// clear textfields
		userNameInput.clear();
		passwordInput.clear();

		// This code is what will retrieve user data from the database. A new user object is created and they will
		// be able to book, edit, and cancel reservations.
		HotelDBManager connection = new HotelDBManager();
		int rc = connection.login(username, passWord);
		if (rc != ReturnCodes.RC_OK) {
			String rcStr = ReturnCodes.getRcAsString(rc);
			// TODO: Delete the below line and display the error to the status.
			System.out.println(rcStr);
			loginLabel.setVisible(true);
			loginLabel.setText("Incorrect Credentials");
		}
		else {
			// Handle the case where the log-in is successful.
			System.out.println("Authentication successful!");
			String[] userAttributes = connection.getUserAttributes(username);
			
			CreateAccountController tempToGetCompanyAndPos = new CreateAccountController(); 
			if (userAttributes[2].isEmpty() && userAttributes[3].isEmpty()) {
				user = new User(username, userAttributes[0], userAttributes[1]);
			}
			else {
				user = new User(username, userAttributes[0], userAttributes[1], userAttributes[2], userAttributes[3]);
			}
			//		tempToGetCompanyAndPos.companyName,tempToGetCompanyAndPos.position);

			System.out.println(userAttributes[0]);
			System.out.println(userAttributes[1]);
			System.out.println(userAttributes[2]);
			System.out.println(userAttributes[3]);
			//returnUserThatIsLoggedIn(user);
			isLoggedIn = true;
			changeScreenHome(event);
		}
		connection.closeManager();
	}
	public User returnUserThatIsLoggedIn() {
		return user;
	}
	
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
}
