package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ProfileController {
	

	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
	
	
	@FXML
	public void changeScreenEditReservations(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenEditReservations(event);	
	}
	
	
}
