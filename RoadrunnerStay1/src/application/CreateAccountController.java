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
import javafx.scene.text.Text;

public class CreateAccountController implements Initializable{
	@FXML private TextField userNameInput;
	@FXML private PasswordField passwordInput;
	@FXML private TextField nameInput;
	@FXML private TextField emailInput;
	@FXML private TextField positionInput;
	@FXML private Label supportedHotelsLabel;
	@FXML private Label positionLabel;
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
	if(!AccountTypeController.isEmployee) {
		supportedHotelsLabel.setVisible(false);
		hotelNameComboBox.setVisible(false);
		
		positionInput.setVisible(false);
		positionLabel.setVisible(false);
		}
	}
	
	private void setUpComboBox() {
		hotelNameComboBox.setItems(options);
		hotelNameComboBox.setVisibleRowCount(4);
	}
	
	@FXML 
	public void createAccount(ActionEvent event) throws IOException {
	
		// get user input store in string
		String username = userNameInput.getText();
		//String passWord = passwordInput.getText();
		String passWord = passwordInput.getText();
		String name = nameInput.getText();
		String email = emailInput.getText();
		String companyName = (String) hotelNameComboBox.getValue();
		String position = positionInput.getText();
		
		// clear textfields
		userNameInput.clear();
		passwordInput.clear();
		nameInput.clear();
		emailInput.clear();
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
