package application.controllers;

import application.Model.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CustomerController {
	int currentTab = 1;
	private Customer customer;
	@FXML
	private Text usernameCus; // Make sure this matches the fx:id in the FXML file
	@FXML
	private Text nameCus; 
	@FXML
	private AnchorPane mainPanel_cus,selection,home; 
	@FXML
	private Button nav_home_cus, nav_notifs_cus, nav_profile_cus, nav_service_cus,nav_Bookings;
	
	
	
	 
	public void setCustomer(Customer customer) {
        this.customer = customer;
        //updateDashboard();
	}
	
	 
	public void loadHomePane_cus() {
		try {
			
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/CustomerHomePane.fxml"));
	    	AnchorPane newPanel = loader.load();
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_home_cus.setStyle("-fx-background-color:  #212832;");
	        updateDashboard();
	        currentTab=1;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadServicePane_cus() { //service pane
	    try {
	    	if(!selection.isVisible()) {
	    		selection.setVisible(true);
			}
	    	home.setVisible(false);
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadServiceSearch_cus() { //service pane
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchTravel.fxml"));
	    	AnchorPane newPanel = loader.load();
	    	ServiceControllerCus serviceC = loader.getController();
			serviceC.setCustomer(customer);
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadBooking_cus() { //service pane
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchTravel1.fxml"));
	    	AnchorPane newPanel = loader.load();
	    	BookingController bc = loader.getController();
			bc.setCustomer(customer);
			bc.loadData();
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_Bookings.setStyle("-fx-background-color:  #212832;");
	        currentTab=5;
	        
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void changeBackButtonBG() {
		switch(currentTab) {
		case 1: nav_home_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 2: nav_service_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 3: nav_notifs_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 4: nav_profile_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 5: nav_Bookings.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		}
	}
	
	private void updateDashboard() {
	    if (usernameCus != null && nameCus != null && customer != null) { 
	        usernameCus.setText(customer.getUsername());
	        nameCus.setText(customer.getName());
	    } else {
	        System.out.println("Error: usernameCus, nameCus, or customer is null.");
	    }
	}
	
	public void exitApplication() {
	    Platform.exit(); 
	}
	
}
