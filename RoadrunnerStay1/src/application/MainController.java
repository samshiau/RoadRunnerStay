package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController  implements Initializable{
	
	@FXML private Button start, back;
	@FXML ImageView imageView, imageView2, imageView3;
	@FXML CheckBox pool, gym, spa, businessOffice;
	boolean isTheUserLoggedIn;
	
	boolean poolSelected = false, gymSelected = false, spaSelected = false, businessOfficeSelected = false; 
	int count = 0;
	int count2 = 1;
	int count3 = 2;
	
	public void slideshow() {
		ArrayList<Image> images = new ArrayList<Image>();
		images.add(new Image("/SA_river.jpg"));
		images.add(new Image("/NY.png"));
		images.add(new Image("/sydney.png"));
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
			imageView.setImage(images.get(count));
			imageView2.setImage(images.get(count2));
			imageView3.setImage(images.get(count3));
			count++;
			count2++;
			count3++;
			if(count == 3)
				count = 0;
			if(count2 == 3)
				count2 = 0;
			if(count3 == 3)
				count3 = 0;
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources ) {
		slideshow();
	}
	public void checkboxSelected() {
		if(pool.isSelected()) {
			poolSelected = true;
		}
		if(gym.isSelected()) {
			gymSelected = true;
		} 
		if(spa.isSelected()) {
			spaSelected = true;
		} 
		if(businessOffice.isSelected()) {
			businessOfficeSelected = true;
		}
	}
	
	@FXML 
	public void changeScreenResult(ActionEvent event) throws IOException {
		checkboxSelected();
		
		ArrayList<Hotel> results;
		boolean[] amenityChecks = new boolean[4];
		amenityChecks[0] = gymSelected;
		amenityChecks[1] = spaSelected;
		amenityChecks[2] = poolSelected;
		amenityChecks[3] = businessOfficeSelected;
		
		// Gets the results from the database.
		HotelDBManager searcher = new HotelDBManager();
		results = searcher.search("", amenityChecks, "", "", 0.0, 0.0);
		
		// FIXME: The below for loop is for testing purposes to test the results received from the database. Delete
		// once the search is working.
		System.out.println("hotels received:");
		for (int i = 0; i < results.size(); i++) {
			System.out.println("- " + results.get(i).getName());
		}
		searcher.closeManager();
		
		// TODO: Display the received results from the database that are stored in the results variable.
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenResult(event);
	}
	
	@FXML 
	public void changeScreenCreateAccount(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenCreateAccount(event);
	}
	
	@FXML 
	public void changeScreenLogin(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenLogin(event);
	}
	
	@FXML 
	public void changeScreenProfile(ActionEvent event) throws IOException {
		isTheUserLoggedIn = LoginController.isLoggedIn;
		if(isTheUserLoggedIn) {
			SwitchScenesController change = new SwitchScenesController();
			change.changeScreenProfile(event);
		}
		else {
			changeScreenLogin(event);
		}
	}
	

}
