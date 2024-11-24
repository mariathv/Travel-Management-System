package application.controllers;

import javafx.scene.paint.Color;
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
import application.Model.FlightService;
import application.Model.HotelService;
import application.Model.ServiceProvider;
import application.Model.TrainService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServiceController {
	ServiceProvider serviceProvider;
	ServiceManager serviceManager = new ServiceManager();
	@FXML
	private Pane addServicePane, sideInfoPane, addHotelListingPane, hotelServicePane, bookingsDisplayPane,
			feedbacksDisplayPane;
	@FXML
	private ScrollPane viewServicePane, feedbacksScroll;
	@FXML
	private Text serviceNumber, PlatformNameTxt, PlatformLocTxt;
	@FXML
	private VBox servicesCont, vBoxFeedbacks;
	@FXML
	private HBox GateInfo, tabPane, infoRatingBox;
	@FXML
	private Button addNewServiceBtn, removeButton, viewBookings, viewFeedbacks, removeButton1, viewBookings1,
			viewFeedbacks1, ongoingBTN, doneBTN, addBtn;
	@FXML
	private TextField depLoc, depTime, depDate, arvLoc, arvTime, arvDate, SBusNo, BStationName, BStationLoc, StktPrice,
			GNumber, totalSeats;
	@FXML
	private Text infoServiceType, infoBusNo, depLocInfo, arvLocInfo, depTimeInfo, arvTimeInfo, dateInfo, hotelRatingNum; // dynamically
	// updated
	// info upon mouse click
	@FXML
	private FontAwesomeIcon goBackView, markDoneBtn;
	@FXML
	private Text critBusNo;
	@FXML
	private TextField HotelName, HotelLocation, BasicPrice, DoublePrice, City;
	@FXML
	private Text infoHotelName, infoLocationDetails, infoBasicPrice, infoDoublePrice;

	boolean infoSideVisibleWas;
	private int selectedServiceID = -1;

	public ArrayList<BusService> BusServices = new ArrayList<>();
	public ArrayList<FlightService> FlightServices = new ArrayList<>();
	public ArrayList<TrainService> TrainServices = new ArrayList<>();
	public ArrayList<HotelService> HotelServices = new ArrayList<>();

	boolean bookingsLoaded = false, feedbacksLoaded = false;
	int bookingsLoadedFr = -1, feedbacksLoadedFr = -1;

	int selectedTab = -1;
	boolean isEditing = false;

	@FXML
	public void initialize() {

		ongoingBTN.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		doneBTN.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

		sideInfoPane.setVisible(false);
		hotelServicePane.setVisible(false);
		addServicePane.setVisible(false);

		ongoingBTN.setOnMouseClicked(event -> {
			try {
				toggleButtonState(ongoingBTN, doneBTN, 1);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		doneBTN.setOnMouseClicked(event -> {
			try {
				toggleButtonState(doneBTN, ongoingBTN, 2);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	private void toggleButtonState(Button activeBtn, Button inactiveBtn, int tabNumber)
			throws ClassNotFoundException, SQLException {
		if (selectedTab == tabNumber)
			return;
		activeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");

		inactiveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

		selectedTab = tabNumber;
		initServicesFS();
	}

	public void clearAllServices() {
		BusServices.clear();
		FlightServices.clear();
		TrainServices.clear();
		HotelServices.clear();
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;

		if (serviceProvider.getServiceType().equals("Hotel")) {
			tabPane.setVisible(false);
		}
	}

	public void editServiceForm() {
		if (serviceProvider.getServiceType().equals("Hotel")) {
			addHotelListingPane.setVisible(true);
			addServicePane.setVisible(false);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			bookingsDisplayPane.setVisible(false);

			goBackView.setVisible(true);
			if (hotelServicePane.isVisible()) {
				infoSideVisibleWas = true;
			} else
				infoSideVisibleWas = false;
			hotelServicePane.setVisible(false);

		} else {
			if (serviceProvider.getServiceType().equals("Train")) {
				critBusNo.setText("Train Number:");
				SBusNo.setPromptText("TRAIN NO");
			} else if (serviceProvider.getServiceType().equals("Flight")) {
				GateInfo.setVisible(true);
				SBusNo.setPromptText("FLIGHT NO");
				critBusNo.setText("Flight Number:");
				PlatformNameTxt.setText(" Airport Name");
				PlatformLocTxt.setText(" Airport Location");
			}
			tabPane.setVisible(false);
			addServicePane.setVisible(true);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			bookingsDisplayPane.setVisible(false);
			goBackView.setVisible(true);
			if (sideInfoPane.isVisible()) {
				infoSideVisibleWas = true;
			} else
				infoSideVisibleWas = false;
			sideInfoPane.setVisible(false);
		}

		// extra stuff
		updateServiceProviderData();
	}

	public void updateServiceProviderData() {
		isEditing = true;

		addBtn.setOnAction(event -> {
			updateService();
		});

		if (serviceProvider.getServiceType().equals("Bus")) {
			BusService bs = null;
			for (int i = 0; i < BusServices.size(); i++) {
				if (BusServices.get(i).getServiceID() == selectedServiceID) {
					bs = BusServices.get(i);
					break;
				}
			}
			if (bs == null) {
				System.out.println("Service not found, error");
				return;
			}

			depLoc.setText(bs.getDepartureLocation());
			depTime.setText(bs.getDepartureTime());
			depDate.setText(bs.getDepartureDate());
			arvTime.setText(bs.getArrivalTime());
			arvLoc.setText(bs.getArrivalLocation());
			arvDate.setText(bs.getArrivalDate());
			SBusNo.setText(bs.getBusNumber());
			BStationName.setText(bs.getStationName());
			BStationLoc.setText(bs.getStationLocation());
			StktPrice.setText("" + bs.getPrice());

			addBtn.setText("Done");

		}
	}

	public void switchBackFromEdit() {
		addBtn.setText("Add");
		isEditing = false;

		addBtn.setOnAction(event -> {
			try {
				addNewService();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		depLoc.clear();
		depTime.clear();
		depDate.clear();
		arvTime.clear();
		arvLoc.clear();
		arvDate.clear();
		SBusNo.clear();
		BStationName.clear();
		BStationLoc.clear();
		StktPrice.clear();
	}

	public void addNewServiceForm() {
		if (serviceProvider.getServiceType().equals("Hotel")) {
			addHotelListingPane.setVisible(true);
			addServicePane.setVisible(false);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			bookingsDisplayPane.setVisible(false);

			goBackView.setVisible(true);
			if (hotelServicePane.isVisible()) {
				infoSideVisibleWas = true;
			} else
				infoSideVisibleWas = false;
			hotelServicePane.setVisible(false);

		} else {
			if (serviceProvider.getServiceType().equals("Train")) {
				critBusNo.setText("Train Number:");
				SBusNo.setPromptText("TRAIN NO");
			} else if (serviceProvider.getServiceType().equals("Flight")) {
				GateInfo.setVisible(true);
				SBusNo.setPromptText("FLIGHT NO");
				critBusNo.setText("Flight Number:");
				PlatformNameTxt.setText(" Airport Name");
				PlatformLocTxt.setText(" Airport Location");
			}
			tabPane.setVisible(false);
			addServicePane.setVisible(true);
			viewServicePane.setVisible(false);
			addNewServiceBtn.setVisible(false);
			bookingsDisplayPane.setVisible(false);
			goBackView.setVisible(true);
			if (sideInfoPane.isVisible()) {
				infoSideVisibleWas = true;
			} else
				infoSideVisibleWas = false;
			sideInfoPane.setVisible(false);
		}

	}

	public void displayServices() {
		addServicePane.setVisible(false);
		addHotelListingPane.setVisible(false);
		bookingsDisplayPane.setVisible(false);
		viewServicePane.setVisible(true);
		addNewServiceBtn.setVisible(true);
		isEditing = false;
		if (serviceProvider.getServiceType().equals("Hotel")) {
			tabPane.setVisible(false);
			viewFeedbacks1.setVisible(true);
			viewBookings1.setVisible(true);
		} else {
			tabPane.setVisible(true);
			viewFeedbacks.setVisible(true);
			viewBookings.setVisible(true);
		}
		goBackView.setVisible(false);
		feedbacksDisplayPane.setVisible(false);
		if (infoSideVisibleWas && !serviceProvider.getServiceType().equals("Hotel"))
			sideInfoPane.setVisible(true);
		else if (infoSideVisibleWas && serviceProvider.getServiceType().equals("Hotel"))
			hotelServicePane.setVisible(true);

	}

	public void updateService() {
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
		try {
			int serviceID = selectedServiceID;

			boolean flag = serviceManager.updateService(serviceID, depLoc.getText(), depTime.getText(),
					depDate.getText(),
					arvLoc.getText(), arvTime.getText(), arvDate.getText(),
					SBusNo.getText(), BStationName.getText(), BStationLoc.getText(),
					StktPrice.getText(), GNumber.getText(),
					serviceProvider.getServiceType());

			if (flag) {
				initServicesFS();
				displayServices();
				switchBackFromEdit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isAllDigits(String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}
		return input.matches("\\d+");
	}

	public void addNewService() throws SQLException, ClassNotFoundException {

		// add new service logic and db handling
		if (depLoc.getText().isEmpty() ||
				depTime.getText().isEmpty() ||
				depDate.getText().isEmpty() ||
				arvLoc.getText().isEmpty() ||
				arvTime.getText().isEmpty() ||
				arvDate.getText().isEmpty() ||
				SBusNo.getText().isEmpty() ||
				BStationName.getText().isEmpty() ||
				BStationLoc.getText().isEmpty() ||
				StktPrice.getText().isEmpty() || !isAllDigits(totalSeats.getText())) {
			System.out.println("One or more fields are empty/incorrect. Please fill all fields.");
			return;
		}
		try {

			boolean flag = serviceManager.addNewService(serviceProvider, depLoc.getText(), depTime.getText(),
					depDate.getText(),
					arvLoc.getText(), arvTime.getText(), arvDate.getText(),
					SBusNo.getText(), BStationName.getText(), BStationLoc.getText(),
					StktPrice.getText(), GNumber.getText(), Integer.parseInt(totalSeats.getText()));

			if (flag) {
				initServicesFS();
				displayServices();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeService(int serviceID, boolean isTravelService) throws SQLException, ClassNotFoundException {

		Connection connection = dbHandler.connect();

		try {
			connection.setAutoCommit(false);

			if (isTravelService) {
				serviceManager.deleteServiceFeedback(serviceID);
				try {
					String cancellationMessage = "Your booking has been cancelled due to service unavailability ("
							+ serviceID + "), you have been refunded";
					int notificationsSent = serviceManager.addCancellationNotifications(serviceID,
							cancellationMessage, true, serviceProvider.getServiceType(),
							serviceProvider.getAgencyName());
					System.out.println("Notifications sent: " + notificationsSent);
				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				serviceManager.deleteTravelBooking(serviceID);

				// Assuming serviceProvider is accessible here
				String serviceType = serviceProvider.getServiceType();
				if (serviceManager.deleteServiceDetails(serviceID, serviceType) > 0) {
					if (serviceManager.deleteTravelService(serviceID) > 0) {
						System.out.println("Successfully removed travel service with ID: " + serviceID);

						connection.commit();
						initServicesFS();
						displayServices();
					} else {
						System.out.println("Failed to remove service from TravelService table.");
						connection.rollback();
					}
				} else {
					System.out.println("Failed to remove service details for service ID: " + serviceID);
					connection.rollback();
				}
			} else {
				serviceManager.deleteHotelFeedback(serviceID);
				String cancellationMessage = "Your booking has been cancelled due to service unavailability ("
						+ serviceID + "), you have been refunded";
				try {
					int notificationsSent = serviceManager.addCancellationNotifications(serviceID,
							cancellationMessage, false, serviceProvider.getServiceType(),
							serviceProvider.getAgencyName());
					System.out.println("Notifications sent: " + notificationsSent);
				} catch (SQLException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				serviceManager.deleteHotelBooking(serviceID);

				if (serviceManager.deleteHotelService(serviceID) > 0) {
					System.out.println("Successfully removed hotel service with ID: " + serviceID);

					connection.commit();
					initServicesFS();
					displayServices();
				} else {
					System.out.println("Failed to remove service from HotelService table.");
					connection.rollback();
				}
			}
		} catch (SQLException e) {
			System.out.println("Error while removing service: " + e.getMessage());
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true); // Restore auto-commit
			connection.close();
		}
	}

	public void addNewHotelListing() throws SQLException, ClassNotFoundException {

		// add new service logic and db handling
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
			} else {
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

		// clearing prev fetched data
		clearAllServices();
		if (serviceProvider.getServiceType().equals("Hotel"))
			addNewServiceBtn.setText("Add Hotel Listing");
		servicesCont.getChildren().clear();

		Connection connection = dbHandler.connect();
		String query;
		if (serviceProvider.getServiceType().equals("Bus"))
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
					"ts.status, " +
					"ts.ticketPrice " +
					"FROM TravelService ts " +
					"JOIN BusService bs ON ts.ServiceID = bs.ServiceID " +
					"WHERE ts.ServiceProviderID = ?;";
		else if (serviceProvider.getServiceType().equals("Flight")) {
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
					"ts.status, " +
					"ts.ticketPrice " +
					"FROM TravelService ts " +
					"JOIN FlightService fs ON ts.ServiceID = fs.ServiceID " +
					"WHERE ts.ServiceProviderID = ?;";
		} else if (serviceProvider.getServiceType().equals("Train")) {
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
					"ts.status, " +
					"ts.ticketPrice " +
					"FROM TravelService ts " +
					"JOIN TrainService bs ON ts.ServiceID = bs.ServiceID " +
					"WHERE ts.ServiceProviderID = ?;";
		} else if (serviceProvider.getServiceType().equals("Hotel")) {
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
		} else {
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
			if (selectedTab == 1 || selectedTab == -1) {
				// Show ongoing services
				if (!serviceProvider.getServiceType().equals("Hotel")
						&& !resultSet.getString("status").equals("ONGOING")) {
					continue;
				}
			}
			if (selectedTab == 2) {
				// Show done services
				if (!serviceProvider.getServiceType().equals("Hotel")
						&& !resultSet.getString("status").equals("DONE")) {
					continue;
				}
			}
			// Store the result set values into local variables

			if (!serviceProvider.getServiceType().equals("Hotel")) {
				int serviceID = resultSet.getInt(1);
				String serviceType = resultSet.getString(4);
				String busNo = resultSet.getString(11);
				String serviceDesc = resultSet.getString(3);
				String arrivalTime = resultSet.getString(6);
				String departureTime = resultSet.getString(5);
				String departureLocation = resultSet.getString(7);
				String arrivalLocation = resultSet.getString(8);
				String status;
				String departureDate;
				String arrivalDate;
				int price;
				if (serviceProvider.getServiceType().equals("Flight")) {
					departureDate = resultSet.getString(13);
					arrivalDate = resultSet.getString(14);
					status = resultSet.getString(15);
					price = resultSet.getInt(16);
				} else {
					departureDate = resultSet.getString(12);
					arrivalDate = resultSet.getString(13);
					status = resultSet.getString(14);
					price = resultSet.getInt(15);
				}

				System.out.println(serviceProvider.getServiceType() + " is the service type of SP");
				if (serviceProvider.getServiceType().equals("Bus")) {
					BusService busService = new BusService(
							resultSet.getString(9), // stationName
							resultSet.getString(10), // stationLocation
							busNo, // BusNumber
							resultSet.getInt(1), // serviceID
							resultSet.getInt(2), // serviceProviderID
							serviceDesc, // description
							serviceType, // serviceType
							departureTime, // departureTime
							arrivalTime, // arrivalTime
							departureLocation, // departureLocation
							arrivalLocation, // arrivalLocation
							departureDate, // serviceDate
							arrivalDate);
					busService.setStatus(status);
					busService.setPrice(price);
					BusServices.add(busService);
				} else if (serviceProvider.getServiceType().equals("Train")) {
					TrainService trainService = new TrainService(
							resultSet.getString(9), // stationName
							resultSet.getString(10), // stationLocation
							busNo, // TrainNumber
							resultSet.getInt(1), // serviceID
							resultSet.getInt(2), // serviceProviderID
							serviceDesc, // description
							serviceType, // serviceType
							departureTime, // departureTime
							arrivalTime, // arrivalTime
							departureLocation, // departureLocation
							arrivalLocation, // arrivalLocation
							departureDate, // serviceDate
							arrivalDate);
					trainService.setStatus(status);
					trainService.setPrice(price);
					TrainServices.add(trainService);
				} else if (serviceProvider.getServiceType().equals("Flight")) {
					FlightService flightService = new FlightService(
							resultSet.getString(9), // stationName
							resultSet.getString(10), // stationLocation
							busNo, // FlightNumber
							resultSet.getInt(1), // serviceID
							resultSet.getInt(2), // serviceProviderID
							serviceDesc, // description
							serviceType, // serviceType
							departureTime, // departureTime
							arrivalTime, // arrivalTime
							departureLocation, // departureLocation
							arrivalLocation, // arrivalLocation
							departureDate, // serviceDate
							arrivalDate,
							resultSet.getString(10)

					);
					flightService.setStatus(status);
					flightService.setPrice(price);
					FlightServices.add(flightService);
				}

				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("../scenes/components/service_item.fxml"));

				try {
					System.out.println("adding service to View");
					HBox hbox = fxmlloader.load();
					ServiceItemController sItemC = fxmlloader.getController();

					// Pass the captured data to the controller
					sItemC.setData(arrivalLocation, departureLocation, arrivalTime, departureTime,
							serviceProvider.getServiceType(), serviceID);

					hbox.setOnMouseClicked(event -> {
						showServiceDetails(serviceType, busNo, arrivalLocation, departureLocation, arrivalTime,
								departureTime);
						selectedServiceID = serviceID;
					});

					servicesCont.getChildren().add(hbox);
					Separator separator = new Separator();
					separator.setPrefWidth(100); // Set the width of the separator
					separator.setStyle("-fx-background-color: transparent; "
							+ "-fx-border-color: gray; "
							+ "-fx-border-width: 0 0 1 0;"); // Gray line at the bottom

					servicesCont.getChildren().add(separator);
					// removeButton.setOnAction(event -> {
					// if (selectedServiceID != -1) {
					// try {
					// System.out.println("trying to remove service");
					// removeService(selectedServiceID);
					// removeServiceFromView(selectedServiceID);
					// } catch (SQLException | ClassNotFoundException e) {
					// e.printStackTrace();
					// }
					// }
					// });
				} catch (IOException io) {
					System.out.println(io);
				}
			} else {
				int HotelServiceID = resultSet.getInt(1);
				int ServiceProviderID = resultSet.getInt(2);
				String HotelName = resultSet.getString(3);
				String HotelLocation = resultSet.getString(4);
				int rating = resultSet.getInt(5);
				int BasicRoomPrice = resultSet.getInt(6);
				int DoubleRoomPrice = resultSet.getInt(7);
				String city = resultSet.getString(8);

				HotelService hotelService = new HotelService(
						HotelServiceID, ServiceProviderID, HotelName, HotelLocation, rating, BasicRoomPrice,
						DoubleRoomPrice, city);
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

					hbox.setOnMouseClicked(event -> {
						selectedServiceID = HotelServiceID;
						showHotelServiceDetails(hotelService);
					});
					System.out.println("addiing");
					servicesCont.getChildren().add(hbox);
				} catch (IOException io) {
					System.out.println("Error loading FXML: " + io.getMessage());
				}

			}
		} while (resultSet.next());
	}

	public void removeSelectedService() throws ClassNotFoundException, SQLException {
		System.out.println("trying to remove service");
		if (serviceProvider.getServiceType().equals("Hotel"))
			removeService(selectedServiceID, false);
		else
			removeService(selectedServiceID, true);
		removeServiceFromView(selectedServiceID);
		if (!serviceProvider.getServiceType().equals("Hotel"))
			sideInfoPane.setVisible(false);
		else
			hotelServicePane.setVisible(false);
	}

	private void showServiceDetails(String serviceType, String busNo, String arrivalLocation, String departureLocation,
			String arrivalTime,
			String departureTime) {

		serviceProvider.printDetails();
		if (!sideInfoPane.isVisible()) {
			sideInfoPane.setVisible(true);
		}
		depLocInfo.setText(departureLocation);
		arvLocInfo.setText(arrivalLocation);
		infoServiceType.setText(serviceType);
		infoBusNo.setText("  " + busNo);
		depTimeInfo.setText(departureTime);
		arvTimeInfo.setText(arrivalTime);

	}

	void showHotelServiceDetails(HotelService hotel) {

		if (!hotelServicePane.isVisible()) {
			hotelServicePane.setVisible(true);
		}

		System.out.println("Clicked on hotel detail");
		infoHotelName.setText(" " + hotel.getHotelName());
		infoLocationDetails.setText(hotel.getHotelLocation());
		infoBasicPrice.setText(Integer.toString(hotel.getBasicRoomPrice()));
		infoDoublePrice.setText(Integer.toString(hotel.getDoubleRoomPrice()));
		hotelRatingNum.setText("(" + hotel.getRating() + ")");
		for (int i = 0; i < hotel.getRating(); i++) {
			FontAwesomeIcon fai = new FontAwesomeIcon();
			fai.setGlyphName("STAR");
			fai.setSize("1.5em");
			fai.setFill(Color.WHITE);
			infoRatingBox.getChildren().add(fai);
		}

	}

	private void removeServiceFromView(int serviceID) throws ClassNotFoundException {
		for (Node node : servicesCont.getChildren()) {
			if (node instanceof HBox) {
				HBox hbox = (HBox) node;
				ServiceItemController controller = (ServiceItemController) hbox.getUserData(); // Assuming you set
																								// userData in the HBox

				if (controller != null && controller.getServiceID() == serviceID) {
					servicesCont.getChildren().remove(hbox);
					break; // Exit after removing the correct HBox
				}
			}
		}
	}

	// bookings handling
	public void displayBookings() throws IOException, ClassNotFoundException, SQLException {
		if (bookingsLoaded && selectedServiceID == bookingsLoadedFr) {
			viewBookings.setVisible(false);
			viewBookings1.setVisible(false);

			hideServicePanels("bookings");
			if (!bookingsDisplayPane.isVisible()) {
				bookingsDisplayPane.setVisible(true);
			}
			return; // Exit early since bookings are already loaded for the selected service
		}
		tabPane.setVisible(false);

		hideServicePanels("bookings");
		viewBookings.setVisible(false);
		viewBookings1.setVisible(false);

		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(getClass().getResource("../scenes/ServiceBookings.fxml"));
		BookingController cont = new BookingController();
		fxmlloader.setController(cont);
		Pane pane = fxmlloader.load();

		BookingController bookingController = fxmlloader.getController();
		if (serviceProvider.getServiceType().equals("Hotel")) {
			bookingController.loadBookingData(selectedServiceID, false);
		} else {
			bookingController.loadBookingData(selectedServiceID, true);
		}

		bookingsDisplayPane.getChildren().clear();
		bookingsDisplayPane.getChildren().add(pane);

		bookingsLoaded = true;
		bookingsLoadedFr = selectedServiceID;
		bookingsDisplayPane.setVisible(true);
	}

	// Helper method to hide all service-related panels
	private void hideServicePanels(String forr) {
		if (forr.equals("bookings")) {
			addNewServiceBtn.setVisible(false);
			viewServicePane.setVisible(false);
			addServicePane.setVisible(false);
			addHotelListingPane.setVisible(false);
			feedbacksDisplayPane.setVisible(false);
			goBackView.setVisible(true);
		} else if (forr.equals("feedbacks")) {
			addNewServiceBtn.setVisible(false);
			viewServicePane.setVisible(false);
			addServicePane.setVisible(false);
			addHotelListingPane.setVisible(false);
			bookingsDisplayPane.setVisible(false);
			goBackView.setVisible(true);
		}
	}

	public void displayFeedbacks() throws IOException, ClassNotFoundException, SQLException {
		System.out.println("view VBOX");
		if (feedbacksLoaded && selectedServiceID == feedbacksLoadedFr) {
			viewFeedbacks.setVisible(false);
			viewFeedbacks1.setVisible(false);
			tabPane.setVisible(false);
			hideServicePanels("feedbacks");
			if (!feedbacksDisplayPane.isVisible()) {
				feedbacksDisplayPane.setVisible(true);
			}
			return; // Exit early since bookings are already loaded for the selected service
		}
		tabPane.setVisible(false);
		hideServicePanels("feedbacks");
		viewFeedbacks.setVisible(false);
		viewFeedbacks1.setVisible(false);

		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setController(this);
		fxmlloader.setLocation(getClass().getResource("../scenes/ServiceFeedbacks.fxml"));
		Pane pane = fxmlloader.load();
		if (serviceProvider.getServiceType().equals("Hotel"))
			loadFeedbackData(selectedServiceID, false);
		else
			loadFeedbackData(selectedServiceID, true);

		feedbacksDisplayPane.getChildren().clear();
		feedbacksDisplayPane.getChildren().add(pane);

		feedbacksScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		feedbacksLoaded = true;
		feedbacksLoadedFr = selectedServiceID;
		feedbacksDisplayPane.setVisible(true);
	}

	public void loadFeedbackData(int serviceID, boolean tFLag)
			throws IOException, ClassNotFoundException, SQLException {
		System.out.println("clearing VBOX");
		vBoxFeedbacks.getChildren().clear();

		// Create an instance of ServiceDAO to fetch feedbacks for the given serviceID
		ServiceManager dao = new ServiceManager();
		List<ServiceManager.Feedback> feedbacks;
		if (tFLag)
			feedbacks = dao.getFeedbacksByServiceID(serviceID); // Get the list of feedbacks from
		else
			feedbacks = dao.getHotelFeedbacksByServiceID(serviceID); // the DAO

		System.out.println("Fetching Data");

		// If no feedbacks are available, show a message
		if (feedbacks.isEmpty()) {
			Text noFeedbacksText = new Text("No feedbacks available for this service.");
			vBoxFeedbacks.getChildren().add(noFeedbacksText);
		} else {
			for (ServiceManager.Feedback feedback : feedbacks) {
				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("../scenes/components/feedback_item.fxml"));
				VBox vbox = fxmlloader.load();
				feedbackItemController feedbackItemController = fxmlloader.getController();

				feedbackItemController.setData(feedback.getCustomerUsername(), feedback.getComment(),
						feedback.getRating());

				vBoxFeedbacks.getChildren().add(vbox);
			}
		}
	}

	public void markServiceDone() throws ClassNotFoundException, SQLException {
		if (selectedServiceID == -1)
			return;
		boolean flag = serviceManager.markServiceAsDone(selectedServiceID, serviceProvider.getAgencyName());
		if (flag) {
			System.out.println("Service marked done successfully");
			initServicesFS();
		}
	}

}
