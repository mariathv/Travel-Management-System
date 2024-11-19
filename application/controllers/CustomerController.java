package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Model.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerController {
	int currentTab = 1;
	private Customer customer;
	@FXML
	Text Cus_name,Cus_username;
	@FXML
	private AnchorPane mainPanel_cus,selection,home; 
	@FXML
	private Button nav_home_cus, nav_notifs_cus, nav_profile_cus, nav_service_cus;
	
	
	
	 
	public void setCustomer(Customer customer) {
        this.customer = customer;
        updateDashboard();
	}
	
	 
	public void loadHomePane_cus() {
	    try {
	    	updateDashboard();
	    	if(!home.isVisible()) {
	    		home.setVisible(true);
			}
	    	selection.setVisible(false);
	        changeBackButtonBG();
	        nav_home_cus.setStyle("-fx-background-color:  #212832;"); 
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
	    	System.out.println("SUZ1");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchTravel.fxml"));
	    	System.out.println("SUZ2");
	    	
	    	AnchorPane newPanel = loader.load();
	    	// Retrieve the controller associated with the FXML
	    	System.out.println("SUZ3");
	    	
            mainPanel_cus.getChildren().setAll(newPanel);
            
            System.out.println("SUZ4");
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	        System.out.println("SUZ");
	
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
		}
	}
	
	private void updateDashboard() {
		Cus_username.setText(customer.getUsername());
	    Cus_name.setText(customer.getName());
	}
	
	public void exitApplication() {
	    Platform.exit(); 
	}
	
}


