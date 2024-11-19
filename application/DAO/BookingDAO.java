package application.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Model.HotelBooking;
import application.Model.TravelBooking;
import application.controllers.dbHandler;

public class BookingDAO {
    List<TravelBooking> bookings = new ArrayList<>();
    List<HotelBooking> hotelbookings = new ArrayList<>();

    public List<TravelBooking> getBookingsByServiceProvider(int serviceID) throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT * FROM TravelBooking WHERE serviceID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookingID = rs.getInt("bookingID");
                int customerID = rs.getInt("customerID");
                int totalPrice = rs.getInt("TotalPrice");
                String serviceType = rs.getString("serviceType");
                String bookingDate = rs.getString("bookingDate");
                String username = rs.getString("username");

                TravelBooking booking = new TravelBooking(bookingID, customerID, serviceID, totalPrice, serviceType,
                        bookingDate, username);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return bookings;
    }

    public List<HotelBooking> getHotelBookingsByServiceProvider(int serviceID)
            throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT * FROM HotelBooking WHERE serviceID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int listingID = rs.getInt("listingID");
                int customerID = rs.getInt("customerID");
                int price = rs.getInt("price");
                String roomType = rs.getString("roomType");
                String bookingDate = rs.getString("bookingDate");
                String username = rs.getString("username");

                HotelBooking booking = new HotelBooking(listingID, customerID, serviceID, price, roomType,
                        bookingDate, username);
                hotelbookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelbookings;
    }
}
