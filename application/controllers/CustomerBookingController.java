package application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.Managers.BookingManager;
import application.Managers.ServiceManager;
import application.Model.Customer;
import application.Model.HotelBooking;
import application.Model.TravelBooking;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CustomerBookingController {
    @FXML
    ScrollPane bookingsScroll, scrollPaneBooking;
    @FXML
    HBox sampleHBOX;
    @FXML
    VBox vBoxBookings, vbox;
    @FXML
    Button ongoingBTN, doneBTN, cancel, feedbackBtn;
    @FXML
    Text serviceNo, textNumber, errf, errf1;
    @FXML
    Pane viewBookingsPane, infoPanelBooking, activeSidePane, historySidePane;

    String CancelType;
    String Cancelbookingid;

    @FXML
    private FontAwesomeIcon star1, star2, star3, star4, star5;
    @FXML
    private TextArea feedbackmsg;

    List<TravelBooking> bookings;
    List<HotelBooking> HotelBookings;

    Customer customer;

    int selectedTab = -1;

    ServiceManager serviceManager = new ServiceManager();

    String typeService;
    int serviceID;

    @FXML
    private void initialize() throws IOException {
        bookingsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        bookingsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneBooking.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        activeSidePane.setVisible(false);
        historySidePane.setVisible(false);

        setStarClickListener(star1, 1);
        setStarClickListener(star2, 2);
        setStarClickListener(star3, 3);
        setStarClickListener(star4, 4);
        setStarClickListener(star5, 5);

        ongoingBTN.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        doneBTN.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        cancel.setVisible(false);
        serviceNo.setVisible(false);
        textNumber.setVisible(false);

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

    private void viewFeedbackPanel() {
        activeSidePane.setVisible(false);
        historySidePane.setVisible(true);
    }

    private void toggleButtonState(Button activeBtn, Button inactiveBtn, int tabNumber)
            throws ClassNotFoundException, SQLException {
        if (selectedTab == tabNumber)
            return;
        activeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");

        inactiveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        selectedTab = tabNumber;
        activeSidePane.setVisible(false);
        historySidePane.setVisible(false);

        loadData();
    }

    private int getFilledStarCount() {
        int count = 0;

        if (star1.getFill().equals(Color.BLACK))
            count++;
        if (star2.getFill().equals(Color.BLACK))
            count++;
        if (star3.getFill().equals(Color.BLACK))
            count++;
        if (star4.getFill().equals(Color.BLACK))
            count++;
        if (star5.getFill().equals(Color.BLACK))
            count++;

        return count;
    }

    private void setStarClickListener(FontAwesomeIcon star, int starIndex) {
        star.setOnMouseClicked(event -> updateStarRating(starIndex));
    }

    private void updateStarRating(int clickedStarIndex) {
        resetStars();

        for (int i = 1; i <= clickedStarIndex; i++) {
            getStarByIndex(i).setFill(Color.BLACK);
        }
    }

    private void resetStars() {
        star1.setFill(Color.WHITE);
        star2.setFill(Color.WHITE);
        star3.setFill(Color.WHITE);
        star4.setFill(Color.WHITE);
        star5.setFill(Color.WHITE);
    }

    private FontAwesomeIcon getStarByIndex(int index) {
        switch (index) {
            case 1:
                return star1;
            case 2:
                return star2;
            case 3:
                return star3;
            case 4:
                return star4;
            case 5:
                return star5;
            default:
                throw new IllegalArgumentException("Invalid star index: " + index);
        }
    }

    public void giveFeedback() {
        if (getFilledStarCount() <= 0) {
            errf.setText("* Required field (rating)");
            return;
        }

        String message = "";
        if (feedbackmsg != null) {
            message = feedbackmsg.getText();
        }
        boolean flag;
        if (typeService != "Hotel") {
            flag = serviceManager.giveTravelFeedback(serviceID, getFilledStarCount(), message, customer.getCustomerID(),
                    customer.getUsername());
        } else {
            flag = serviceManager.giveHotelFeedback(serviceID, getFilledStarCount(), message, customer.getCustomerID(),
                    customer.getUsername());
        }

        if (!flag) {
            errf.setText("Error giving feedback");
        } else {
            errf1.setText("Feedback submitted");

        }

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
        if (selectedTab == 1 || selectedTab == -1)
            query = "SELECT bookingID, bookingDate, TotalPrice AS price, serviceType, serviceID AS id, status, 'BUS' AS type "
                    +
                    "FROM travelbooking WHERE customerID = ? AND status = 1 " +
                    "UNION ALL " +
                    "SELECT bookingID, bookingDate, price, 'Hotel' AS serviceType, listingID AS id,status,'BED' AS type "
                    +
                    "FROM hotelbooking WHERE customerID = ? AND status = 1 ";
        else
            query = "SELECT bookingID, bookingDate, TotalPrice AS price,serviceType, serviceID AS id,  status, 'BUS' AS type "
                    +
                    "FROM travelbooking WHERE customerID = ? AND status = 0 " +
                    "UNION ALL " +
                    "SELECT bookingID, bookingDate, price,'Hotel' AS serviceType, listingID AS id, status, 'BED' AS type "
                    +
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
                        // String type = resultSet.getString("type");
                        int status = resultSet.getInt("status");
                        int serviceID = resultSet.getInt("id");
                        System.out.println("fetched serviceID" + serviceID);
                        String serviceType = resultSet.getString("serviceType");
                        String type1 = "";
                        if (serviceType.equals("Bus"))
                            type1 = "BUS";
                        else if (serviceType.equals("Train"))
                            type1 = "TRAIN";
                        else if (serviceType.equals("Flight"))
                            type1 = "PLANE";
                        else if (serviceType.equals("Hotel"))
                            type1 = "HOTEL";
                        allBookingItem.setData(x, bookingID, bookingDate, price, type1, status);

                        String type = type1;

                        if (selectedTab != 2)
                            hbox.setOnMouseClicked(event -> {
                                activeSidePane.setVisible(true);
                                historySidePane.setVisible(false);

                                Cancelbookingid = bookingID;
                                CancelType = type;

                                serviceNo.setVisible(true);
                                textNumber.setVisible(true);
                                try {
                                    if (serviceType.equals("Bus")) {
                                        textNumber.setText("Bus No. ");
                                        serviceNo.setText(serviceManager.getBusNumber(serviceID));
                                    } else if (serviceType.equals("Train")) {
                                        textNumber.setText("Train No. ");
                                        serviceNo.setText(serviceManager.getTrainNumber(serviceID));
                                    } else if (serviceType.equals("Flight")) {
                                        textNumber.setText("Flight No. ");
                                        serviceNo.setText(serviceManager.getFlightNumber(serviceID));
                                    } else {
                                        textNumber.setText("Room Type: ");
                                        String roomType = serviceManager.getRoomType(serviceID);
                                        System.out.println("room type : " + roomType);
                                        serviceNo.setText(roomType);
                                    }
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
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
                        else {
                            hbox.setOnMouseClicked(event -> {

                                activeSidePane.setVisible(false);
                                historySidePane.setVisible(true);

                                typeService = serviceType;
                                this.serviceID = serviceID;

                                viewFeedbackPanel();
                                resetFeedbackFields();

                                // Reset the style of the previously selected HBox
                                if (currentlySelectedHBox != null) {
                                    currentlySelectedHBox.setStyle("-fx-background-color: transparent;");
                                }

                                // Highlight the selected HBox
                                hbox.setStyle("-fx-background-color: #C9DFE1;");

                                // Update the reference to the currently selected HBox
                                currentlySelectedHBox = hbox;
                            });
                        }

                        vbox.getChildren().add(hbox);
                        x++;
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }
    }

    public void resetFeedbackFields() {
        feedbackmsg.clear();
        errf.setText("");
        errf1.setText("");

        resetStars();
    }

    public void cancelbooking() {
        // Retrieve input values

        try {
            int bookingid = Integer.parseInt(Cancelbookingid); // Parse booking ID as an integer

            // Database query logic based on type
            if (CancelType.equalsIgnoreCase("HOTEL")) {
                updateBookingStatus("hotelbooking", bookingid);
            } else {
                updateBookingStatus("travelbooking", bookingid);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID. Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace(); // Log any unexpected errors
        }
    }

    private void updateBookingStatus(String tableName, int bookingid) throws SQLException, ClassNotFoundException {

        String query = "UPDATE " + tableName + " SET status = 2 WHERE bookingID = ?";

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