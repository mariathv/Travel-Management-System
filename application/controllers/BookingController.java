package application.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import application.DAO.BookingDAO;
import application.Model.HotelBooking;
import application.Model.TravelBooking;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BookingController {
    @FXML
    ScrollPane bookingsScroll;
    @FXML
    HBox sampleHBOX;
    @FXML
    VBox vBoxBookings;

    List<TravelBooking> bookings;
    List<HotelBooking> HotelBookings;

    @FXML
    private void initialize() throws IOException {
        bookingsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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
        BookingDAO dao = new BookingDAO();
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
                            booking.getBookingDate(), String.valueOf(booking.getTotalPrice()));

                    vBoxBookings.getChildren().add(hbox);
                }
            }
        }
    }

}
