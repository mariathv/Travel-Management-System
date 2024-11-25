package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.Managers.ServiceManager;
import application.Model.Customer;
import application.Model.Notifications;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomerController {
	int currentTab = 1;
	private Customer customer;
	@FXML
	private Text usernameCus, usernameCus1;
	@FXML
	private AnchorPane mainPanel_cus, selection, home;
	@FXML
	private Button nav_home_cus, nav_notifs_cus, nav_profile_cus, nav_service_cus, nav_Bookings, confirmPassChange,
			confirmPhoneChange, logoutBtn;
	@FXML
	private Text cusID, profileName, profileEmail, profilePhoneNum, total, travel, hotel, modify_textprompt, errPass,
			errPN;
	@FXML
	private Pane modify_basePane, modify_passwordPane, modify_phonePane;
	@FXML
	private TextField newUsername, changePNField;
	@FXML
	private PasswordField newPass, currPass;
	@FXML
	private VBox NewVbox, notifBox;
	@FXML
	private ScrollPane homeBookingScroll, notifScroll;
	@FXML
	private FontAwesomeIcon gobackManage2, gobackManage1;

	ScreenController screenController = new ScreenController();

	List<Notifications> notifs = new ArrayList<>();
	ServiceManager serviceManager = new ServiceManager();

	public void setCustomer(Customer customer) {
		this.customer = customer;
		// updateDashboard();
	}

	public void resetPassword() {
		modify_basePane.setVisible(false);
		modify_passwordPane.setVisible(true);
	}

	public void resetPhoneNumber() {
		modify_basePane.setVisible(false);
		modify_phonePane.setVisible(true);
	}

	public void confirmPasswordChange() throws ClassNotFoundException, SQLException {
		modify_passwordPane.setVisible(false);
		modify_basePane.setVisible(true);
		String query = "UPDATE customerauth SET password = ? WHERE userID = ?";
		try (Connection connection = dbHandler.connect();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newPass.getText());
			statement.setInt(2, customer.getCustomerID());
			statement.executeUpdate();
		}
	}

	public void confirmPhoneChange() throws ClassNotFoundException, SQLException {
		customer.setUsername(newUsername.getText());
		modify_phonePane.setVisible(false);
		modify_basePane.setVisible(true);
		String query = "UPDATE customerauth SET username = ? WHERE userID = ?";
		try (Connection connection = dbHandler.connect();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, newUsername.getText());
			statement.setInt(2, customer.getCustomerID());
			statement.executeUpdate();

		}
	}

	public void loadData() throws SQLException, ClassNotFoundException {
		Connection connection = dbHandler.connect();
		String query;
		if (NewVbox != null)
			NewVbox.getChildren().remove(1, NewVbox.getChildren().size());
		int x = 1; // Counter for serial numbers

		// Unified query using UNION
		query = "SELECT bookingID, bookingDate, TotalPrice AS price, status, 'BUS' AS type " +
				"FROM travelbooking WHERE customerID = ? " +
				"UNION ALL " +
				"SELECT bookingID, bookingDate, price, status, 'BED' AS type " +
				"FROM hotelbooking WHERE customerID = ?";

		try (PreparedStatement prepStatement = connection.prepareStatement(query)) {
			prepStatement.setInt(1, customer.getCustomerID());
			prepStatement.setInt(2, customer.getCustomerID());

			try (ResultSet resultSet = prepStatement.executeQuery()) {
				if (!resultSet.isBeforeFirst()) { // Check if the result set is empty
					System.out.println("No Bookings Found");
					return;
				}

				while (resultSet.next()) { // Start the loop here
					FXMLLoader fxmlloader = new FXMLLoader();
					fxmlloader.setLocation(getClass().getResource("../scenes/components/allBookings.fxml"));

					try {
						HBox hbox = fxmlloader.load();
						AllBookingItemController allBookingItem = fxmlloader.getController();

						// Pass the data to the controller
						String bookingID = resultSet.getString("bookingID");
						String bookingDate = resultSet.getString("bookingDate");
						String price = resultSet.getString("price");
						String type = resultSet.getString("type");
						int status = resultSet.getInt("status");
						if (status == 0)
							continue;
						allBookingItem.setData(x, bookingID, bookingDate, price, type, status);
						if (NewVbox != null)
							NewVbox.getChildren().add(hbox);
						x++;
					} catch (IOException io) {
						io.printStackTrace();
					}
				}
			}
		}
	}

	public void Logout(MouseEvent event) throws IOException {
		customer = null;
		screenController.switchToLoginScene(event, true);
	}

	public void loadHomePane_cus() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/new.fxml"));
			loader.setController(this);
			AnchorPane newPanel = loader.load();
			mainPanel_cus.getChildren().setAll(newPanel);
			changeBackButtonBG();
			nav_home_cus.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");
			currentTab = 1;
			loadData();
			loadNotifications();
			homeBookingScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadServiceSearch_cus() { // service pane
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchTravel.fxml"));
			AnchorPane newPanel = loader.load();
			ServiceControllerCus serviceC = loader.getController();
			serviceC.setCustomer(customer);
			mainPanel_cus.getChildren().setAll(newPanel);
			changeBackButtonBG();
			nav_service_cus.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");
			currentTab = 2;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadBooking_cus() { // service pane
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/Bookings.fxml"));
			CustomerBookingController cont = new CustomerBookingController();
			loader.setController(cont);
			AnchorPane newPanel = loader.load();
			CustomerBookingController bc = loader.getController();
			bc.setCustomer(customer);
			bc.loadData();
			mainPanel_cus.getChildren().setAll(newPanel);
			changeBackButtonBG();
			nav_Bookings.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");
			currentTab = 3;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadProfilePane() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/CustomerProfilePane.fxml"));
			loader.setController(this);
			AnchorPane newPanel = loader.load();
			mainPanel_cus.getChildren().setAll(newPanel);
			loadProfileData();
			changeBackButtonBG();
			nav_profile_cus.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");

			changePNField.setStyle("-fx-padding: 0 0 0 10px;");
			currPass.setStyle("-fx-padding: 0 0 0 10px;");
			newPass.setStyle("-fx-padding: 0 0 0 10px;");

			modify_basePane.setVisible(true);
			modify_passwordPane.setVisible(false);
			modify_phonePane.setVisible(false);

			confirmPhoneChange.setOnMouseClicked(arg0 -> {
				try {
					PhoneNumberChange();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			});

			logoutBtn.setOnMouseClicked(arg0 -> {
				try {
					Logout(arg0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			confirmPassChange.setOnMouseClicked(arg0 -> {
				try {
					PasswordChange();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			});

			gobackManage1.setOnMouseClicked(arg0 -> {
				modify_basePane.setVisible(true);
				modify_passwordPane.setVisible(false);
				modify_phonePane.setVisible(false);
			});

			gobackManage2.setOnMouseClicked(arg0 -> {
				modify_basePane.setVisible(true);
				modify_passwordPane.setVisible(false);
				modify_phonePane.setVisible(false);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		currentTab = 4;
	}

	private void PhoneNumberChange() throws ClassNotFoundException, SQLException {
		errPN.setFill(Color.RED);

		// Validate phone number input
		if (changePNField.getText().isEmpty()) {
			errPN.setText("* Phone number is required");
			return;
		}

		if (!isValidPhoneNumber(changePNField.getText())) {
			errPN.setText("* Please enter a valid phone number");
			return;
		}

		if (changePNField.getText().equals(customer.getPhoneNum())) {
			errPN.setText("* Enter New Phone number");
			return;
		}
		// Update the phone number in the database
		String query = "UPDATE customer SET phoneNo = ? WHERE CustomerID = ?";
		try (Connection connection = dbHandler.connect();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, changePNField.getText());
			statement.setInt(2, customer.getCustomerID());

			// Check if the update was successful
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected == 0) {
				errPN.setText("* Error changing phone number. Please try again.");
				return;
			}
		}

		customer.setPhoneNum(changePNField.getText());
		errPN.setFill(Color.BLACK);
		errPN.setText("Phone number changed successfully");
		loadProfileData();
	}

	private void PasswordChange() throws ClassNotFoundException, SQLException {
		errPass.setFill(Color.RED);
		if (currPass.getText().equals("") || newPass.getText().equals("")) {
			errPass.setText("* Above fields are required");
			return;
		}
		if (!isCurrentPassword(currPass.getText(), customer.getCustomerID())) {
			errPass.setText("* Current password incorrect");
			return;
		}
		if (newPass.getText().equals(currPass.getText())) {
			errPass.setText("* Insert Different password");
			return;
		}
		if (!setPassword(customer.getCustomerID(), newPass.getText())) {
			errPass.setText("* Error changing password");
			return;
		} else {
			errPass.setFill(Color.BLACK);
			errPass.setText("Password changed successfully");
			loadProfileData();
		}
	}

	public boolean setPassword(int ID, String newPassword) throws SQLException, ClassNotFoundException {
		String query = "UPDATE customerauth SET password = ? WHERE userID = ?";
		try (Connection connection = dbHandler.connect();
				PreparedStatement statement = connection.prepareStatement(query)) {
			// Hash the new password before storing (implement hashing as required)
			statement.setString(1, newPassword); // Replace with hashed password
			statement.setInt(2, ID);

			// Execute update
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public boolean isCurrentPassword(String currentPassword, int serviceProviderID)
			throws SQLException, ClassNotFoundException {
		String query = "SELECT password FROM customerauth WHERE userID = ?";
		try (Connection connection = dbHandler.connect();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, serviceProviderID);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				String storedPassword = rs.getString("password");
				// Compare stored hashed password with the input password (hash it if necessary)
				return storedPassword.equals(currentPassword); // Replace with hashing comparison
			}
		}
		return false;
	}

	public void loadProfileData() throws SQLException, ClassNotFoundException {
		Connection connection = dbHandler.connect();
		String query = "SELECT " +
				"    COUNT(*) AS total_bookings, " +
				"    SUM(CASE WHEN type = 'BUS' THEN 1 ELSE 0 END) AS total_travel_bookings, " +
				"    SUM(CASE WHEN type = 'BED' THEN 1 ELSE 0 END) AS total_hotel_bookings " +
				"FROM ( " +
				"    SELECT bookingID, 'BUS' AS type " +
				"    FROM travelbooking " +
				"    WHERE customerID = ? AND status = 1 " +
				"    UNION ALL " +
				"    SELECT bookingID, 'BED' AS type " +
				"    FROM hotelbooking " +
				"    WHERE customerID = ? AND status = 1 " +
				") AS all_bookings;";

		try (PreparedStatement prepStatement = connection.prepareStatement(query)) {
			prepStatement.setInt(1, customer.getCustomerID());
			prepStatement.setInt(2, customer.getCustomerID());

			try (ResultSet resultSet = prepStatement.executeQuery()) {
				if (resultSet.next()) { // Move the cursor to the first row
					// Retrieve data safely
					total.setText(String.valueOf(resultSet.getInt("total_bookings")));
					hotel.setText(String.valueOf(resultSet.getInt("total_hotel_bookings")));
					travel.setText(String.valueOf(resultSet.getInt("total_travel_bookings")));
				} else {
					// Handle case when no data is returned
					System.out.println("No Bookings Found");
					total.setText("0");
					hotel.setText("0");
					travel.setText("0");
				}
			}
		}

		// Set customer profile information
		cusID.setText("@" + customer.getUsername());
		profileName.setText(customer.getName());
		profileEmail.setText(customer.getEmail());
		profilePhoneNum.setText(customer.getPhoneNum());
	}

	private void changeBackButtonBG() {
		switch (currentTab) {
			case 1:
				nav_home_cus.setStyle("-fx-background-color:  #393D46;");
				System.out.println("changed bg color");
				break;
			case 2:
				nav_service_cus.setStyle("-fx-background-color:  #393D46;");
				System.out.println("changed bg color");
				break;
			case 4:
				nav_profile_cus.setStyle("-fx-background-color:  #393D46;");
				System.out.println("changed bg color");
				break;
			case 3:
				nav_Bookings.setStyle("-fx-background-color:  #393D46;");
				System.out.println("changed bg color");
				break;
		}
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		System.out.println("checking : " + phoneNumber);
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			return false;
		}
		return phoneNumber.matches("\\d+"); // Regex to check if it contains only digits
	}

	public void exitApplication() {
		Platform.exit();
	}

	public void loadNotifications() throws ClassNotFoundException {
		if (notifs != null)
			notifs.clear();

		notifScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		notifScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		notifs = serviceManager.getCustomerNotifications(customer.getCustomerID());

		if (notifs.isEmpty()) {
			// add hbox text
			System.out.println("No notifications");
			return;
		}

		for (int i = 0; i < notifs.size(); i++) {
			FXMLLoader fxmlloader = new FXMLLoader();
			URL resource = getClass().getResource("../scenes/components/notification_item.fxml");
			if (resource == null) {
				System.err.println("FXML file not found at the specified location!");
			}
			fxmlloader.setLocation(getClass().getResource("../scenes/components/notification_item.fxml"));

			try {
				VBox vbox = fxmlloader.load();
				notificationItemController notifController = fxmlloader.getController();

				notifController.setData(notifs.get(i).getDate(), notifs.get(i).getMessage(), notifs.get(i).getAgency(),
						notifs.get(i).getTag(), notifs.get(i).getType());

				if (notifBox != null)
					notifBox.getChildren().add(vbox);

				if (notifs.size() > 1 && i != notifs.size() - 1) {
					Separator s = new Separator();
					if (notifBox != null)
						notifBox.getChildren().add(s);
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}

	}

}
