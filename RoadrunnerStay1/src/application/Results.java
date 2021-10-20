package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
	
	@FXML 
	public void changeScreenHome(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenonHome(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Scanner inFile1 = null;
		String token1 = "";
		try {
			inFile1 = new Scanner(new File("resutls.txt")).useDelimiter(",\\s*");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
		    	resultsList.getItems().add(s);
		      System.out.println(s);
		    }
		  }

		
	
	
}
