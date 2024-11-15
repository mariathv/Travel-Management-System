package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Model.ServiceProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
	
	public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
	}
	
	public void addNewService(){
		addServicePane.setVisible(true);
		viewServicePane.setVisible(false);
		addNewServiceBtn.setVisible(false);
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
		
		
		serviceNumber.setText("Blah");
		
		ResultSet resultSet = prepStatement.executeQuery();
		
		List<String[]> mockData = new ArrayList<>();
		mockData.add(new String[] {"1", "Service 1", "Description 1", "Type 1", "12:00", "Location A", "Location B", "2024-11-14"});
		mockData.add(new String[] {"2", "Service 2", "Description 2", "Type 2", "14:00", "Location C", "Location D", "2024-11-15"});
		mockData.add(new String[] {"3", "Service 3", "Description 3", "Type 3", "16:00", "Location E", "Location F", "2024-11-16"});

//		if(!resultSet.next()) {
//			return;
//		}
//		
//		while(resultSet.next()) {
//			FXMLLoader fxmlloader = new FXMLLoader();
//			fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));
//			
//			try{
//				HBox hbox = fxmlloader.load();
//				ServiceItemController sItemC = fxmlloader.getController(); 
//				sItemC.setData(resultSet.getString(7), resultSet.getString(8),resultSet.getString(6), resultSet.getString(5));
//				servicesCont.getChildren().add(hbox);
//			}catch(IOException io){
//				System.out.println(io);
//			}
//				
//			
//		}

		for (String[] data : mockData) {
		    FXMLLoader fxmlloader = new FXMLLoader();
		    fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));
		
		try {
		    // Mock the loading of a HBox and controller
		    HBox hbox = fxmlloader.load();
		    ServiceItemController sItemC = fxmlloader.getController();
		
		    // Simulate setting the data from the mock data list
		    sItemC.setData(data[5], data[7], data[5], data[6]);
		
		    // Adding the item to the container
		    servicesCont.getChildren().add(hbox);
		} catch (IOException io) {
		    System.out.println(io);
	}
	}
	}
}
