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

public class MainController  implements Initializable{
	
	@FXML
	private Button start, back;
	
	@FXML ImageView imageView; @FXML ImageView imageView2; @FXML ImageView imageView3;
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
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenProfile(event);
	}
	@FXML 
	public void changeScreenResult(ActionEvent event) throws IOException {
		SwitchScenesController change = new SwitchScenesController();
		change.changeScreenResult(event);
	}

}
