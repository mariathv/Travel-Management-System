package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import application.Model.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
	private Text nameCus; 
	@FXML
	private AnchorPane mainPanel_cus,selection,home; 
	@FXML
	private Button nav_home_cus, nav_notifs_cus, nav_profile_cus, nav_service_cus,nav_Bookings;
	@FXML
	private Text cusID,profileName,profileEmail,profilePhoneNum,total,travel,hotel;
	@FXML
	private Pane modify_basePane, modify_passwordPane, modify_phonePane;
	@FXML
	private TextField newUsername;
	@FXML
	private PasswordField newPass;
	@FXML
	private VBox NewVbox;
	
	public void setCustomer(Customer customer) {
        this.customer = customer;
        //updateDashboard();
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
	    NewVbox.getChildren().clear();
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

	                    NewVbox.getChildren().add(hbox);
	                    x++;
	                } catch (IOException io) {
	                    io.printStackTrace();
	                }
	            }
	        }
	    }
	}
	   
	
	public void Logout() {
	    try {
	        // Close the current stage
	        Stage primaryStage = AppController.getPrimaryStage();
	        primaryStage.close();

	        // Restart the application
	        Platform.runLater(() -> {
	            try {
	                Main mainApp = new Main();
	                Stage newStage = new Stage();
	                mainApp.start(newStage); // Call the start() method of Main
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadHomePane_cus() {
		try {
			
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/new.fxml"));
	    	AnchorPane newPanel = loader.load();
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_home_cus.setStyle("-fx-background-color:  #212832;");
	        updateDashboard();
	        currentTab=1;
	        loadData();
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	public void loadServiceSearch_cus() { //service pane
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchTravel.fxml"));
	    	AnchorPane newPanel = loader.load();
	    	ServiceControllerCus serviceC = loader.getController();
			serviceC.setCustomer(customer);
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_service_cus.setStyle("-fx-background-color:  #212832;");
	        currentTab=2;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadBooking_cus() { //service pane
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/Bookings.fxml"));
	    	AnchorPane newPanel = loader.load();
	    	BookingController bc = loader.getController();
			bc.setCustomer(customer);
			bc.loadData();
            mainPanel_cus.getChildren().setAll(newPanel);
	        changeBackButtonBG();
	        nav_Bookings.setStyle("-fx-background-color:  #212832;");
	        currentTab=5;
	        
	
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
			nav_profile_cus.setStyle("-fx-background-color:  #212832;");

		} catch (Exception e) {
			e.printStackTrace();
		}

		currentTab = 4;
	}
	
	/*
	public void loadData() throws SQLException, ClassNotFoundException {
	    Connection connection = dbHandler.connect();
	    String query;
	    vbox.getChildren().clear();
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
	                    if(status == 0)
	                    	continue;
	                    allBookingItem.setData(x, bookingID, bookingDate, price, type, status);

	                    vbox.getChildren().add(hbox);
	                    x++;
	                } catch (IOException io) {
	                    io.printStackTrace();
	                }
	            }
	        }
	    }
	}
	*/
	private void changeBackButtonBG() {
		switch(currentTab) {
		case 1: nav_home_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 2: nav_service_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 3: nav_notifs_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 4: nav_profile_cus.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		case 5: nav_Bookings.setStyle("-fx-background-color:  #393D46;"); System.out.println("changed bg color"); break;
		}
	}
	
	private void updateDashboard() {
	    if (usernameCus != null && nameCus != null && customer != null) { 
	        usernameCus.setText(customer.getUsername());
	        nameCus.setText(customer.getName());
	    } else {
	        System.out.println("Error: usernameCus, nameCus, or customer is null.");
	    }
	}
	
	public void exitApplication() {
	    Platform.exit(); 
	}
	
}
