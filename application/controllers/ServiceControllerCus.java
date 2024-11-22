package application.controllers;

import java.awt.ScrollPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Model.Customer;
import application.Model.HotelBooking;
import application.Model.HotelService;
import application.Model.TravelBooking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class ServiceControllerCus {
	@FXML
	private Text ServiceNo, From, To, Dtime, Atime, StationName, StationLoc, Price;
	@FXML
	private ComboBox<String> TypeInput,TypeInput1;
	@FXML
	private TextField FromInput, ToInput;
	@FXML
	private Pane sideInfoPane,hotelServicePane1;
	@FXML
	private AnchorPane SearchPane,ConfirmBookingPane,HotelBookingPane,PaymentTravel,PaymentHotel;
	@FXML
	private Text ServiceNo2, StaName, Staloc, Agency, Dloc, Ddate, Dtime1, Price1,Atime1,ADate,Aloc;
	@FXML
	private VBox servicesCont,vBoxHotel;
	@FXML
	private ImageView imageViewInfo,imageViewInfo1;
	
	String Des,Arrival,Type;
	Customer customer;
	TravelBooking travel_Booking; 
	HotelBooking hotel_booking;
	HotelService hotelservice;
	
	boolean infoSideVisibleWas;
	
	@FXML
	private Text infoHotelName2, infoLocationDetails1, infoBasicPrice1, infoDoublePrice1, infoRatingBox1;
	
	@FXML
    public void initialize() {
        // Add items to the ComboBox
        TypeInput.getItems().add("Bus");
        TypeInput.getItems().add("Train");
        TypeInput.getItems().add("Flight");
        TypeInput1.getItems().add("Basic Room");
        TypeInput1.getItems().add("Double Room");
    }
	
	public ServiceControllerCus() {
        // Initialization code here
    }
	
	public void setCustomer(Customer c) {
		customer = c;
    }
	public void displayServices() {
		if(infoSideVisibleWas)
		sideInfoPane.setVisible(true);
	}
	
	public void ConfirmBooking() {
		if(!ConfirmBookingPane.isVisible())
			ConfirmBookingPane.setVisible(true);
		SearchPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		PaymentTravel.setVisible(false);
		PaymentHotel.setVisible(false);


	}
	
	public void DoPayment() {
		if(!PaymentTravel.isVisible())
			PaymentTravel.setVisible(true);
		SearchPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		PaymentHotel.setVisible(false);
	}
	
	public void DoPayment2() {
		if(!PaymentHotel.isVisible())
			PaymentHotel.setVisible(true);
		SearchPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		PaymentTravel.setVisible(false);
	}
	
	public void BackToSearch() {
		if(!SearchPane.isVisible())
			SearchPane.setVisible(true);
		sideInfoPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		PaymentTravel.setVisible(false);
		PaymentHotel.setVisible(false);
	}
	
	
	public void DoHotelPayment() {
	    Connection conn = null; // Ensure you establish your database connection
	    PreparedStatement pstmt = null;
	    ResultSet resultSet = null;
	    
	    hotel_booking.setListingID(hotelservice.getServiceID());
	    String roomType = TypeInput1.getValue(); // "Single" or "Double" (example inputs)

	    int price = 0; // Default price initialization

	    // Determine the price based on room type
	    if (roomType.equalsIgnoreCase("Basic Room")) {
	        price = hotelservice.getBasicRoomPrice(); // Fetch basic room price from hotelservice
	    } else if (roomType.equalsIgnoreCase("Double Room")) {
	        price = hotelservice.getDoubleRoomPrice(); // Fetch double room price from hotelservice
	    } else {
	        System.out.println("Invalid room type selected!");
	    }

	    // Assign values to the hotel_booking object
	    hotel_booking.setRoomType(roomType);
	    hotel_booking.setPrice(price);
	    

	    try {
	        // 1. Connect to the database
	        conn = dbHandler.connect(); // Replace with your database connection logic

	        // 2. Retrieve the last booking ID
	        String fetchLastIDQuery = "SELECT MAX(bookingID) AS lastID FROM hotelbooking";
	        Statement stmt = conn.createStatement();
	        resultSet = stmt.executeQuery(fetchLastIDQuery);

	        int lastBookingID = 0;
	        if (resultSet.next()) {
	            lastBookingID = resultSet.getInt("lastID");
	        }

	        // 3. Assign new booking ID
	        int newBookingID = lastBookingID + 1;
	        hotel_booking.setBookingID(newBookingID); // `hotel_Booking` is the assumed object of HotelBooking
	        hotel_booking.setStatus(1);
	        // 4. Insert the booking into the database
	        String insertQuery = "INSERT INTO hotelbooking (bookingID, customerID, listingID, price, roomType, bookingDate, username, status) VALUES (?, ?, ?, ?,?, ?, ?, ?)";
	        pstmt = conn.prepareStatement(insertQuery);

	        pstmt.setInt(1, hotel_booking.getBookingID());
	        pstmt.setInt(2, hotel_booking.getCustomerID());
	        pstmt.setInt(3, hotel_booking.getListingID());
	        pstmt.setInt(4, hotel_booking.getPrice());
	        pstmt.setString(5, hotel_booking.getRoomType());
	        pstmt.setString(6, hotel_booking.getBookingDate());
	        pstmt.setString(7, hotel_booking.getUsername());
	        pstmt.setInt(8, hotel_booking.getStatus());

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Hotel booking successfully added to the database!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // 5. Close all resources
	        try {
	            if (resultSet != null) resultSet.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	
	public void DoBooking() {
	    Connection conn = null; // Ensure you establish your database connection
	    PreparedStatement pstmt = null;
	    ResultSet resultSet = null;

	    try {
	        // 1. Connect to the database
	        conn = dbHandler.connect(); // Replace with your database connection logic

	        // 2. Retrieve the last booking ID
	        String fetchLastIDQuery = "SELECT MAX(bookingID) AS lastID FROM travelbooking";
	        Statement stmt = conn.createStatement();
	        resultSet = stmt.executeQuery(fetchLastIDQuery);

	        int lastBookingID = 0;
	        if (resultSet.next()) {
	            lastBookingID = resultSet.getInt("lastID");
	        }

	        // 3. Assign new booking ID
	        int newBookingID = lastBookingID + 1;
	        travel_Booking.setBookingID(newBookingID);
	        travel_Booking.setStatus(1);
	        // 5. Insert the booking into the database
	        String insertQuery = "INSERT INTO travelbooking (bookingID, customerID, serviceID, totalPrice, serviceType, bookingDate, username,status) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
	        pstmt = conn.prepareStatement(insertQuery);

	        pstmt.setInt(1, travel_Booking.getBookingID());
	        pstmt.setInt(2, travel_Booking.getCustomerID());
	        pstmt.setInt(3, travel_Booking.getServiceID());
	        pstmt.setInt(4, travel_Booking.getTotalPrice());
	        pstmt.setString(5, travel_Booking.getServiceType());
	        pstmt.setString(6, travel_Booking.getBookingDate());
	        pstmt.setString(7, travel_Booking.getUsername());
	        pstmt.setInt(8, travel_Booking.getStatus());

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Booking successfully added to the database!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // 6. Close all resources
	        try {
	            if (resultSet != null) resultSet.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	public void BookingDetails(
    String serviceType,
    String transportNumber,
    String departureLocation,
    String arrivalLocation,
    String departureTime,
    String arrivalTime,
    String stationName,
    String stationLocation,
    String price,
    String departureDate,
    String arrivalDate,
    String agency,
    int ServiceID
	) {
	    try {
	        // Set Text values
	        ServiceNo2.setText(transportNumber); // Transport number (e.g., BusNo, FlightNumber, TrainNumber)
	        StaName.setText(stationName);       // Station/Airport name
	        Staloc.setText(stationLocation);    // Station/Airport location
	        Agency.setText(agency);             // Travel agency name
	        Dloc.setText(departureLocation);    // Departure location
	        Ddate.setText(departureDate);       // Departure date
	        Dtime1.setText(departureTime);      // Departure time
	        Price1.setText(price);              // Ticket price
	        Atime1.setText(arrivalTime);        // Arrival time
	        ADate.setText(arrivalDate);         // Arrival date
	        Aloc.setText(arrivalLocation);      // Arrival location
	        
	        travel_Booking = new TravelBooking(
	        		customer.getCustomerID(),
	        		ServiceID,
	        		Integer.parseInt(price),
	        		serviceType,
	        		customer.getUsername()
	        		);
	        
	        
	        // Set Image based on service type
	        Image image;
	        switch (serviceType) {
	            case "Bus":
	                image = new Image(getClass().getResourceAsStream("../assets/images/pngs/bus.png"));
	                break;
	            case "Train":
	                image = new Image(getClass().getResourceAsStream("../assets/images/pngs/train.png"));
	                break;
	            case "Flight":
	                image = new Image(getClass().getResourceAsStream("../assets/images/pngs/flight.png"));
	                break;
	            default:
	                image = new Image(getClass().getResourceAsStream("../assets/images/pngs/hotel.png"));
	        }
	        imageViewInfo1.setImage(image);
	
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle exceptions
	    }
	}
	
	
	public void BookHotelToo() throws ClassNotFoundException, SQLException {
		if(!HotelBookingPane.isVisible())
			HotelBookingPane.setVisible(true);
		SearchPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		PaymentTravel.setVisible(false);
		PaymentHotel.setVisible(false);
		hotelServicePane1.setVisible(false);


		
		vBoxHotel.getChildren().clear();
		hotel_booking = new HotelBooking();
		hotelservice = new HotelService();
		Connection connection = dbHandler.connect();
	    String query;
	    
	        query = "SELECT * FROM hotelservice WHERE city = ?;";
	        PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, Arrival); // Set arrival location
		
			ResultSet resultSet = prepStatement.executeQuery();
		
			if (!resultSet.next()) {
				System.out.println("No Hotel Found");
			    return;
			}
	        int x = 1;
			do {
			    // Fetch values from the ResultSet
			    int hotelServiceID = resultSet.getInt("HotelServiceID");
			    int serviceProviderID = resultSet.getInt("ServiceProviderID");
			    String hotelName = resultSet.getString("HotelName");
			    String hotelLocation = resultSet.getString("HotelLocation");
			    double rating = resultSet.getDouble("Rating");
			    double basicRoomPrice = resultSet.getDouble("BasicRoomPrice");
			    double doubleRoomPrice = resultSet.getDouble("DoubleRoomPrice");
			    String Hotelcity = resultSet.getString("city"); // Assuming the city field exists in the table.
			    

			    infoHotelName2.setText(hotelName); // Hotel name in detailed view
			    infoLocationDetails1.setText(hotelLocation); // City
			    infoBasicPrice1.setText(String.valueOf(basicRoomPrice)); 
			    infoDoublePrice1.setText(String.valueOf(doubleRoomPrice)); 
			    infoRatingBox1.setText(String.format("%.1f ★", rating)); // Rating
			    
			    FXMLLoader fxmlloader = new FXMLLoader();
		        fxmlloader.setLocation(getClass().getResource("../scenes/components/AvaliableHotel_item.fxml"));

		        try {
		            System.out.println("Hotel Details");
		            HBox hbox = fxmlloader.load();
		            HotelItemController h1 = fxmlloader.getController();

		            // Pass the captured data to the controller
		            h1.setData(x,hotelName,Hotelcity,rating);
		            hbox.setOnMouseClicked(event -> {
		            	if(!hotelServicePane1.isVisible()) {
		            		hotelServicePane1.setVisible(true);
		            	}
		            		infoHotelName2.setText(hotelName); // Hotel name in detailed view
		      			    infoLocationDetails1.setText(hotelLocation); // City
		      			    infoBasicPrice1.setText(String.valueOf(basicRoomPrice)); 
		      			    infoDoublePrice1.setText(String.valueOf(doubleRoomPrice)); 
		      			    infoRatingBox1.setText(String.format("%.1f ★", rating)); // Rating
		      			
		      			hotel_booking.setCustomerID(customer.getCustomerID());      // Set customer ID
		      			hotel_booking.setUsername(customer.getUsername());
		      			
		      			hotelservice.setServiceID(hotelServiceID);
		      			hotelservice.setBasicRoomPrice((int) basicRoomPrice); // Cast double to int before setting
		      			hotelservice.setCity(Hotelcity);
		      			hotelservice.setHotelLocation(hotelLocation);
		      			hotelservice.setServiceProviderID(serviceProviderID);
		      			hotelservice.setDoubleRoomPrice((int) doubleRoomPrice); // Cast double to int before setting
		      			hotelservice.setHotelName(hotelName); // Add this if HotelName also needs to be set
		      			hotelservice.setRating((int) rating); // Add this if the rating should also be assigned
		        		
		            });

		            vBoxHotel.getChildren().add(hbox);
		        } catch (IOException io) {
		            io.printStackTrace();
		        }
		        x++;
			} while (resultSet.next());
	        
	        
	        
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
	                "ts.ticketPrice, " +
	                "sp.travelAgencyName " +  // Added travelAgencyName
	                "FROM TravelService ts " +
	                "JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
	                "JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with ServiceProvider
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
	                "fs.FlightNumber, " +
	                "fs.GateNumber, " +
	                "ts.DepartureDate, " +
	                "ts.ArrivalDate, " +
	                "ts.ticketPrice, " +
	                "sp.travelAgencyName " +  // Added travelAgencyName
	                "FROM TravelService ts " +
	                "JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
	                "JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with ServiceProvider
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
	                "bs.TrainNumber, " +
	                "ts.DepartureDate, " +
	                "ts.ArrivalDate, " +
	                "ts.ticketPrice, " +
	                "sp.travelAgencyName " +  // Added travelAgencyName
	                "FROM TravelService ts " +
	                "JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
	                "JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with ServiceProvider
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
	    	int serviceId = resultSet.getInt("ServiceID");
	    	String arrivalTime = resultSet.getString("ArrivalTime");
	    	String departureTime = resultSet.getString("DepartureTime");
	    	String departureLocation = resultSet.getString("DepartureLocation");
	    	String arrivalLocation = resultSet.getString("ArrivalLocation");
	    	String price = resultSet.getString("ticketPrice");
	    	String departureDate = resultSet.getString("DepartureDate");
	    	String arrivalDate = resultSet.getString("ArrivalDate");
	    	String agency = resultSet.getString("travelAgencyName");
	    	int ServiceID = resultSet.getInt("ServiceID");
	        
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
	            sItemC.setData(arrivalLocation, departureLocation, arrivalTime, departureTime, Type, ServiceID);

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
	            	// Create a new method to pass the data to BookingDetails
	            	BookingDetails(
	                        serviceType,
	                        transportNumber,
	                        departureLocation,
	                        arrivalLocation,
	                        departureTime,
	                        arrivalTime,
	                        stationName,
	                        stationLocation,
	                        price,
	                        departureDate,
	                        arrivalDate,
	                        agency,
	                        serviceId
	                        
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
}