/*

package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Model.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerController {
	int currentTab = 1;
	private Customer customer;
	@FXML
	Text Cus_name,Cus_username;
	@FXML
	private AnchorPane mainPanel_cus,selection,home,search; 
	@FXML
	private Button nav_home_cus, nav_notifs_cus, nav_profile_cus, nav_service_cus;
	
	@FXML
	private Text ServiceNo, From, To, Dtime, Atime, StationName, StationLoc, Price;
	@FXML
	private ComboBox<String> TypeInput;
	@FXML
	private TextField FromInput, ToInput;
	@FXML
	private Pane sideInfoPane;

	@FXML
	private VBox servicesCont;
	@FXML
	private ImageView imageViewInfo;
	
	String Des,Arrival,Type;
	boolean infoSideVisibleWas;
	
	@FXML
    public void initialize() {
        // Add items to the ComboBox
        TypeInput.getItems().add("Bus");
        TypeInput.getItems().add("Train");
        TypeInput.getItems().add("Flight");
    }
	
	
	public void displayServices() {
		if(infoSideVisibleWas)
		sideInfoPane.setVisible(true);
	}
	
	
	public void initServicesFS() throws SQLException, ClassNotFoundException {
		
		servicesCont.getChildren().clear();
		
		
	    Connection connection = dbHandler.connect();
	    String query;
	    if (Type.equals("Bus")) {
	        query = "SELECT " +
	                "ts.ServiceID, " +
	                "ts.ServiceProviderID, " +
	                "ts.Description, " +
	                "ts.ServiceType, " +
	                "ts.DepartureTime, " +
	                "ts.ArrivalTime, " +
	                "ts.DepartureLocation, " +
	                "ts.ArrivalLocation, " +
	                "bs.StationName, " +
	                "bs.StationLocation, " +
	                "bs.BusNo, " +
	                "ts.DepartureDate, " +
	                "ts.ArrivalDate, " +
	                "ts.ticketPrice " +  // Correct SQL syntax
	                "FROM TravelService ts " +
	                "JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
	                "WHERE ts.DepartureLocation = ? " +
	                "AND ts.ArrivalLocation = ?;";
	    } else if (Type.equals("Flight")) {
	        query = "SELECT " +
	                "ts.ServiceID, " +
	                "ts.ServiceProviderID, " +
	                "ts.Description, " +
	                "ts.ServiceType, " +
	                "ts.DepartureTime, " +
	                "ts.ArrivalTime, " +
	                "ts.DepartureLocation, " +
	                "ts.ArrivalLocation, " +
	                "fs.AirportName, " +
	                "fs.AirportLocation, " +
	                "fs.FlightNumber, " + // Retrieve flight number
	                "fs.GateNumber, " +
	                "ts.DepartureDate, " +
	                "ts.ArrivalDate, " +
	                "ts.ticketPrice " +
	                "FROM TravelService ts " +
	                "JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
	                "WHERE ts.DepartureLocation = ? " +
	                "AND ts.ArrivalLocation = ?;";
	    } else if (Type.equals("Train")) {
	        query = "SELECT " +
	                "ts.ServiceID, " +
	                "ts.ServiceProviderID, " +
	                "ts.Description, " +
	                "ts.ServiceType, " +
	                "ts.DepartureTime, " +
	                "ts.ArrivalTime, " +
	                "ts.DepartureLocation, " +
	                "ts.ArrivalLocation, " +
	                "bs.StationName, " +
	                "bs.StationLocation, " +
	                "bs.TrainNumber, " + // Retrieve train number
	                "ts.DepartureDate, " +
	                "ts.ArrivalDate, " +
	                "ts.ticketPrice " +
	                "FROM TravelService ts " +
	                "JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
	                "WHERE ts.DepartureLocation = ? " +
	                "AND ts.ArrivalLocation = ?;";
	    }
	    else {
	    	System.out.println("error. servicetype failure");
	    	return;
	    }

	    PreparedStatement prepStatement = connection.prepareStatement(query);
		 // Assuming departureLocation and arrivalLocation are passed into this method
		prepStatement.setString(1, Des); // Set departure location
		prepStatement.setString(2, Arrival); // Set arrival location
	
		ResultSet resultSet = prepStatement.executeQuery();
	
		if (!resultSet.next()) {
			System.out.println("> No Services Found for the specified locations.");
		    return;
		}

	    do {
	        // Store the result set values into local variables
	    	String serviceType = resultSet.getString("ServiceType");
	    	String serviceDesc = resultSet.getString("Description");
	    	String arrivalTime = resultSet.getString("ArrivalTime");
	    	String departureTime = resultSet.getString("DepartureTime");
	    	String departureLocation = resultSet.getString("DepartureLocation");
	    	String arrivalLocation = resultSet.getString("ArrivalLocation");
	    	String price = resultSet.getString("ticketPrice");
	    	String departureDate = resultSet.getString("DepartureDate");
	    	String arrivalDate = resultSet.getString("ArrivalDate");

	        
	        String stationName;
	        String stationLocation;
	        String transportNumber; // Generic field for BusNo, FlightNumber, or TrainNumber

	        if (Type.equals("Flight")) {
	            stationName = resultSet.getString("AirportName");
	            stationLocation = resultSet.getString("AirportLocation");
	            transportNumber = resultSet.getString("FlightNumber"); // Retrieve flight number
	        } else if (Type.equals("Train")) {
	            stationName = resultSet.getString("StationName");
	            stationLocation = resultSet.getString("StationLocation");
	            transportNumber = resultSet.getString("TrainNumber"); // Retrieve train number
	        } else { // For Bus
	            stationName = resultSet.getString("StationName");
	            stationLocation = resultSet.getString("StationLocation");
	            transportNumber = resultSet.getString("BusNo"); // Retrieve bus number
	        }
	        

	        FXMLLoader fxmlloader = new FXMLLoader();
	        fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));

	        try {
	            System.out.println("adding service to View");
	            HBox hbox = fxmlloader.load();
	            ServiceItemController sItemC = fxmlloader.getController();

	            // Pass the captured data to the controller
	            sItemC.setData(arrivalLocation, departureLocation, arrivalTime, departureTime, Type);

	            hbox.setOnMouseClicked(event -> {
	            	showServiceDetails(
	            	        serviceType,
	            	        transportNumber,
	            	        departureLocation,
	            	        arrivalLocation,
	            	        departureTime,
	            	        arrivalTime,
	            	        stationName,
	            	        stationLocation,
	            	        price
	            	    );
	            });

	            servicesCont.getChildren().add(hbox);
	        } catch (IOException io) {
	            System.out.println(io);
	        }
	    } while (resultSet.next());
	    
	}
	
	private void showServiceDetails(
		    String serviceType, 
		    String transportNumber, 
		    String departureLocation,
		    String arrivalLocation, 
		    String departureTime, 
		    String arrivalTime, 
		    String StaName,
		    String StaLOC,
		    String price
		) {

		if(!sideInfoPane.isVisible()) {
			sideInfoPane.setVisible(true);
		}
		ServiceNo.setText(transportNumber);          // Set the service type (service number)
	    From.setText(departureLocation);         // Set the departure location
	    To.setText(arrivalLocation);             // Set the arrival location
	    Dtime.setText(departureTime);            // Set the departure time
	    Atime.setText(arrivalTime);              // Set the arrival time
	    StationName.setText(StaName);        // Set the service description
	    StationLoc.setText(StaLOC);   // Set the station location (can be adjusted)
	    Price.setText(price);
		Image image;
		if(serviceType.equals("Bus")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/bus.png"));
		}else if (serviceType.equals("Train")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/train.png"));
		}else if(serviceType.equals("Flight")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/flight.png"));
		}else {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/hotel.png"));
		}
		
		imageViewInfo.setImage(image);
	}
	public void StartSearch() {
		Des = FromInput.getText(); // Assign text from 'FromInput' to 'Des'
	    Arrival = ToInput.getText(); // Assign text from 'ToInput' to 'Arrival'
	    Type = TypeInput.getValue();
	    try {
			initServicesFS();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	 
	public void setCustomer(Customer customer) {
        this.customer = customer;
        updateDashboard();
	}
	
	 
	public void loadHomePane_cus() {
	    try {
	    	updateDashboard();
	    	if(!home.isVisible()) {
	    		home.setVisible(true);
			}
	    	selection.setVisible(false);
	    	search.setVisible(false);
	        changeBackButtonBG();
	        nav_home_cus.setStyle("-fx-background-color:  #212832;"); 
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
	    	search.setVisible(false);
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadServiceSearch_cus() { //service pane
	    try {
	    	if(!search.isVisible()) {
	    		search.setVisible(true);
			}
	    	home.setVisible(false);
	    	selection.setVisible(false);
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	        System.out.println("SUZ");
	
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
		}
	}
	
	private void updateDashboard() {
		Cus_username.setText(customer.getUsername());
	    Cus_name.setText(customer.getName());
	}
	
	public void exitApplication() {
	    Platform.exit(); 
	}
	
}

*/