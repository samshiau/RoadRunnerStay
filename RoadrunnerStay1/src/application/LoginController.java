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
			changeScreenLogin(event);	
		}
		if(username.equals("admin") && passWord.equals("pass")) {
			loginLabel.setText("Login Success");
			admin = true;
			/* send that user is a admin to database*/
			changeScreenLogin(event);
		} else {
			loginLabel.setText("Login Failed");
		}
		
		// check username and password against database

		}
}
