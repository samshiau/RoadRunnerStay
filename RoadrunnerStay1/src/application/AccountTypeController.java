package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AccountTypeController {

	@FXML private Button customerButton;
	@FXML private Button employeeButton;
	static boolean isEmployee = false;
	
	
	@FXML 
	public void customerButton(ActionEvent event) throws IOException {
		isEmployee = false;
		changeScreenCreateAccount(event);
	}
	@FXML 
	public void employeeButton(ActionEvent event) throws IOException {
		isEmployee = true;
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
		change.changeScreenCreateAccount(event);
	}
}
