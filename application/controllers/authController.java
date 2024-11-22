package application.controllers;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import application.Model.Customer;
import application.Model.ServiceProvider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

//adding sql executions here for now, will add it in separate classes later
public class authController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private TextField reg_username;
	@FXML
	private TextField email;
	@FXML
	private TextField reg_password;
	@FXML
	private TextField password_re;
	@FXML
	private TextField agencyName;
	@FXML
	private Pane mainRegPanel, nextRegPanel, serviceInfoPane;;
	@FXML
	private ComboBox<String> ComboSTypes;
	@FXML
	private TextField email_Cus;
	@FXML
	private TextField phoneNo;
	@FXML
	private TextField reg_name;
	@FXML
	private TextField reg_CUS_username;
	@FXML
	private TextField reg_CUS_password;
	@FXML
	private TextField password_re_Cus;
	@FXML
	private Text serviceEnterText;
	@FXML
	private FontAwesomeIcon serviceEnterImage;

	@FXML
	private Text errText, errTextLogin;

	ScreenController screenController = new ScreenController();

	ServiceProvider serviceProvider;
	Customer customer;

	private Stage primaryStage;

	public void selectServiceType() {
		mainRegPanel.setVisible(false);
		nextRegPanel.setVisible(true);

		ComboSTypes.getItems().add("Bus");
		ComboSTypes.getItems().add("Train");
		ComboSTypes.getItems().add("Flight");
		ComboSTypes.getItems().add("Hotel");
	}

	public void login(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		// Establish the database connection
		Connection connection = dbHandler.connect();

		// First, try to find the username in the customerAuth table
		String getPassQuery = "SELECT * FROM customerAuth WHERE username = ?";
		PreparedStatement prepStatement = connection.prepareStatement(getPassQuery);
		prepStatement.setString(1, username.getText()); // Assuming username is a TextField

		System.out.println("Authenticating username: " + username.getText());

		ResultSet resultSet = prepStatement.executeQuery();

		if (resultSet.next()) {
			// Customer found
			String retrievedPassword = resultSet.getString("password");
			int id = resultSet.getInt("userID");

			// Check if the entered password matches
			if (retrievedPassword.equals(password.getText())) {
				System.out.println("Login successful for Customer.");

				// Retrieve additional customer details
				String getCustomerQuery = "SELECT * FROM Customer WHERE CustomerID = ?";
				PreparedStatement prepStatement2 = connection.prepareStatement(getCustomerQuery);
				prepStatement2.setInt(1, id);

				ResultSet resultSet2 = prepStatement2.executeQuery();
				if (resultSet2.next()) {
					String customerName = resultSet2.getString("name");
					String email = resultSet2.getString("email");
					String phoneNo = resultSet2.getString("phoneNo");
					customer = new Customer(id, customerName, email, phoneNo, username.getText());

					// Redirect to the customer home screen
					screenController.switchToCusHome(event, customer);
				} else {
					System.out.println("Customer details not found.");
				}
			} else {
				System.out.println("Incorrect password for Customer.");
				errTextLogin.setText("Incorrect password");
			}
		} else {
			// Customer not found, try ServiceProvider
			System.out.println("Username not found in customerAuth. Checking serviceProviderAuth...");

			// Now try to find the username in the serviceProviderAuth table
			String getPassQuery2 = "SELECT * FROM serviceProviderAuth WHERE username = ?";
			PreparedStatement prepStatement2 = connection.prepareStatement(getPassQuery2);
			prepStatement2.setString(1, username.getText()); // Assuming username is a TextField

			ResultSet resultSet2 = prepStatement2.executeQuery();

			if (resultSet2.next()) {
				// ServiceProvider found
				String retrievedPassword = resultSet2.getString("password");
				int id = resultSet2.getInt("userID");

				// Check if the entered password matches
				if (retrievedPassword.equals(password.getText())) {
					System.out.println("Login successful for Service Provider.");

					// Retrieve additional service provider details
					String getSPQuery = "SELECT * FROM ServiceProvider WHERE serviceProviderID = ?";
					PreparedStatement prepStatement3 = connection.prepareStatement(getSPQuery);
					prepStatement3.setInt(1, id);

					ResultSet resultSet3 = prepStatement3.executeQuery();
					if (resultSet3.next()) {
						String emailSP = resultSet3.getString("email");
						String agencyName = resultSet3.getString("travelAgencyName");
						String serviceType = resultSet3.getString("serviceType");

						serviceProvider = new ServiceProvider(id, emailSP, username.getText(), agencyName, serviceType);

						// Redirect to the service provider home screen
						screenController.switchToSPHome(event, serviceProvider);
					} else {
						System.out.println("Service provider details not found.");
					}
				} else {
					System.out.println("Incorrect password for Service Provider.");
					errTextLogin.setText("Incorrect password");
				}
			} else {
				System.out.println("Username not found in serviceProviderAuth.");
				errTextLogin.setText("Username not found");
			}
		}
	}

	@FXML
	public void createNewAccount(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToRegisterScene(event);
	}

	public void BacktoLogin(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToLoginScene(event);
	}

	public void RegisterServiceProvider(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		Connection connection = dbHandler.connect();

		String insertQuery0 = "INSERT INTO serviceProviderAuth(username, password) VALUES (?,?)";
		String insertQuery1 = "INSERT INTO ServiceProvider(serviceProviderID, email, travelAgencyName, rating, serviceType) VALUES (?,?,?,?,?)";

		if (!reg_password.getText().equals(password_re.getText())) {
			System.out.println("Incorrect Password: " + reg_password.getText() + " " + password_re.getText());
		}
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery0, Statement.RETURN_GENERATED_KEYS);

		prepStatement.setString(1, reg_username.getText());
		prepStatement.setString(2, reg_password.getText());
		System.out.println("executing statement 1");
		int affectedRows = prepStatement.executeUpdate();
		int userID = -1;
		if (affectedRows > 0) {
			ResultSet generatedKeys = prepStatement.getGeneratedKeys();

			if (generatedKeys.next()) {
				userID = generatedKeys.getInt(1);
			} else {
				System.out.println("Key generation error");
				return;
			}

			prepStatement.close();
		} else {

			prepStatement.close();
			connection.close();
			return;
		}

		PreparedStatement prepStatement1 = connection.prepareStatement(insertQuery1);

		prepStatement1.setInt(1, userID);
		prepStatement1.setString(2, email.getText());
		prepStatement1.setString(3, agencyName.getText());
		prepStatement1.setInt(4, 0);
		prepStatement1.setString(5, ComboSTypes.getValue());
		System.out.println("executing statement 2");
		if (prepStatement1.executeUpdate() > 0) {
			System.out.println("successful operation :: adding new service provieder");
			prepStatement1.close();
			serviceProvider = new ServiceProvider(userID, email.getText(), reg_username.getText(), agencyName.getText(),
					ComboSTypes.getValue());

		} else {
			System.out.println("failed operation :: adding new service provieder");
			prepStatement1.close();
			connection.close();
			return;
		}
		System.out.println("switching scenes");

		screenController.switchToSPHome(event, serviceProvider);

	}

	public void onUpdateComboBox() {
		System.out.println("combo box value changed");
		String selc = ComboSTypes.getValue();
		if (!serviceInfoPane.isVisible()) {
			serviceInfoPane.setVisible(true);
		}
		if (selc.equals("Hotel")) {
			serviceEnterText.setText("Enter Your Hotel Name:");
			serviceEnterImage.setGlyphName("HOTEL");
		} else {
			serviceEnterText.setText("Enter Your Travel Agency Name:");
			serviceEnterImage.setGlyphName("BUS");
		}
	}

	// utils
	public void RegisterCustomer(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		Connection connection = dbHandler.connect();

		// Step 1: Insert customer credentials into customerAuth table
		String insertQuery0 = "INSERT INTO customerAuth(username, password) VALUES (?, ?)";
		String insertQuery1 = "INSERT INTO Customer(CustomerID, name, email, phoneNo) VALUES (?, ?, ?, ?)";

		// Check if passwords match
		if (!reg_CUS_password.getText().equals(password_re_Cus.getText())) {
			System.out.println("Password mismatch: " + reg_CUS_password.getText() + " != " + password_re_Cus.getText());
			return;
		}

		// Insert credentials into customerAuth
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery0, Statement.RETURN_GENERATED_KEYS);
		prepStatement.setString(1, reg_CUS_username.getText());
		prepStatement.setString(2, reg_CUS_password.getText());
		System.out.println("Executing statement 1: Inserting credentials");

		int affectedRows = prepStatement.executeUpdate();
		int userID = -1;
		if (affectedRows > 0) {
			ResultSet generatedKeys = prepStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				userID = generatedKeys.getInt(1);
			} else {
				System.out.println("Failed to retrieve generated userID");
				prepStatement.close();
				connection.close();
				return;
			}
			prepStatement.close();
		} else {
			System.out.println("Failed to insert customer credentials");
			prepStatement.close();
			connection.close();
			return;
		}

		// Step 2: Insert customer details into Customer table
		PreparedStatement prepStatement1 = connection.prepareStatement(insertQuery1);
		prepStatement1.setInt(1, userID); // Use the generated userID
		prepStatement1.setString(2, reg_name.getText());
		prepStatement1.setString(3, email_Cus.getText());
		prepStatement1.setString(4, phoneNo.getText());
		System.out.println("Executing statement 2: Inserting customer details");

		if (prepStatement1.executeUpdate() > 0) {
			System.out.println("Customer registration successful");

			// Create a Customer object
			customer = new Customer(userID, reg_name.getText(), email_Cus.getText(), phoneNo.getText(),
					reg_CUS_username.getText());
			prepStatement1.close();

			// Step 3: Switch to customer home screen
			System.out.println("Switching scenes to customer home");
			screenController.switchToCusHome(event, customer);
		} else {
			System.out.println("Failed to insert customer details");
			prepStatement1.close();
			connection.close();
			return;
		}

		connection.close();
	}

	// utils

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void exitApplication() {
		Platform.exit();
	}

}
