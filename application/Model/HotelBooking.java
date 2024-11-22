package application.Model;

import java.time.LocalDate;

public class HotelBooking {

    private int bookingID;
    private int customerID;
    private int listingID;
    private int price;
    private String roomType;
    private String bookingDate;
    private String username;
    private int status; // Added status field

    public HotelBooking(int bookingID, int customerID, int listingID, int price, String roomType,
                        String bookingDate, String username) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.listingID = listingID;
        this.price = price;
        this.roomType = roomType;
        this.bookingDate = bookingDate;
        this.username = username;
        this.status = 1;
    }

    public HotelBooking() {
        this.bookingDate = LocalDate.now().toString(); // Store today's date
    }

    // Getters and Setters

    public int getBookingID() {
        return bookingID;
    }

    public String getRoomType() {
        return roomType;
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

    public int getListingID() {
        return listingID;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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
