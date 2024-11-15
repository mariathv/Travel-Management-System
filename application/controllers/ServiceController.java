package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Model.ServiceProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServiceController {
	ServiceProvider serviceProvider;
	@FXML
	private Pane addServicePane;
	@FXML
	private ScrollPane viewServicePane;
	@FXML
	private Text serviceNumber;
	@FXML
	private VBox servicesCont;
	@FXML
	private Button addNewServiceBtn;
	@FXML
	private TextField depLoc, depTime, depDate, arvLoc, arvTime, arvDate, SBusNo, BStationName, BStationLoc, StktPrice;
	public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
	}
	
	public void addNewServiceForm() {
		addServicePane.setVisible(true);
		viewServicePane.setVisible(false);
		addNewServiceBtn.setVisible(false);
		
	}
	
	public void addNewService() throws SQLException, ClassNotFoundException{

		//add new service logic and db handling
		if (depLoc.getText().isEmpty() || 
			    depTime.getText().isEmpty() || 
			    depDate.getText().isEmpty() || 
			    arvLoc.getText().isEmpty() || 
			    arvTime.getText().isEmpty() || 
			    arvDate.getText().isEmpty() || 
			    SBusNo.getText().isEmpty() || 
			    BStationName.getText().isEmpty() || 
			    BStationLoc.getText().isEmpty() || 
			    StktPrice.getText().isEmpty()) {
			    System.out.println("One or more fields are empty. Please fill all fields.");
			    return;
		}
		
		String depLoc = this.depLoc.getText(); // Departure Location
		String depTime = this.depTime.getText(); // Departure Time
		String depDate = this.depDate.getText(); // Departure Date
		String arvLoc = this.arvLoc.getText(); // Arrival Location
		String arvTime = this.arvTime.getText(); // Arrival Time
		String arvDate = this.arvDate.getText(); // Arrival Date
		String SBusNo = this.SBusNo.getText(); // Bus Number
		String BStationName = this.BStationName.getText(); // Bus Station Name
		String BStationLoc = this.BStationLoc.getText(); // Bus Station Location
		String StktPrice = this.StktPrice.getText(); // Ticket Price

		String description = String.format(
		    "Travel Service: Bus No. %s from %s to %s departing on %s at %s and arriving on %s at %s. Ticket Price: PKR %s. Bus station: %s located at %s.",
		    SBusNo, depLoc, arvLoc, depDate, depTime, arvDate, arvTime, StktPrice, BStationName, BStationLoc
		);
		
		Connection connection = dbHandler.connect();
		
		String insertQuery = "INSERT INTO TravelService(serviceProviderID, description, serviceType, arrivalTime, departureTime, arrivalLocation, departureLocation, departureDate, arrivalDate, ticketPrice) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		
		prepStatement.setInt(1, serviceProvider.getServiceProviderID());
		prepStatement.setString(2, description);
		prepStatement.setString(3, "Bus");
		prepStatement.setString(4, arvTime);
		prepStatement.setString(5, depTime);
		prepStatement.setString(6, arvLoc);
		prepStatement.setString(7, depLoc);
		prepStatement.setString(8, depDate);
		prepStatement.setString(9, arvDate);
		prepStatement.setInt(10, Integer.parseInt(StktPrice));
		
		int serviceID = -1;
		
		System.out.println("Adding new service > Executing Statement");
		int affectedRows = prepStatement.executeUpdate();
		
		if (affectedRows > 0) {
			
			ResultSet generatedKeys = prepStatement.getGeneratedKeys();
		    
		    if (generatedKeys.next()) {
		        serviceID = generatedKeys.getInt(1); 
		    }else {
		    	System.out.println("Key generation error");
		    	return;
		    }
		    prepStatement.close();
		    
		    String insertQuery1 = "INSERT INTO BusService(ServiceID, StationName, StationLocation, BusNo) VALUES (?,?,?,?)";
			
			PreparedStatement prepStatement1 = connection.prepareStatement(insertQuery1);
			
			prepStatement1.setInt(1, serviceID);
			prepStatement1.setString(2, BStationName);
			prepStatement1.setString(3, BStationLoc);
			prepStatement1.setString(4, SBusNo);
			
			int affectedRows1 = prepStatement1.executeUpdate();
			
			if (affectedRows1 > 0) {
				System.out.println("Adding new service > Successfully Added New Service");
				initServicesFS();
				addServicePane.setVisible(false);
				viewServicePane.setVisible(true);
				addNewServiceBtn.setVisible(true);
			}else {
				System.out.println("Adding new service > Failure Adding New Service > 2");
			}
		} else {
			System.out.println("Adding new service > Failure Adding New Service > 1");
		    prepStatement.close();
		    connection.close();
		    return;
		}
		
		
	}
	public void initServicesFS() throws SQLException, ClassNotFoundException { //initialize services for service provider ONLY not for the customer interface
		Connection connection = dbHandler.connect();
		
		String query = "SELECT " +
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
               "bs.BusNo " +
               "FROM TravelService ts " +
               "JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
               "WHERE ts.ServiceProviderID = ?;";

		PreparedStatement prepStatement = connection.prepareStatement(query);
		prepStatement.setInt(1, serviceProvider.getServiceProviderID());
		
		
		//serviceNumber.setText("Blah");
		
		ResultSet resultSet = prepStatement.executeQuery();
		
		
//		List<String[]> mockData = new ArrayList<>();
//		mockData.add(new String[] {"1", "Service 1", "Description 1", "Type 1", "12:00", "Location A", "Location B", "2024-11-14"});
//		mockData.add(new String[] {"2", "Service 2", "Description 2", "Type 2", "14:00", "Location C", "Location D", "2024-11-15"});
//		mockData.add(new String[] {"3", "Service 3", "Description 3", "Type 3", "16:00", "Location E", "Location F", "2024-11-16"});

		if(!resultSet.next()) {
			System.out.println("> No Services Found for current user" + serviceProvider.getServiceProviderID());
			return;
		}
		
		do {
		    FXMLLoader fxmlloader = new FXMLLoader();
		    fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));
		    
		    try {
		        System.out.println("adding service to View");
		        HBox hbox = fxmlloader.load();
		        ServiceItemController sItemC = fxmlloader.getController(); 
		        sItemC.setData(resultSet.getString(8), resultSet.getString(7), resultSet.getString(6), resultSet.getString(5));
		        
//		        hbox.setOnMouseClicked(event -> {
//	                showServiceDetails(resultSet.getString(4), resultSet.getString(11), resultSet.getString(3));
//	            });
		        
		        servicesCont.getChildren().add(hbox);
		    } catch (IOException io) {
		        System.out.println(io);
		    }
		} while (resultSet.next());

//		for (String[] data : mockData) {
//		    FXMLLoader fxmlloader = new FXMLLoader();
//		    fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));
//		
//		try {
//		    // Mock the loading of a HBox and controller
//		    HBox hbox = fxmlloader.load();
//		    ServiceItemController sItemC = fxmlloader.getController();
//		
//		    // Simulate setting the data from the mock data list
//		    sItemC.setData(data[5], data[7], data[5], data[6]);
//		
//		    // Adding the item to the container
//		    servicesCont.getChildren().add(hbox);
//		} catch (IOException io) {
//		    System.out.println(io);
//	}
//	}
	}
	
//	private void showServiceDetails(String serviceType, String BUSNO, String Desc, String arrivalTime) {
//		
//	}
}