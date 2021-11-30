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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SwitchScenesController{
	
	// this class is designed as the scene switcher for the application
	
	private String setTitle;
	private String fxmlFile;
	private String screenID;
	
	// get info from controllers
	public void setSceneInfo(String fxmlFileSet, String setTitleSet) {
		fxmlFile = fxmlFileSet;
		setTitle = setTitleSet;
	}
	// get info from controllers
		public void setSceneInfo(String fxmlFileSet, String setTitleSet, String setID) {
			fxmlFile = fxmlFileSet;
			setTitle = setTitleSet;
			screenID = setID;
		}
	
	// change to desired scene according to controller info
	@FXML 
	public void changeScreen(ActionEvent event) throws IOException {
		FXMLLoader viewHome = new FXMLLoader(getClass().getResource(fxmlFile));
		Parent root = (Parent) viewHome.load();
		//root.setId("Home");
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle(setTitle);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("logoicon.png")));
		stage.show();
	}
}
