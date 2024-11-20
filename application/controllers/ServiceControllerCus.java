package application.controllers;

import java.awt.ScrollPane;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private ComboBox<String> TypeInput;
	@FXML
	private TextField FromInput, ToInput;
	@FXML
	private Pane sideInfoPane;
	@FXML
	private AnchorPane SearchPane, ConfirmBookingPane;
	@FXML
	private Text ServiceNo2, StaName, Staloc, Agency, Dloc, Ddate, Dtime1, Price1, Atime1, ADate, Aloc;
	@FXML
	private VBox servicesCont;
	@FXML
	private ImageView imageViewInfo, imageViewInfo1;

	String Des, Arrival, Type;
	boolean infoSideVisibleWas;

	@FXML
	public void initialize() {
		// Add items to the ComboBox
		TypeInput.getItems().add("Bus");
		TypeInput.getItems().add("Train");
		TypeInput.getItems().add("Flight");
	}

	public ServiceControllerCus() {
		// Initialization code here
	}

	public void displayServices() {
		if (infoSideVisibleWas)
			sideInfoPane.setVisible(true);
	}

	public void ConfirmBooking() {
		if (!ConfirmBookingPane.isVisible())
			ConfirmBookingPane.setVisible(true);
		SearchPane.setVisible(false);
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
			String agency) {
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
					"sp.travelAgencyName " + // Added travelAgencyName
					"FROM TravelService ts " +
					"JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
					"JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with
																								// ServiceProvider
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
					"sp.travelAgencyName " + // Added travelAgencyName
					"FROM TravelService ts " +
					"JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
					"JOIN ServiceProvider sp ON ts.ServiceProviderID = sp.serviceProviderID " + // Join with
																								// ServiceProvider
					"WHERE ts.DepartureLocation = ? " +
					"AND ts.ArrivalLocation = ?;";
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
							price);
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
							agency);
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
			String price) {

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
		Price.setText(price);
		Image image;
		if (serviceType.equals("Bus")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/bus.png"));
		} else if (serviceType.equals("Train")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/train.png"));
		} else if (serviceType.equals("Flight")) {
			image = new Image(getClass().getResourceAsStream("../assets/images/pngs/flight.png"));
		} else {
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
