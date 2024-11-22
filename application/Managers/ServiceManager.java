package application.Managers;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.Model.ServiceProvider;
import application.controllers.dbHandler;

public class ServiceManager {

    // Defining the Feedback class to store the feedback details
    public static class Feedback {
        private int serviceID;
        private int rating;
        private String comment;
        private int customerID;
        private String customerUsername;

        public Feedback(int serviceID, int rating, String comment, int customerID, String customerUsername) {
            this.serviceID = serviceID;
            this.rating = rating;
            this.comment = comment;
            this.customerID = customerID;
            this.customerUsername = customerUsername;
        }

        public int getServiceID() {
            return serviceID;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public int getCustomerID() {
            return customerID;
        }

        public String getCustomerUsername() {
            return customerUsername;
        }
    }

    // Method to get all feedbacks for a specific service ID
    public List<Feedback> getFeedbacksByServiceID(int serviceID) throws SQLException, ClassNotFoundException {
        List<Feedback> feedbackList = new ArrayList<>();

        Connection connection = dbHandler.connect();

        String query = "SELECT * FROM servicefeedback WHERE serviceID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, serviceID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int rating = resultSet.getInt("rating");
            String comment = resultSet.getString("comment");
            int customerID = resultSet.getInt("customerID");
            String customerUsername = resultSet.getString("customerUsername");

            Feedback feedback = new Feedback(serviceID, rating, comment, customerID, customerUsername);
            feedbackList.add(feedback);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return feedbackList;
    }

