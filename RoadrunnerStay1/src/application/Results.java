package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Results implements Initializable{
			
	@FXML private ListView<String> resultsList;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// replace code with getters
		ArrayList<String> temps = new ArrayList<String>();
		String token = "";
		
//		for (int i = 0; i < MainController.results.size(); i++) {
//			//System.out.println("- " + MainController.results.get(i).getName());
//			token = MainController.results.get(i).getName();
//			temps.add(token);
//			System.out.println(token);
//			System.out.println(temps.get(i));
//		}
//		String[] tempsArray = temps.toArray(new String[0]);
//		
//		for (String s : tempsArray) {
//			resultsList.getItems().add(s);
//		}
				    
	}
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}
}
