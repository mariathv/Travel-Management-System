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
	private Pane mainRegPanel, nextRegPanel, serviceInfoPane;
	@FXML
	private ComboBox<String> ComboSTypes;

	@FXML
	private Text serviceEnterText, errText, errTextLogin;
	@FXML
	private FontAwesomeIcon serviceEnterImage;

	ScreenController screenController = new ScreenController();

	ServiceProvider serviceProvider;

	private Stage primaryStage;

	public void selectServiceType() throws ClassNotFoundException, SQLException {
		if (!isUsernameTaken(reg_username.getText())) {
			mainRegPanel.setVisible(false);
			nextRegPanel.setVisible(true);

			ComboSTypes.getItems().add("Bus");
			ComboSTypes.getItems().add("Train");
			ComboSTypes.getItems().add("Flight");
			ComboSTypes.getItems().add("Hotel");
		}
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

	public boolean isUsernameTaken(String username) throws SQLException, ClassNotFoundException {

		Connection connection = dbHandler.connect();

		// SQL query to check if the username already exists
		String checkUsernameQuery = "SELECT COUNT(*) FROM serviceProviderAuth WHERE username = ?";
		PreparedStatement checkUsernameStmt = connection.prepareStatement(checkUsernameQuery);
		checkUsernameStmt.setString(1, username);

		ResultSet rs = checkUsernameStmt.executeQuery();

		boolean usernameExists = false;
		if (rs.next()) {
			usernameExists = rs.getInt(1) > 0; // If count > 0, username exists
		}

		rs.close();
		checkUsernameStmt.close();
		connection.close();

		if (usernameExists) {
			errText.setText("This Username already exists");
		}

		return usernameExists;
	}

	public void login_sp(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		Connection connection = dbHandler.connect();

		// login implementation
		String getPassQuery = "SELECT * FROM serviceProviderAuth WHERE username = ?";
		PreparedStatement prepStatement = connection.prepareStatement(getPassQuery);
		prepStatement.setString(1, username.getText());

		System.out.println("auth controlling username " + username);

		ResultSet resultSet = prepStatement.executeQuery();
		if (resultSet.next()) {
			String retrievedPassword = resultSet.getString("password");
			int id = resultSet.getInt("userID");
			if (retrievedPassword.equals(password.getText())) {
				System.out.println("Login successful.");

				String getPassQuery2 = "SELECT * FROM ServiceProvider WHERE serviceProviderID = ?";
				PreparedStatement prepStatement2 = connection.prepareStatement(getPassQuery2);
				prepStatement2.setInt(1, id);
				ResultSet resultSet2 = prepStatement2.executeQuery();
				if (resultSet2.next()) {
					String emailSP = resultSet2.getString("email");
					String agencyName = resultSet2.getString("travelAgencyName");
					String serviceType = resultSet2.getString("serviceType");
					serviceProvider = new ServiceProvider(id, emailSP, username.getText(), agencyName, serviceType);

				} else {
					System.out.println("Incorrect password.");
					errTextLogin.setText("Incorrect Password");
				}

				screenController.switchToSPHome(event, serviceProvider);
			} else {
				System.out.println("Incorrect password.");
				errTextLogin.setText("Incorrect Password");
			}
		} else {
			System.out.println("Username not found.");
			errTextLogin.setText("Username does not exist.");
		}

	}

	@FXML
	public void createNewAccount(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToRegisterScene(event);
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

	// utils

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void exitApplication() {
		Platform.exit();
	}

}
