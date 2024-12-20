package application.Model;

import java.time.LocalDate;

public class TravelBooking {
    private int bookingID;
    private int customerID;
    private int serviceID;
    private int totalPrice;
    private String serviceType;
    private String bookingDate;
    private String username;
    private int status; // Added status field

    // Constructor with today's date as default for bookingDate
    public TravelBooking(int customerID, int serviceID, int totalPrice, String serviceType, String username) {
        this.customerID = customerID;
        this.serviceID = serviceID;
        this.totalPrice = totalPrice;
        this.serviceType = serviceType;
        this.bookingDate = LocalDate.now().toString(); // Store today's date
        this.username = username;
        status = 1;
    }

    public TravelBooking(int bookingID, int customerID, int serviceID, int totalPrice, String serviceType,
                         String bookingDate, String username) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.serviceID = serviceID;
        this.totalPrice = totalPrice;
        this.serviceType = serviceType;
        this.bookingDate = bookingDate;
        this.username = username;
        status = 1;
    }

    // Getters and Setters

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
