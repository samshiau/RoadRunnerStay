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
	
	
	@FXML
	public void changeScreenCreateAccount(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Create Account");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	@FXML
	public void changeScreenLogin(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Login");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	
	@FXML
	public void changeScreenProfile(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("Profile.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Profile");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	@FXML
	public void changeScreenEditReservations(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("EditReservations.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Edit Reservations");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	@FXML
	public void changeScreenAccountType(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("AccountType.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Account Type");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	@FXML
	public void changeScreenEditReservationsAsUser(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("EditReservationsAsUSer.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Edit Reservation");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	@FXML
	public void changeScreenEditUserProfile(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("EditUserProfile.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Edit Profile");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	
	@FXML
	public void changeScreenResult(ActionEvent event) throws IOException {
		FXMLLoader viewDifficulty = new FXMLLoader(getClass().getResource("Results.fxml"));
		Parent root = (Parent) viewDifficulty.load();
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("Results");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}
	
	@FXML 
	public void changeScreenonHome(ActionEvent event) throws IOException {
		FXMLLoader viewHome = new FXMLLoader(getClass().getResource("MainRoadrunnerStay.fxml"));
		Parent root = (Parent) viewHome.load();
		root.setId("Home");
	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.centerOnScreen();
		
		stage.setScene(scene);
		stage.setTitle("MainRoadrunnerStay: Home");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("roadrunnerIcon.png")));
		stage.show();
	}

}