    public List<Feedback> getHotelFeedbacksByServiceID(int serviceID) throws SQLException, ClassNotFoundException {
        List<Feedback> feedbackList = new ArrayList<>();

        Connection connection = dbHandler.connect();

        String query = "SELECT * FROM hotelfeedback WHERE serviceID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, serviceID);
        System.out.println(serviceID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int rating = resultSet.getInt("rating");
            String comment = resultSet.getString("comment");
            int customerID = resultSet.getInt("customerID");
            String customerUsername = resultSet.getString("customerUsername");

            Feedback feedback = new Feedback(serviceID, rating, comment, customerID, customerUsername);
            feedbackList.add(feedback);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return feedbackList;
    }

    public int deleteServiceFeedback(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM servicefeedback WHERE serviceID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int deleteTravelBooking(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM travelbooking WHERE serviceID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int deleteServiceDetails(int serviceID, String serviceType) throws SQLException, ClassNotFoundException {
        String query = switch (serviceType) {
            case "Bus" -> "DELETE FROM BusService WHERE ServiceID = ?";
            case "Train" -> "DELETE FROM TrainService WHERE ServiceID = ?";
            case "Flight" -> "DELETE FROM FlightService WHERE ServiceID = ?";
            default -> throw new IllegalArgumentException("Unknown service type: " + serviceType);
        };

        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int deleteTravelService(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM TravelService WHERE ServiceID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    // Hotel Service Queries
    public int deleteHotelFeedback(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM servicefeedback WHERE serviceID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int deleteHotelBooking(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM hotelbooking WHERE listingID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int deleteHotelService(int serviceID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM hotelservice WHERE HotelServiceID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceID);
            return prepStatement.executeUpdate();
        }
    }

    public int addCancellationNotifications(int serviceID, String message, boolean tFlag)
            throws SQLException, ClassNotFoundException {
        int rowsAffected = 0;
        String getCustQuery;
        if (tFlag) {
            getCustQuery = "Select customerID FROM travelbooking WHERE serviceID = ?";
        } else {
            getCustQuery = "Select customerID FROM hotelbooking WHERE listingID = ?";
        }
        System.out.println(serviceID);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = today.format(formatter);

        String insertNotificationQuery = "INSERT INTO customernotifications (customerID, message, date) VALUES (?, ?, ?)";

        try (Connection connection = dbHandler.connect();
                PreparedStatement selectStatement = connection.prepareStatement(getCustQuery);
                PreparedStatement insertStatement = connection.prepareStatement(insertNotificationQuery)) {

            selectStatement.setInt(1, serviceID);

            ResultSet resultSet = selectStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No customers found for the given service ID: " + serviceID);
            }
            while (resultSet.next()) {
                int customerID = resultSet.getInt("customerID");

                insertStatement.setInt(1, customerID);
                insertStatement.setString(2, message);
                insertStatement.setString(3, todayString);

                rowsAffected += insertStatement.executeUpdate();
            }
        }

        return rowsAffected;
    }

    public boolean markServiceAsDone(int serviceId) throws ClassNotFoundException {
        String query = "UPDATE travelservice SET status = 'DONE' WHERE serviceID = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, serviceId);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewService(ServiceProvider serviceProvider,
            String depLoc, String depTime, String depDate,
            String arvLoc, String arvTime, String arvDate,
            String SBusNo, String BStationName, String BStationLoc,
            String StktPrice, String GNumber) throws SQLException, ClassNotFoundException {

        String description = String.format(
                "Bus No. %s from %s to %s departing on %s at %s and arriving on %s at %s. \nTicket Price: PKR %s. \nBus station: %s located at %s.",
                SBusNo, depLoc, arvLoc, depDate, depTime, arvDate, arvTime, StktPrice, BStationName, BStationLoc);

        Connection connection = dbHandler.connect();

        String insertQuery = "INSERT INTO TravelService(serviceProviderID, description, serviceType, arrivalTime, departureTime, arrivalLocation, departureLocation, departureDate, arrivalDate, ticketPrice, status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement prepStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        prepStatement.setInt(1, serviceProvider.getServiceProviderID());
        prepStatement.setString(2, description);
        prepStatement.setString(3, serviceProvider.getServiceType());
        prepStatement.setString(4, arvTime);
        prepStatement.setString(5, depTime);
        prepStatement.setString(6, arvLoc);
        prepStatement.setString(7, depLoc);
        prepStatement.setString(8, depDate);
        prepStatement.setString(9, arvDate);
        prepStatement.setInt(10, Integer.parseInt(StktPrice));
        prepStatement.setString(11, "ONGOING");

        int affectedRows = prepStatement.executeUpdate();

        if (affectedRows > 0) {
            ResultSet generatedKeys = prepStatement.getGeneratedKeys();
            int serviceID = -1;
            if (generatedKeys.next()) {
                serviceID = generatedKeys.getInt(1);
            } else {
                return false; // Failed to retrieve service ID.
            }
            prepStatement.close();

            String serviceInsertQuery = switch (serviceProvider.getServiceType()) {
                case "Bus" -> "INSERT INTO BusService(ServiceID, StationName, StationLocation, BusNo) VALUES (?,?,?,?)";
                case "Train" ->
                    "INSERT INTO TrainService(ServiceID, StationName, StationLocation, TrainNumber) VALUES (?,?,?,?)";
                case "Flight" ->
                    "INSERT INTO FlightService(ServiceID, AirportName, AirportLocation, FlightNumber, GateNumber) VALUES (?,?,?,?,?)";
                default -> throw new IllegalArgumentException("Invalid service type.");
            };

            PreparedStatement serviceStatement = connection.prepareStatement(serviceInsertQuery);

            serviceStatement.setInt(1, serviceID);
            serviceStatement.setString(2, BStationName);
            serviceStatement.setString(3, BStationLoc);
            serviceStatement.setString(4, SBusNo);
            if (serviceProvider.getServiceType().equals("Flight")) {
                serviceStatement.setString(5, GNumber);
            }

            int rows = serviceStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Service added successfully.");
                return true;
            }
        }
        return false; // Failure in the operation.
    }

    public boolean updateService(int serviceID, String depLoc, String depTime, String depDate,
            String arvLoc, String arvTime, String arvDate,
            String SBusNo, String BStationName, String BStationLoc,
            String StktPrice, String GNumber, String serviceType) throws SQLException, ClassNotFoundException {
        Connection connection = dbHandler.connect();

        // Update the main TravelService table
        String updateQuery = "UPDATE TravelService SET departureLocation = ?, departureTime = ?, departureDate = ?, " +
                "arrivalLocation = ?, arrivalTime = ?, arrivalDate = ?, ticketPrice = ? WHERE ServiceID = ?";

        PreparedStatement prepStatement = connection.prepareStatement(updateQuery);

        prepStatement.setString(1, depLoc);
        prepStatement.setString(2, depTime);
        prepStatement.setString(3, depDate);
        prepStatement.setString(4, arvLoc);
        prepStatement.setString(5, arvTime);
        prepStatement.setString(6, arvDate);
        prepStatement.setInt(7, Integer.parseInt(StktPrice));
        prepStatement.setInt(8, serviceID);

        int affectedRows = prepStatement.executeUpdate();

        if (affectedRows > 0) {
            // Update the specific service table based on service type
            String specificUpdateQuery = switch (serviceType) {
                case "Bus" ->
                    "UPDATE BusService SET StationName = ?, StationLocation = ?, BusNo = ? WHERE ServiceID = ?";
                case "Train" ->
                    "UPDATE TrainService SET StationName = ?, StationLocation = ?, TrainNumber = ? WHERE ServiceID = ?";
                case "Flight" ->
                    "UPDATE FlightService SET AirportName = ?, AirportLocation = ?, FlightNumber = ?, GateNumber = ? WHERE ServiceID = ?";
                default -> throw new IllegalArgumentException("Invalid service type.");
            };

            PreparedStatement serviceStatement = connection.prepareStatement(specificUpdateQuery);

            serviceStatement.setString(1, BStationName);
            serviceStatement.setString(2, BStationLoc);
            serviceStatement.setString(3, SBusNo);

            if (serviceType.equals("Flight")) {
                serviceStatement.setString(4, GNumber);
                serviceStatement.setInt(5, serviceID);
            } else {
                serviceStatement.setInt(4, serviceID);
            }

            int rows = serviceStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Service updated successfully.");
                return true;
            }
        }
        return false; // Failure in the operation.
    }

    public int getTotalServices(int serviceProviderID) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) AS totalServices FROM TravelService WHERE serviceProviderID = ?";
        try (Connection connection = dbHandler.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, serviceProviderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("totalServices");
            }
        }
        return 0; // Default to 0 if no results
    }

