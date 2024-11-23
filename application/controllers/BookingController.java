package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.Managers.BookingManager;
import application.Model.Customer;
import application.Model.HotelBooking;
import application.Model.TravelBooking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BookingController {
    @FXML
    ScrollPane bookingsScroll, scrollPaneBooking;
    @FXML
    HBox sampleHBOX;
    @FXML
    VBox vBoxBookings, vbox;
    @FXML
    Button ongoingBTN, doneBTN, cancel;

    String CancelType;
    String Cancelbookingid;

    List<TravelBooking> bookings;
    List<HotelBooking> HotelBookings;

    Customer customer;

    int selectedTab = -1;

    @FXML
    private void initialize() throws IOException {
        bookingsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneBooking.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ongoingBTN.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        doneBTN.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        cancel.setVisible(false);

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
        loadData();
    }

    public void setCustomer(Customer c) {
        customer = c;
    }

    public void loadBookingData(int serviceID, boolean tFlag) throws IOException, ClassNotFoundException, SQLException {

        vBoxBookings.getChildren().clear();
        FXMLLoader initLoader = new FXMLLoader();
        initLoader.setLocation(getClass().getResource("../scenes/components/booking_item.fxml"));
        HBox hboxinit = initLoader.load();

        vBoxBookings.getChildren().add(hboxinit);

        Separator separator = new Separator();
        separator.setPrefWidth(400);
        vBoxBookings.getChildren().add(separator);
        separator.setStyle("-fx-border-color: #1e353f; -fx-border-width: 2;");
        // Get the list of bookings from the DAO based on the provided serviceID
        BookingManager dao = new BookingManager();
        System.out.println("Fetching Data");
        if (tFlag)
            bookings = dao.getBookingsByServiceProvider(serviceID);
        else
            HotelBookings = dao.getHotelBookingsByServiceProvider(serviceID);

        System.out.println("Loading Data");
        int serlnum = 1;
        if ((tFlag && bookings.isEmpty()) || (!tFlag && HotelBookings.isEmpty())) {
            Text noBookingsText = new Text("No bookings available for this service provider.");
            vBoxBookings.getChildren().add(noBookingsText);
        } else {
            if (tFlag) {
                for (TravelBooking booking : bookings) {
                    FXMLLoader fxmlloader = new FXMLLoader();
                    fxmlloader.setLocation(getClass().getResource("../scenes/components/booking_item.fxml"));
                    HBox hbox = fxmlloader.load(); // Load the FXML for each booking item

                    bookingItemController itemController = fxmlloader.getController();

                    itemController.setData(serlnum++, booking.getUsername(),
                            booking.getBookingDate(), String.valueOf(booking.getTotalPrice()));

                    vBoxBookings.getChildren().add(hbox);
                }
            } else {
                for (HotelBooking booking : HotelBookings) {
                    FXMLLoader fxmlloader = new FXMLLoader();
                    fxmlloader.setLocation(getClass().getResource("../scenes/components/booking_item.fxml"));
                    HBox hbox = fxmlloader.load(); // Load the FXML for each booking item

                    bookingItemController itemController = fxmlloader.getController();

                    itemController.setData(serlnum++, booking.getUsername(),
                            booking.getBookingDate(), String.valueOf(booking.getPrice()));

                    vBoxBookings.getChildren().add(hbox);
                }
            }
        }
    }

    private HBox currentlySelectedHBox; // Track the selected HBox

    public void loadData() throws SQLException, ClassNotFoundException {
        Connection connection = dbHandler.connect();
        String query;
        vbox.getChildren().clear();
        int x = 1; // Counter for serial numbers

        // Unified query using UNION
        if (selectedTab == 1 || selectedTab == -1)
            query = "SELECT bookingID, bookingDate, TotalPrice AS price, status, 'BUS' AS type " +
                    "FROM travelbooking WHERE customerID = ? AND status = 1 " +
                    "UNION ALL " +
                    "SELECT bookingID, bookingDate, price, status, 'BED' AS type " +
                    "FROM hotelbooking WHERE customerID = ? AND status = 1 ";
        else
            query = "SELECT bookingID, bookingDate, TotalPrice AS price, status, 'BUS' AS type " +
                    "FROM travelbooking WHERE customerID = ? AND status = 0 " +
                    "UNION ALL " +
                    "SELECT bookingID, bookingDate, price, status, 'BED' AS type " +
                    "FROM hotelbooking WHERE customerID = ? AND status = 0 ";

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

                        allBookingItem.setData(x, bookingID, bookingDate, price, type, status);

                        // Pass `bookingID` and `type` to the event handler
                        hbox.setOnMouseClicked(event -> {
                            Cancelbookingid = bookingID;
                            CancelType = type;

                            cancel.setVisible(true);

                            // Reset the style of the previously selected HBox
                            if (currentlySelectedHBox != null) {
                                currentlySelectedHBox.setStyle("-fx-background-color: transparent;");
                            }

                            // Highlight the selected HBox
                            hbox.setStyle("-fx-background-color: #C9DFE1;");

                            // Update the reference to the currently selected HBox
                            currentlySelectedHBox = hbox;
                        });

                        vbox.getChildren().add(hbox);
                        x++;
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }
    }

    public void cancelbooking() {
        // Retrieve input values

        try {
            int bookingid = Integer.parseInt(Cancelbookingid); // Parse booking ID as an integer

            // Database query logic based on type
            if (CancelType.equalsIgnoreCase("BED")) {
                updateBookingStatus("hotelbooking", bookingid);
            } else if (CancelType.equalsIgnoreCase("BUS")) {
                updateBookingStatus("travelbooking", bookingid);
            } else {
                System.out.println("Invalid booking type. Please enter 'hotel' or 'travel'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID. Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace(); // Log any unexpected errors
        }
    }

    private void updateBookingStatus(String tableName, int bookingid) throws SQLException, ClassNotFoundException {

        String query = "UPDATE " + tableName + " SET status = 0 WHERE bookingID = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingid);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                loadData();
                System.out.println(
                        "Booking ID " + bookingid + " in table '" + tableName + "' has been successfully cancelled.");
            } else {
                System.out.println("Booking ID " + bookingid + " not found in table '" + tableName + "'.");
            }
        }
    }

}
