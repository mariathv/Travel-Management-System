package application.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import application.controllers.dbHandler;

public class ServiceDAO {

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
}
