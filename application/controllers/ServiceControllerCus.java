package application.controllers;

import java.awt.ScrollPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.Managers.ServiceManager;
import application.Model.BusService;
import application.Model.Customer;
import application.Model.FlightService;
import application.Model.HotelBooking;
import application.Model.HotelService;
import application.Model.TrainService;
import application.Model.TravelBooking;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
	private Text ServiceNo, From, To, Dtime, Atime, StationName, StationLoc, Price, errText, errHotel, hotelPrice;
	@FXML
	private ComboBox<String> TypeInput, TypeInput1;
	@FXML
	private TextField FromInput, ToInput;
	@FXML
	private Pane sideInfoPane, hotelServicePane1, hotelPaymentPane;
	@FXML
	private AnchorPane SearchPane, ConfirmBookingPane, HotelBookingPane;
	@FXML
	private Text ServiceNo2, StaName, Staloc, Agency, Dloc, Ddate, Dtime1, Price1, Atime1, ADate, Aloc, agencyName,
			sampleText, typeNo;
	@FXML
	private VBox servicesCont, vBoxHotel;
	@FXML
	private FontAwesomeIcon imageViewInfo;

	@FXML
	private TextField cnField, mmField, yyField, cvvField, fnField, lnField;

	@FXML
	private TextField cnField1, mmField1, yyField1, cvvField1, fnField1, lnField1;

	String Des, Arrival, Type;
	Customer customer;
	TravelBooking travel_Booking;
	HotelBooking hotel_booking;
	HotelService hotelservice;

	boolean infoSideVisibleWas;

	ServiceManager serviceManager = new ServiceManager();

	@FXML
	private Text infoHotelName2, infoLocationDetails1, infoBasicPrice1, infoDoublePrice1, infoRatingBox1;

	List<BusService> busServices = new ArrayList<>();
	List<TrainService> trainServices = new ArrayList<>();
	List<FlightService> flightServices = new ArrayList<>();

	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {

		setupValidation(cnField);
		setupValidation(mmField);
		setupValidation(yyField);
		setupValidation(cvvField);
		setupValidation(fnField);
		setupValidation(lnField);

		// Add items to the ComboBox
		TypeInput.getItems().add("All");
		TypeInput.getItems().add("Bus");
		TypeInput.getItems().add("Train");
		TypeInput.getItems().add("Flight");
		TypeInput1.getItems().add("Basic Room");
		TypeInput1.getItems().add("Double Room");

		SearchPane.setVisible(true);
		ConfirmBookingPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		hotelServicePane1.setVisible(true);
		hotelPaymentPane.setVisible(false);

		// hide stuff
		sideInfoPane.setVisible(false);
		getAllServices();
	}

	private void clearAllTextFields() {
		cnField.clear();
		mmField.clear();
		yyField.clear();
		cvvField.clear();
		fnField.clear();
		lnField.clear();
		cnField1.clear();
		mmField1.clear();
		yyField1.clear();
		cvvField1.clear();
		fnField1.clear();
		lnField1.clear();
	}

	public void gobackServices() {
		SearchPane.setVisible(true);
		ConfirmBookingPane.setVisible(false);
		HotelBookingPane.setVisible(false);
		hotelServicePane1.setVisible(true);
		hotelPaymentPane.setVisible(false);

		clearAllTextFields();
	}

	public ServiceControllerCus() {
		// Initialization code here
	}

	public void setCustomer(Customer c) {
		customer = c;
	}

	public void displayServices() {
		if (infoSideVisibleWas)
			sideInfoPane.setVisible(true);
	}

	public void ConfirmBooking() {
		if (!ConfirmBookingPane.isVisible())
			ConfirmBookingPane.setVisible(true);
		SearchPane.setVisible(false);
		HotelBookingPane.setVisible(false);

	}

	public void DoPayment2() {

		if (TypeInput1 == null || TypeInput1.getValue() == null || TypeInput1.getValue().isBlank()) {
			errHotel.setText("* Select a type");
			return;
		}
		String typev = TypeInput1.getValue();
		hotelServicePane1.setVisible(false);
		hotelPaymentPane.setVisible(true);
		if (typev.equals("Basic Room")) {
			hotelPrice.setText("" + hotelservice.getBasicRoomPrice());
		} else {
			hotelPrice.setText("" + hotelservice.getDoubleRoomPrice());
		}

	}

	public void viewServiceInfo() {
		hotelServicePane1.setVisible(true);
		hotelPaymentPane.setVisible(false);
	}

	public void BackToSearch() {
		if (!SearchPane.isVisible())
			SearchPane.setVisible(true);
		sideInfoPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		HotelBookingPane.setVisible(false);
	}

	public void DoHotelPayment() {

		if (!validateAllFieldsForHotelPayment()) {
			return;
		}
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
			// System.out.println("adding hotel " + hotel_booking.getListingID() +
			// hotel_booking.getStatus());
			pstmt.setInt(3, hotel_booking.getListingID());
			pstmt.setInt(4, hotel_booking.getPrice());
			pstmt.setString(5, hotel_booking.getRoomType());
			pstmt.setString(6, hotel_booking.getBookingDate());
			pstmt.setString(7, hotel_booking.getUsername());
			pstmt.setInt(8, hotel_booking.getStatus());

			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Hotel booking successfully added to the database!");
				gobackServices();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. Close all resources
			try {
				if (resultSet != null)
					resultSet.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void setupValidation(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isEmpty()) {
				textField.getStyleClass().removeAll("text-field-error");
			}
		});
	}

	private boolean validateField(TextField textField) {
		if (textField.getText() == null || textField.getText().isEmpty()) {
			if (!textField.getStyleClass().contains("text-field-error")) {
				textField.getStyleClass().add("text-field-error");
			}
			return false;
		}
		return true;
	}

	public boolean validateAllFieldsForTravelPayment() {
		boolean isValid = true;

		isValid &= validateField(cnField); // Card number field
		isValid &= validateField(mmField); // Month field
		isValid &= validateField(yyField); // Year field
		isValid &= validateField(cvvField); // CVV field
		isValid &= validateField(fnField); // First name field
		isValid &= validateField(lnField); // Last name field

		return isValid;
	}

	public boolean validateAllFieldsForHotelPayment() {
		boolean isValid = true;

		isValid &= validateField(cnField1); // Card number field
		isValid &= validateField(mmField1); // Month field
		isValid &= validateField(yyField1); // Year field
		isValid &= validateField(cvvField1); // CVV field
		isValid &= validateField(fnField1); // First name field
		isValid &= validateField(lnField1); // Last name field

		return isValid;
	}

	public void DoBooking() {
		System.out.println("booking " + cnField.getText());
		if (!validateAllFieldsForTravelPayment()) {
			return;
		}

		Arrival = Aloc.getText();
		Connection conn = null; // Ensure you establish your database connection
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			conn = dbHandler.connect();

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
			System.out.println(travel_Booking.getServiceID());
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
				HotelBookingPane.setVisible(true);
				ConfirmBookingPane.setVisible(false);
				BookHotelToo();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 6. Close all resources
			try {
				if (resultSet != null)
					resultSet.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
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
			int ServiceID) {
		try {
			// Set Text values
			ServiceNo2.setText(transportNumber); // Transport number (e.g., BusNo, FlightNumber, TrainNumber)
			StaName.setText(stationName); // Station/Airport name
			Staloc.setText(stationLocation); // Station/Airport location
			Agency.setText(agency); // Travel agency name
			Dloc.setText(departureLocation); // Departure location
			Ddate.setText(departureDate); // Departure date
			Dtime1.setText(departureTime); // Departure time
			Price1.setText(price); // Ticket price
			Atime1.setText(arrivalTime); // Arrival time
			ADate.setText(arrivalDate); // Arrival date
			Aloc.setText(arrivalLocation); // Arrival location

			travel_Booking = new TravelBooking(
					customer.getCustomerID(),
					ServiceID,
					Integer.parseInt(price),
					serviceType,
					customer.getUsername());

			// Set Image based on service type
			switch (serviceType) {
				case "Bus":
					typeNo.setText("Bus Number: ");
					break;
				case "Train":
					typeNo.setText("Train Number: ");
					break;
				case "Flight":
					typeNo.setText("Flight Number: ");
					break;
			}

		} catch (Exception e) {
			e.printStackTrace(); // Handle exceptions
		}
	}

	public void BookHotelToo() throws ClassNotFoundException, SQLException {
		if (!HotelBookingPane.isVisible())
			HotelBookingPane.setVisible(true);
		SearchPane.setVisible(false);
		ConfirmBookingPane.setVisible(false);
		hotelServicePane1.setVisible(false);

		vBoxHotel.getChildren().clear();
		hotel_booking = new HotelBooking();
		hotelservice = new HotelService();
		Connection connection = dbHandler.connect();
		String query;
		System.out.println("fetching hotels for " + Arrival);
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
				h1.setData(x, hotelName, Hotelcity, rating);
				hbox.setOnMouseClicked(event -> {
					if (!hotelServicePane1.isVisible()) {
						hotelServicePane1.setVisible(true);
					}
					infoHotelName2.setText(hotelName); // Hotel name in detailed view
					infoLocationDetails1.setText(hotelLocation); // City
					infoBasicPrice1.setText(String.valueOf(basicRoomPrice));
					infoDoublePrice1.setText(String.valueOf(doubleRoomPrice));
					infoRatingBox1.setText(String.format("%.1f ★", rating)); // Rating

					hotel_booking.setCustomerID(customer.getCustomerID()); // Set customer ID
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
					"sp.travelAgencyName " + // Added travelAgencyName
					"FROM TravelService ts " +
					"JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
					"JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with
																								// ServiceProvider
					"WHERE ts.DepartureLocation = ? " +
					"AND ts.ArrivalLocation = ? AND ts.status = 'ONGOING';";
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
					"sp.travelAgencyName " + // Added travelAgencyName
					"FROM TravelService ts " +
					"JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
					"JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with
																								// ServiceProvider
					"WHERE ts.DepartureLocation = ? " +
					"AND ts.ArrivalLocation = ? AND ts.status = 'ONGOING';";
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
					"sp.travelAgencyName " + // Added travelAgencyName
					"FROM TravelService ts " +
					"JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
					"JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with
																								// ServiceProvider
					"WHERE ts.DepartureLocation = ? " +
					"AND ts.ArrivalLocation = ? AND ts.status = 'ONGOING';";
		} else {
			System.out.println("error. servicetype failure");
			return;
		}

		PreparedStatement prepStatement = connection.prepareStatement(query);
		// Assuming departureLocation and arrivalLocation are passed into this method
		prepStatement.setString(1, Des); // Set departure location
		prepStatement.setString(2, Arrival); // Set arrival location

		ResultSet resultSet = prepStatement.executeQuery();

		if (!resultSet.next()) {
			Text txt = new Text();
			txt.setText("No Services Found for the specified locations");
			txt.setFont(sampleText.getFont());
			txt.setFill(sampleText.getFill());
			txt.setStyle(sampleText.getStyle());
			servicesCont.getChildren().add(txt);
			return;
		}

		do {
			System.out.println(resultSet.getInt("ServiceProviderID") + resultSet.getInt("ServiceID") + "");
			// Store the result set values into local variables
			String serviceType = resultSet.getString("ServiceType");
			int serviceId = resultSet.getInt("ServiceProviderID");
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
							price, agency);
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
							ServiceID

					);

				});

				servicesCont.getChildren().add(hbox);
			} catch (IOException io) {
				System.out.println(io);
			}
		} while (resultSet.next());

	}

	public void getAllServices() throws SQLException, ClassNotFoundException {
		busServices.clear();

		busServices = serviceManager.getAllBusServices();
		trainServices = serviceManager.getAllTrainServices();
		flightServices = serviceManager.getAllFlightServices();

		if (busServices.isEmpty() && trainServices.isEmpty() && flightServices.isEmpty()) {
			Text txt = new Text();
			txt.setText("No Services Found");
			txt.setFont(sampleText.getFont());
			txt.setFill(sampleText.getFill());
			txt.setStyle(sampleText.getStyle());
			servicesCont.getChildren().add(txt);
			return;
		}

		servicesCont.getChildren().clear();

		for (int i = 0; i < busServices.size(); i++) {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));

			try {
				System.out.println("adding service to View");
				HBox hbox = fxmlloader.load();
				ServiceItemController sItemC = fxmlloader.getController();

				// Pass the captured data to the controller
				sItemC.setData(busServices.get(i).getArrivalLocation(), busServices.get(i).getDepartureLocation(),
						busServices.get(i).getArrivalTime(), busServices.get(i).getDepartureTime(),
						busServices.get(i).getServiceType(),
						busServices.get(i).getServiceID());
				int currentIndex = i;

				hbox.setOnMouseClicked(event -> {
					String agency;
					try {
						agency = serviceManager.getAgencyName(busServices.get(currentIndex).getServiceProviderID());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					showServiceDetails(
							busServices.get(currentIndex).getServiceType(),
							busServices.get(currentIndex).getBusNumber(),
							busServices.get(currentIndex).getDepartureLocation(),
							busServices.get(currentIndex).getArrivalLocation(),
							busServices.get(currentIndex).getDepartureTime(),
							busServices.get(currentIndex).getArrivalLocation(),
							busServices.get(currentIndex).getStationName(),
							busServices.get(currentIndex).getStationLocation(),
							"" + busServices.get(currentIndex).getPrice(), agency);

					BookingDetails(
							busServices.get(currentIndex).getServiceType(),
							busServices.get(currentIndex).getBusNumber(),
							busServices.get(currentIndex).getDepartureLocation(),
							busServices.get(currentIndex).getArrivalLocation(),
							busServices.get(currentIndex).getDepartureTime(),
							busServices.get(currentIndex).getArrivalLocation(),
							busServices.get(currentIndex).getStationName(),
							busServices.get(currentIndex).getStationLocation(),
							"" + busServices.get(currentIndex).getPrice(),
							busServices.get(currentIndex).getDepartureDate(),
							busServices.get(currentIndex).getArrivalDate(),
							agency,
							busServices.get(currentIndex).getServiceID());

				});

				servicesCont.getChildren().add(hbox);
			} catch (IOException io) {
				System.out.println(io);
			}
		}

		for (int i = 0; i < flightServices.size(); i++) {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));

			try {
				System.out.println("adding service to View");
				HBox hbox = fxmlloader.load();
				ServiceItemController sItemC = fxmlloader.getController();

				// Pass the captured data to the controller
				sItemC.setData(flightServices.get(i).getArrivalLocation(), flightServices.get(i).getDepartureLocation(),
						flightServices.get(i).getArrivalTime(), flightServices.get(i).getDepartureTime(),
						flightServices.get(i).getServiceType(),
						flightServices.get(i).getServiceID());
				int currentIndex = i;

				hbox.setOnMouseClicked(event -> {
					String agency;
					try {
						agency = serviceManager.getAgencyName(flightServices.get(currentIndex).getServiceProviderID());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					showServiceDetails(
							flightServices.get(currentIndex).getServiceType(),
							flightServices.get(currentIndex).getFlightNumber(),
							flightServices.get(currentIndex).getDepartureLocation(),
							flightServices.get(currentIndex).getArrivalLocation(),
							flightServices.get(currentIndex).getDepartureTime(),
							flightServices.get(currentIndex).getArrivalLocation(),
							flightServices.get(currentIndex).getAirportName(),
							flightServices.get(currentIndex).getAirportLocation(),
							"" + flightServices.get(currentIndex).getPrice(), agency);

					BookingDetails(
							flightServices.get(currentIndex).getServiceType(),
							flightServices.get(currentIndex).getFlightNumber(),
							flightServices.get(currentIndex).getDepartureLocation(),
							flightServices.get(currentIndex).getArrivalLocation(),
							flightServices.get(currentIndex).getDepartureTime(),
							flightServices.get(currentIndex).getArrivalLocation(),
							flightServices.get(currentIndex).getAirportName(),
							flightServices.get(currentIndex).getAirportLocation(),
							"" + flightServices.get(currentIndex).getPrice(),
							flightServices.get(currentIndex).getDepartureDate(),
							flightServices.get(currentIndex).getArrivalDate(),
							agency,
							flightServices.get(currentIndex).getServiceID());

				});

				servicesCont.getChildren().add(hbox);
			} catch (IOException io) {
				System.out.println(io);
			}
		}

		for (int i = 0; i < trainServices.size(); i++) {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));

			try {
				System.out.println("adding service to View");
				HBox hbox = fxmlloader.load();
				ServiceItemController sItemC = fxmlloader.getController();

				// Pass the captured data to the controller
				sItemC.setData(trainServices.get(i).getArrivalLocation(), trainServices.get(i).getDepartureLocation(),
						trainServices.get(i).getArrivalTime(), trainServices.get(i).getDepartureTime(),
						trainServices.get(i).getServiceType(),
						trainServices.get(i).getServiceID());
				int currentIndex = i;

				hbox.setOnMouseClicked(event -> {
					String agency;
					try {
						agency = serviceManager.getAgencyName(trainServices.get(currentIndex).getServiceProviderID());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					showServiceDetails(
							trainServices.get(currentIndex).getServiceType(),
							trainServices.get(currentIndex).getTrainNumber(),
							trainServices.get(currentIndex).getDepartureLocation(),
							trainServices.get(currentIndex).getArrivalLocation(),
							trainServices.get(currentIndex).getDepartureTime(),
							trainServices.get(currentIndex).getArrivalLocation(),
							trainServices.get(currentIndex).getStationName(),
							trainServices.get(currentIndex).getStationLocation(),
							"" + trainServices.get(currentIndex).getPrice(), agency);

					BookingDetails(
							trainServices.get(currentIndex).getServiceType(),
							trainServices.get(currentIndex).getTrainNumber(),
							trainServices.get(currentIndex).getDepartureLocation(),
							trainServices.get(currentIndex).getArrivalLocation(),
							trainServices.get(currentIndex).getDepartureTime(),
							trainServices.get(currentIndex).getArrivalLocation(),
							trainServices.get(currentIndex).getStationName(),
							trainServices.get(currentIndex).getStationLocation(),
							"" + trainServices.get(currentIndex).getPrice(),
							trainServices.get(currentIndex).getDepartureDate(),
							trainServices.get(currentIndex).getArrivalDate(),
							agency,
							trainServices.get(currentIndex).getServiceID());

				});

				servicesCont.getChildren().add(hbox);
			} catch (IOException io) {
				System.out.println(io);
			}
		}

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
			String price, String agency) {

		if (!sideInfoPane.isVisible()) {
			sideInfoPane.setVisible(true);
		}
		ServiceNo.setText(transportNumber); // Set the service type (service number)
		From.setText(departureLocation); // Set the departure location
		To.setText(arrivalLocation); // Set the arrival location
		Dtime.setText(departureTime); // Set the departure time
		Atime.setText(arrivalTime); // Set the arrival time
		StationName.setText(StaName); // Set the service description
		StationLoc.setText(StaLOC); // Set the station location (can be adjusted)
		Price.setText(price + " PKR");
		agencyName.setText(agency);
		if (serviceType.equals("Bus")) {
			imageViewInfo.setGlyphName("BUS");
		} else if (serviceType.equals("Train")) {
			imageViewInfo.setGlyphName("TRAIN");
		} else if (serviceType.equals("Flight")) {
			imageViewInfo.setGlyphName("PLANE");
		} else {
			imageViewInfo.setGlyphName("HOTEL");
		}

	}

	public void StartSearch() {

		if (TypeInput == null || TypeInput.getValue() == null || TypeInput.getValue().isBlank()) {
			errText.setText("* Select a type");
			return;
		}
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

	public void comboBoxChange() throws ClassNotFoundException, SQLException {
		errText.setText("");
		if (TypeInput.getValue().equals("All"))
			getAllServices();
	}
}
