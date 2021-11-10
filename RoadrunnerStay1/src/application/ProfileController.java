package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProfileController implements Initializable{
	
	@FXML private ListView<String> reservationsList;
	@FXML private Label wecomeUser;
	
	boolean userActType;

	public void checkType() {
		userActType = LoginController.admin; 
		System.out.println(userActType); 
		if (userActType) {
			wecomeUser.setText("Wecome Admin");
		} else {
			wecomeUser.setText("Wecome User");
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Scanner inFile1 = null;
		String token1 = "";
		try {
			inFile1 = new Scanner(new File("resutls.txt")).useDelimiter(",\\s*");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> temps = new ArrayList<String>();
		while (inFile1.hasNext()) {
		      // find next line
		      token1 = inFile1.next();
		      temps.add(token1);
		    }
		    inFile1.close();

		    String[] tempsArray = temps.toArray(new String[0]);

		    for (String s : tempsArray) {
		    	reservationsList.getItems().add(s);
		    }
		checkType();
	}

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
	@FXML
	public void changeScreenEditReservationsAsUser(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenEditReservationsAsUser(event);	
	}
	
	
}
