package application.controllers;

import application.Model.ServiceProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ServiceProviderController {
	 @FXML
	 private AnchorPane mainPanel; 
	 @FXML
	 private Button nav_home, nav_notifs, nav_profile, nav_service;
	 @FXML
	 private Text profileUsername, profileAgencyName, profileEmail, profileName, profilePhoneNum;
	 
	 @FXML
	 private Pane modify_basePane, modify_passwordPane, modify_phonePane;
	 private ServiceProvider serviceProvider;
	 
	 
	 int currentTab = 1;
	 
	 
	 @FXML
	 Text usernameProfilePane,agencyProfilePane;
	 
	 public void resetPassword() {
		 modify_basePane.setVisible(false);
		 modify_passwordPane.setVisible(true);
	 }
	 
	 public void resetPhoneNumber() {
		 modify_basePane.setVisible(false);
		 modify_phonePane.setVisible(true);
	 }
	 
	 public void confirmPasswordChange() {
		 //will be implementing backend later
		 modify_passwordPane.setVisible(false);
		 modify_basePane.setVisible(true);
	 }
	 
	 public void confirmPhoneChange(){
		//will be implementing backend later
		 modify_phonePane.setVisible(false);
		 modify_basePane.setVisible(true);
	 }
	 
	
	 
	 //util functions
	 void loadProfileData() {
		 profileUsername.setText(serviceProvider.getUsername());
		 profileAgencyName.setText(serviceProvider.getAgencyName());
		 profileEmail.setText(serviceProvider.getEmail());
		 profileName.setText(serviceProvider.getUsername());
		 profilePhoneNum.setText("---");
	 }
	
	public void loadNewPanel() { //service pane
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ServiceProviderServices.fxml"));
	        AnchorPane newPanel = loader.load();
	        ServiceController serviceC = loader.getController();
	        serviceC.setServiceProvider(serviceProvider);
	        serviceC.initServicesFS();
	        mainPanel.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_service.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void setServiceProvider(ServiceProvider serviceProvider) {
	        this.serviceProvider = serviceProvider;
	        updateDashboard();
	}
	public void exitApplication() {
	    Platform.exit(); 
	}
	
	public void loadHomePane() {
	    try {
	        AnchorPane newPanel = FXMLLoader.load(getClass().getResource("../scenes/ServiceProviderHomePane.fxml"));
	        mainPanel.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_home.setStyle("-fx-background-color:  #212832;"); 
	        currentTab=1;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadProfilePane() {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ServiceProviderProfilePane.fxml"));
	        loader.setController(this);  
	        AnchorPane newPanel = loader.load();
	        mainPanel.getChildren().setAll(newPanel);
	        loadProfileData();
	        changeBackButtonBG();
	        nav_profile.setStyle("-fx-background-color:  #212832;");
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    currentTab=4;
	}
	
	private void updateDashboard() {
		//implementing logic to update dashboard with the new SP data
		usernameProfilePane.setText(serviceProvider.getUsername());
		agencyProfilePane.setText(serviceProvider.getAgencyName());
		
		
    }
	    
	
	private void changeBackButtonBG() {
		switch(currentTab) {
		case 1: nav_home.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 2: nav_service.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 3: nav_notifs.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 4: nav_profile.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		}
	}
	
	    
	    
	    
	    
}
