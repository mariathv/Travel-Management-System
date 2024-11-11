package application.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ServiceProviderController {
	 @FXML
	 private AnchorPane mainPanel; 

	 
	 //util functions
	    public void loadNewPanel() {
	        try {
	            AnchorPane newPanel = FXMLLoader.load(getClass().getResource("../scenes/ServiceProviderBooking.fxml"));
	            
	            mainPanel.getChildren().setAll(newPanel);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void exitApplication() {
	        Platform.exit(); 
	    }
}
