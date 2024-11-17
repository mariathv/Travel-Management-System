package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Model.BusService;
import application.Model.FlightService;
import application.Model.HotelService;
import application.Model.ServiceProvider;
import application.Model.TrainService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServiceController {
	ServiceProvider serviceProvider;
	@FXML
	private Pane addServicePane, sideInfoPane, addHotelListingPane;
	@FXML
	private ScrollPane viewServicePane;
	@FXML
	private Text serviceNumber, PlatformNameTxt, PlatformLocTxt;
	@FXML
	private VBox servicesCont;
	@FXML
	private HBox GateInfo;
	@FXML
	private Button addNewServiceBtn;
	@FXML
	private TextField depLoc, depTime, depDate, arvLoc, arvTime, arvDate, SBusNo, BStationName, BStationLoc, StktPrice, GNumber;
	@FXML
	private Text infoServiceType, infoBusNo, depLocInfo,arvLocInfo; //dynamically updated info upon mouse click
	@FXML
	private FontAwesomeIcon goBackView;
	@FXML
	private Text critBusNo;
	@FXML
	private ImageView imageViewInfo;
	@FXML
	private TextField HotelName, HotelLocation, BasicPrice, DoublePrice, City;
	
	boolean infoSideVisibleWas;
	
	public ArrayList<BusService> BusServices = new ArrayList<>();
	public ArrayList<FlightService> FlightServices = new ArrayList<>();
	public ArrayList<TrainService> TrainServices = new ArrayList<>();
	public ArrayList<HotelService> HotelServices = new ArrayList<>();
	    
	
	public void clearAllServices() {
		  BusServices.clear();
		    FlightServices.clear();
		    TrainServices.clear();
		    HotelServices.clear();
	}
	public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
	}
	
	public void addNewServiceForm() {
		if(serviceProvider.getServiceType().equals("Hotel")) {
			addHotelListingPane.setVisible(true);
			addServicePane.setVisible(false);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			goBackView.setVisible(true);
			if(sideInfoPane.isVisible()) {
				infoSideVisibleWas=true;
			}else
				infoSideVisibleWas=false;
			sideInfoPane.setVisible(false);
			
		}else {
			if(serviceProvider.getServiceType().equals("Train")) {
				critBusNo.setText("Train Number:");
				SBusNo.setPromptText("TRAIN NO");
			} else if (serviceProvider.getServiceType().equals("Flight")) {
				GateInfo.setVisible(true);
				SBusNo.setPromptText("FLIGHT NO");
				critBusNo.setText("Flight Number:");
				PlatformNameTxt.setText(" Airport Name");
				PlatformLocTxt.setText(" Airport Location");
			}
			
			
			
			addServicePane.setVisible(true);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			goBackView.setVisible(true);
			if(sideInfoPane.isVisible()) {
				infoSideVisibleWas=true;
			}else
				infoSideVisibleWas=false;
			sideInfoPane.setVisible(false);
		}
		
	}
	
	
	public void displayServices() {
		addServicePane.setVisible(false);
		addHotelListingPane.setVisible(false);
		viewServicePane.setVisible(true);
		addNewServiceBtn.setVisible(true);
		goBackView.setVisible(false);
		if(infoSideVisibleWas)
		sideInfoPane.setVisible(true);
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
		String GNumber = this.GNumber.getText();

		String description = String.format(
		    "Bus No. %s from %s to %s departing on %s at %s and arriving on %s at %s. \nTicket Price: PKR %s. \nBus station: %s located at %s.",
		    SBusNo, depLoc, arvLoc, depDate, depTime, arvDate, arvTime, StktPrice, BStationName, BStationLoc
		);
		
		Connection connection = dbHandler.connect();
		
		String insertQuery = "INSERT INTO TravelService(serviceProviderID, description, serviceType, arrivalTime, departureTime, arrivalLocation, departureLocation, departureDate, arrivalDate, ticketPrice) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		
		prepStatement.setInt(1, serviceProvider.getServiceProviderID());
		prepStatement.setString(2, description);
		if(serviceProvider.getServiceType().equals("Bus"))
			prepStatement.setString(3, "Bus");
		else if(serviceProvider.getServiceType().equals("Train"))
			prepStatement.setString(3, "Train");
		else
			prepStatement.setString(3, "Flight");
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
		    
		    String insertQuery1;
		    if(serviceProvider.getServiceType().equals("Bus")){
		       insertQuery1 = "INSERT INTO BusService(ServiceID, StationName, StationLocation, BusNo) VALUES (?,?,?,?)";
		    } else if(serviceProvider.getServiceType().equals("Train")) {
		    	 insertQuery1 = "INSERT INTO TrainService(ServiceID, StationName, StationLocation, TrainNumber) VALUES (?,?,?,?)";
		    }else {
		    	 insertQuery1 = "INSERT INTO FlightService(ServiceID, AirportName, AirportLocation, FlightNumber, GateNumber) VALUES (?,?,?,?,?)";
		    }
		    
			PreparedStatement prepStatement1 = connection.prepareStatement(insertQuery1);
			
			prepStatement1.setInt(1, serviceID);
			prepStatement1.setString(2, BStationName);
			prepStatement1.setString(3, BStationLoc);
			prepStatement1.setString(4, SBusNo);
			
			if(serviceProvider.getServiceType().equals("Flight")) {
				prepStatement1.setString(5, GNumber);
			}
			
			int affectedRows1 = prepStatement1.executeUpdate();
			
			if (affectedRows1 > 0) {
				System.out.println("Adding new service > Successfully Added New Service");
				initServicesFS();
				displayServices();
				
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
	
	public void addNewHotelListing() throws SQLException, ClassNotFoundException{
		
		//add new service logic and db handling
		if (HotelName.getText().isEmpty() || 
			    HotelLocation.getText().isEmpty() || 
			    BasicPrice.getText().isEmpty() || 
			    DoublePrice.getText().isEmpty()) {
			    System.out.println("One or more fields are empty. Please fill all fields.");
			    return;
		}
		
		String HotelName = this.HotelName.getText();
		String HotelLocation = this.HotelLocation.getText();
		int BasicPrice = Integer.parseInt(this.BasicPrice.getText());
		int DoublePrice = Integer.parseInt(this.DoublePrice.getText());
		String City = this.City.getText();

		
		Connection connection = dbHandler.connect();
		
		String insertQuery = "INSERT INTO HotelService(serviceProviderID, HotelName, HotelLocation, Rating, BasicRoomPrice, DoubleRoomPrice, city) VALUES (?,?,?,?,?,?,?)";
		
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		
		prepStatement.setInt(1, serviceProvider.getServiceProviderID());
		prepStatement.setString(2, HotelName);
		prepStatement.setString(3, HotelLocation);
		prepStatement.setInt(4, 1);
		prepStatement.setInt(5, BasicPrice);
		prepStatement.setInt(6, DoublePrice);
		prepStatement.setString(7, City);
		
		int HotelID = -1;
		
		System.out.println("Adding new service > Executing Statement");
		int affectedRows = prepStatement.executeUpdate();
		
		if (affectedRows > 0) {
			
			ResultSet generatedKeys = prepStatement.getGeneratedKeys();
		    
		    if (generatedKeys.next()) {
		    	HotelID = generatedKeys.getInt(1); 
		    }else {
		    	System.out.println("Key generation error");
		    	return;
		    }
		    prepStatement.close();
		    
		   
			System.out.println("Adding new service > Successfully Added New Service");
			initServicesFS();
			displayServices();
			
		} else {
			System.out.println("Adding new service > Failure Adding New Service > 1");
		    prepStatement.close();
		    connection.close();
		    return;
		}
		
		
	}
	public void initServicesFS() throws SQLException, ClassNotFoundException {
		
		//clearing prev fetched data
		clearAllServices();
		addNewServiceBtn.setText("Add Hotel Listing");
		servicesCont.getChildren().clear();
		
		
	    Connection connection = dbHandler.connect();
	    String query;
	    if(serviceProvider.getServiceType().equals("Bus"))
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
	                   "ts.ArrivalDate " +
	                   "FROM TravelService ts " +
	                   "JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
	                   "WHERE ts.ServiceProviderID = ?;";
	    else if(serviceProvider.getServiceType().equals("Flight")) {
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
	                   "fs.FlightNumber, " +
	                   "fs.GateNumber, " +
	                   "ts.DepartureDate, " +
	                   "ts.ArrivalDate " +
	                   "FROM TravelService ts " +
	                   "JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
	                   "WHERE ts.ServiceProviderID = ?;";
	    }else if(serviceProvider.getServiceType().equals("Train")) {
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
	                   "bs.TrainNumber, " +
	                   "ts.DepartureDate, " +
	                   "ts.ArrivalDate " +
	                   "FROM TravelService ts " +
	                   "JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
	                   "WHERE ts.ServiceProviderID = ?;";
	    }else if(serviceProvider.getServiceType().equals("Hotel")) {
	    	query = "SELECT " +
	                   "HotelServiceID," +
	                   "ServiceProviderID, " +
	                   "HotelName," +
	                   "HotelLocation," +
	                   "Rating, " +
	                   "BasicRoomPrice, " +
	                   "DoubleRoomPrice, " +
	                   "city " +
	                   "FROM HotelService " +
	                   "WHERE ServiceProviderID = ?;";
	    }
	    else {
	    	System.out.println("error. servicetype failure");
	    	return;
	    }

	    PreparedStatement prepStatement = connection.prepareStatement(query);
	    prepStatement.setInt(1, serviceProvider.getServiceProviderID());

	    ResultSet resultSet = prepStatement.executeQuery();

	    if (!resultSet.next()) {
	        System.out.println("> No Services Found for current user" + serviceProvider.getServiceProviderID());
	        return;
	    }

	    do {
	        // Store the result set values into local variables
	    	if(!serviceProvider.getServiceType().equals("Hotel")) {
		        String serviceType = resultSet.getString(4);
		        String busNo = resultSet.getString(11);
		        String serviceDesc = resultSet.getString(3);
		        String arrivalTime = resultSet.getString(6);
		        String departureTime = resultSet.getString(5);
		        String departureLocation = resultSet.getString(7);
		        String arrivalLocation = resultSet.getString(8);
		        String departureDate ;
		        String arrivalDate ;
		        if(serviceProvider.getServiceType().equals("Flight")) {
		        	departureDate = resultSet.getString(13);
		        	arrivalDate = resultSet.getString(14);
		        }else {
		        	departureDate = resultSet.getString(12);
		        	arrivalDate = resultSet.getString(13);
		        }
		        
		        System.out.println(serviceProvider.getServiceType() + " is the service type of SP");
		        if(serviceProvider.getServiceType().equals("Bus")) {
		        	BusService busService = new BusService(
		        	    resultSet.getString(9),          // stationName
		        	    resultSet.getString(10),         // stationLocation
		        	    busNo,                // BusNumber
		        	    resultSet.getInt(1),  // serviceID
		        	    resultSet.getInt(2),  // serviceProviderID
		        	    serviceDesc,          // description
		        	    serviceType,          // serviceType
		        	    departureTime,        // departureTime
		        	    arrivalTime,          // arrivalTime
		        	    departureLocation,    // departureLocation
		        	    arrivalLocation,      // arrivalLocation
		        	    departureDate,          // serviceDate
		        	    arrivalDate
		        	);
		        	BusServices.add(busService);
		        }else if(serviceProvider.getServiceType().equals("Train")) {
		        	TrainService trainService = new TrainService(
			        	    resultSet.getString(9),          // stationName
			        	    resultSet.getString(10),         // stationLocation
			        	    busNo,                // TrainNumber
			        	    resultSet.getInt(1),  // serviceID
			        	    resultSet.getInt(2),  // serviceProviderID
			        	    serviceDesc,          // description
			        	    serviceType,          // serviceType
			        	    departureTime,        // departureTime
			        	    arrivalTime,          // arrivalTime
			        	    departureLocation,    // departureLocation
			        	    arrivalLocation,      // arrivalLocation
			        	    departureDate,          // serviceDate
			        	    arrivalDate
			        	);
			        	TrainServices.add(trainService);
		        }else if (serviceProvider.getServiceType().equals("Flight")){
		        	FlightService flightService = new FlightService(
			        	    resultSet.getString(9),          // stationName
			        	    resultSet.getString(10),         // stationLocation
			        	    busNo,                // FlightNumber
			        	    resultSet.getInt(1),  // serviceID
			        	    resultSet.getInt(2),  // serviceProviderID
			        	    serviceDesc,          // description
			        	    serviceType,          // serviceType
			        	    departureTime,        // departureTime
			        	    arrivalTime,          // arrivalTime
			        	    departureLocation,    // departureLocation
			        	    arrivalLocation,      // arrivalLocation
			        	    departureDate,          // serviceDate
			        	    arrivalDate,
			        	    resultSet.getString(10)
			        	    
			        	);
		        		System.out.println("added flight service object");
			        	FlightServices.add(flightService);
		        }
	    	

		        FXMLLoader fxmlloader = new FXMLLoader();
		        fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));
	
		        try {
		            System.out.println("adding service to View");
		            HBox hbox = fxmlloader.load();
		            ServiceItemController sItemC = fxmlloader.getController();
	
		            // Pass the captured data to the controller
		            sItemC.setData(arrivalLocation, departureLocation, arrivalTime, departureTime, serviceProvider.getServiceType());
	
		            hbox.setOnMouseClicked(event -> {
		                showServiceDetails(serviceType, busNo, arrivalLocation, departureLocation);
		            });
	
		            servicesCont.getChildren().add(hbox);
		        } catch (IOException io) {
		            System.out.println(io);
		        }
	    	}else {
	    		int HotelServiceID = resultSet.getInt(1);
	    		int ServiceProviderID = resultSet.getInt(2);
		        String HotelName = resultSet.getString(3);
		        String HotelLocation = resultSet.getString(4);
		        int rating = resultSet.getInt(5);
		        int BasicRoomPrice = resultSet.getInt(6);
		        int DoubleRoomPrice = resultSet.getInt(7);
		        String city = resultSet.getString(8);
		        
		        HotelService hotelService = new HotelService(
		        		HotelServiceID, ServiceProviderID, HotelName, HotelLocation, rating, BasicRoomPrice, DoubleRoomPrice,city
		        	);
		        HotelServices.add(hotelService);
		        FXMLLoader fxmlloader = new FXMLLoader();
		        System.out.println("loading fxml");
		        fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item_hotel.fxml"));

		        try {
		            System.out.println("loading hbox");
		            HBox hbox = fxmlloader.load();
		            System.out.println("getting controller");
		            HotelServiceItemController sHItemC = fxmlloader.getController();
		            System.out.println("setting data");
		            // Pass the captured data to the controller
		            sHItemC.setData(HotelName, HotelLocation, BasicRoomPrice, DoubleRoomPrice, rating);
	
//		            hbox.setOnMouseClicked(event -> {
//		                showServiceDetails(serviceType, busNo, arrivalLocation, departureLocation);
//		            });
		            System.out.println("addiing");
		            servicesCont.getChildren().add(hbox);
		        } catch (IOException io) {
		        	 System.out.println("Error loading FXML: " + io.getMessage());
		        }
		        
	    	}
	    } while (resultSet.next());
	}

	
	private void showServiceDetails(String serviceType, String Number, String arrivalLoc, String depLoc) {

		serviceProvider.printDetails();
		if(!sideInfoPane.isVisible()) {
			sideInfoPane.setVisible(true);
		}
		depLocInfo.setText(depLoc);
		arvLocInfo.setText(arrivalLoc);
		infoServiceType.setText(serviceType);
		infoBusNo.setText(Number);
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

}