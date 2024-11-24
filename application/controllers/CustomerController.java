package application.controllers;

import java.io.IOException;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomerController {
	int currentTab = 1;
	private Customer customer;
	@FXML
	private Text usernameCus;
	@FXML
	private AnchorPane mainPanel_cus, selection, home;
	@FXML
	private Button nav_home_cus, nav_profile_cus, nav_service_cus, nav_Bookings;
	@FXML
	private Text cusID, profileName, profileEmail, profilePhoneNum, total, travel, hotel;
	@FXML
	private Pane modify_basePane, modify_passwordPane, modify_phonePane;
	@FXML
	private TextField newUsername;
	@FXML
	private PasswordField newPass;
	@FXML
	private VBox NewVbox, notifBox;
	@FXML
	private ScrollPane homeBookingScroll, notifScroll;

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

	public void loadProfileData() throws SQLException, ClassNotFoundException {
		Connection connection = dbHandler.connect();
		String query = "SELECT " +
				"    COUNT(*) AS total_bookings, " +
				"    SUM(CASE WHEN type = 'BUS' THEN 1 ELSE 0 END) AS total_travel_bookings, " +
				"    SUM(CASE WHEN type = 'BED' THEN 1 ELSE 0 END) AS total_hotel_bookings " +
				"FROM ( " +
				"    SELECT bookingID, 'BUS' AS type " +
				"    FROM travelbooking " +
				"    WHERE customerID = ? " +
				"    UNION ALL " +
				"    SELECT bookingID, 'BED' AS type " +
				"    FROM hotelbooking " +
				"    WHERE customerID = ? " +
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
		cusID.setText(String.valueOf(customer.getCustomerID()));
		profileName.setText(customer.getName());
		profileEmail.setText(customer.getEmail());
		profilePhoneNum.setText(customer.getPhoneNum());
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		currentTab = 4;
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
