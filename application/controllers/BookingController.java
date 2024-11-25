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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BookingController {
    @FXML
    ScrollPane bookingsScroll;
    @FXML
    HBox sampleHBOX;
    @FXML
    VBox vBoxBookings, vbox;
    @FXML
    private FontAwesomeIcon TYPE;
    String CancelType;
    String Cancelbookingid;

    List<TravelBooking> bookings;
    List<HotelBooking> HotelBookings;

    Customer customer;

    @FXML
    private void initialize() throws IOException {
        bookingsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void setCustomer(Customer c) {
        customer = c;
    }

    public void loadBookingData(int serviceID, boolean tFlag, String type)
            throws IOException, ClassNotFoundException, SQLException {

        vBoxBookings.getChildren().clear();

        Separator separator = new Separator();
        separator.setPrefWidth(400);
        vBoxBookings.getChildren().add(separator);
        separator.setStyle("-fx-border-color: #1e353f; -fx-border-width: 2;");
        // Get the list of bookings from the DAO based on the provided serviceID
        BookingManager dao = new BookingManager();
        System.out.println("Fetching Data");
        if (tFlag) {
            if (type.equals("Bus"))
                TYPE.setGlyphName("BUS");
            else if (type.equals("Train"))
                TYPE.setGlyphName("TRAIN");
            else
                TYPE.setGlyphName("PLANE");
            bookings = dao.getBookingsByServiceProvider(serviceID);
        } else {
            TYPE.setGlyphName("HOTEL");
            HotelBookings = dao.getHotelBookingsByServiceProvider(serviceID);
        }

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
                            booking.getBookingDate(), String.valueOf(booking.getTotalPrice()), booking.getStatus());

                    vBoxBookings.getChildren().add(hbox);
                }
            } else {
                for (HotelBooking booking : HotelBookings) {
                    FXMLLoader fxmlloader = new FXMLLoader();
                    fxmlloader.setLocation(getClass().getResource("../scenes/components/booking_item.fxml"));
                    HBox hbox = fxmlloader.load(); // Load the FXML for each booking item

                    bookingItemController itemController = fxmlloader.getController();

                    itemController.setData(serlnum++, booking.getUsername(),
                            booking.getBookingDate(), String.valueOf(booking.getPrice()), 1);

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

                        allBookingItem.setData(x, bookingID, bookingDate, price, type, status);

                        // Pass `bookingID` and `type` to the event handler
                        hbox.setOnMouseClicked(event -> {
                            Cancelbookingid = bookingID;
                            CancelType = type;

                            // Reset the style of the previously selected HBox
                            if (currentlySelectedHBox != null) {
                                currentlySelectedHBox.setStyle("-fx-background-color: transparent;");
                            }

                            // Highlight the selected HBox
                            hbox.setStyle("-fx-background-color: #393351;");

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