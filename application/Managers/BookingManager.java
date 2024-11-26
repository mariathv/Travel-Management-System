package application.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Model.HotelBooking;
import application.Model.TravelBooking;
import application.controllers.dbHandler;

public class BookingManager {
    List<TravelBooking> bookings = new ArrayList<>();
    List<HotelBooking> hotelbookings = new ArrayList<>();

    public List<TravelBooking> getBookingsByServiceProvider(int serviceID) throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT * FROM TravelBooking WHERE serviceID = ? AND status != 2";

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
                int status = rs.getInt("status");

                TravelBooking booking = new TravelBooking(bookingID, customerID, serviceID, totalPrice, serviceType,
                        bookingDate, username);
                booking.setStatus(status);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return bookings;
    }

    public List<TravelBooking> getRecentBookingsByServiceProvider(int serviceProviderID)
            throws ClassNotFoundException, SQLException {
        List<TravelBooking> bookings = new ArrayList<>(); // Make sure the bookings list is initialized
        Connection connection = dbHandler.connect();
        String query = "SELECT * "
                +
                "FROM travelbooking cb " +
                "JOIN travelservice ts ON cb.serviceID = ts.serviceID " +
                "WHERE ts.serviceProviderID = ? AND cb.status = 1 " + // Changed to serviceProviderID
                "ORDER BY cb.bookingDate DESC " +
                "LIMIT 5";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, serviceProviderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookingID = rs.getInt("bookingID");
                int customerID = rs.getInt("customerID");
                int totalPrice = rs.getInt("TotalPrice");
                String serviceType = rs.getString("serviceType");
                String bookingDate = rs.getString("bookingDate");
                String username = rs.getString("username");
                int serviceID = rs.getInt("serviceID");

                TravelBooking booking = new TravelBooking(bookingID, customerID, serviceID, totalPrice, serviceType,
                        bookingDate, username);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close(); // Ensure connection is closed even if exception occurs
        }
        return bookings;
    }

    public List<HotelBooking> getHotelBookingsByServiceProvider(int serviceID)
            throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT * FROM HotelBooking WHERE listingID = ? AND status != 2";

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

    public List<HotelBooking> getRecentHotelBookingsByServiceProvider(int serviceProviderID)
            throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT * from hotelbooking hb JOIN hotelservice hs ON hb.listingID = hs.HotelServiceID WHERE hs.ServiceProviderID = ? AND hb.status = 1 ORDER BY bookingDate DESC LIMIT 5";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceProviderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int listingID = rs.getInt("listingID");
                int customerID = rs.getInt("customerID");
                int price = rs.getInt("price");
                String roomType = rs.getString("roomType");
                String bookingDate = rs.getString("bookingDate");
                String username = rs.getString("username");
                int serviceID = rs.getInt("listingID");

                HotelBooking booking = new HotelBooking(listingID, customerID, serviceID, price, roomType,
                        bookingDate, username);
                hotelbookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelbookings;
    }

    public int getTotalEarnings(int spID) throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT SUM(tb.TotalPrice) AS totalEarnings, tb.bookingDate FROM travelbooking tb JOIN travelservice ts ON tb.serviceID = ts.serviceID WHERE ts.serviceProviderID = ? GROUP BY tb.bookingDate ORDER BY tb.bookingDate";
        int total = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, spID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                total = rs.getInt("totalEarnings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getHotelTotalEarnings(int spID) throws ClassNotFoundException, SQLException {
        Connection connection = dbHandler.connect();
        String sql = "SELECT SUM(hb.price) AS totalEarnings, hb.bookingDate FROM hotelbooking hb JOIN hotelservice hs ON hb.listingID = hs.HotelServiceID WHERE hs.ServiceProviderID = ? GROUP BY hb.bookingDate ORDER BY hb.bookingDate";
        int total = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, spID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                total = rs.getInt("totalEarnings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

}