    public int getOnGoingServices(int serviceProviderID) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) AS onGoingServices FROM TravelService WHERE serviceProviderID = ? AND status = 'ONGOING'";
        try (Connection connection = dbHandler.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, serviceProviderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("onGoingServices");
            }
        }
        return 0; // Default to 0 if no results
    }

    public int getTotalBookings(int serviceProviderID) throws SQLException, ClassNotFoundException {
        String query = """
                SELECT COUNT(*) AS totalBookings
                FROM travelbooking
                WHERE serviceID IN (
                    SELECT serviceID
                    FROM TravelService
                    WHERE serviceProviderID = ?
                )
                """;
        try (Connection connection = dbHandler.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, serviceProviderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("totalBookings");
            }
        }
        return 0; // Default to 0 if no results
    }

    public int getServiceProviderRating(int serviceProviderID) throws ClassNotFoundException, SQLException {
        String query = "Select rating from ServiceProvider where serviceProviderID = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, serviceProviderID);
            ResultSet resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("rating");
            }
        }
        return -1; // return -1 on no result
    }

    public int getTotalTravelFeedbacks(int serviceProviderID) {
        String query = """
                SELECT COUNT(*) AS totalFeedbacks
                FROM servicefeedback sf
                JOIN travelservice ts ON sf.serviceID = ts.serviceID
                WHERE ts.serviceProviderID = ?
                """;

        int totalFeedbacks = 0;

        try (Connection connection = dbHandler.connect();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, serviceProviderID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                totalFeedbacks = rs.getInt("totalFeedbacks");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalFeedbacks;
    }

    public int getTotalHotelFeedbacks(int serviceProviderID) {
        String query = """
                SELECT COUNT(*) AS totalFeedbacks
                FROM hotelfeedback sf
                JOIN travelservice ts ON sf.serviceID = ts.serviceID
                WHERE ts.serviceProviderID = ?
                """;

        int totalFeedbacks = 0;

        try (Connection connection = dbHandler.connect();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, serviceProviderID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                totalFeedbacks = rs.getInt("totalFeedbacks");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalFeedbacks;
    }

    public boolean setPhoneNumber(int serviceProviderID, String phoneNum) {
        String query = "UPDATE ServiceProvider SET phoneNum = ? WHERE serviceProviderID = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, phoneNum);
            pstmt.setInt(2, serviceProviderID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCurrentPassword(String pass, int serviceProviderID) {
        String query = "SELECT 1 FROM serviceproviderauth WHERE userID = ? AND password = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement prep = connection.prepareStatement(query)) {

            prep.setInt(1, serviceProviderID);
            prep.setString(2, pass);

            try (ResultSet resultSet = prep.executeQuery()) {
                return resultSet.next(); // returns true if a row exists, meaning the password is correct
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if there is an exception
        }
    }

    public boolean setPassword(int serviceProviderID, String pass) {
        String query = "UPDATE serviceproviderauth SET password = ? WHERE userID = ?";

        try (Connection connection = dbHandler.connect();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, pass);
            pstmt.setInt(2, serviceProviderID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getBusNumber(int serviceID) throws ClassNotFoundException {
        String sql = "SELECT BusNo FROM busservice WHERE ServiceID = ?";
        String busNumber = "";
        try (Connection connection = dbHandler.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, serviceID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                busNumber = rs.getString("BusNo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return busNumber;
    }

    public String getTrainNumber(int serviceID) throws ClassNotFoundException {
        String sql = "SELECT TrainNumber FROM trainservice WHERE ServiceID = ?";
        String busNumber = "";
        try (Connection connection = dbHandler.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, serviceID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                busNumber = rs.getString("BusNo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return busNumber;
    }

    public String getFlightNumber(int serviceID) throws ClassNotFoundException {
        String sql = "SELECT FlightNumber FROM flightservice WHERE ServiceID = ?";
        String busNumber = "";
        try (Connection connection = dbHandler.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, serviceID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                busNumber = rs.getString("BusNo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return busNumber;
    }

}
