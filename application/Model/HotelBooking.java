package application.Model;

public class HotelBooking {

    private int bookingID;
    private int customerID;
    private int listingID;
    private int price;
    private String roomType;
    private String bookingDate;
    private String username;

    public HotelBooking(int bookingID, int customerID, int listingID, int price, String roomType,
            String bookingDate, String username) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.listingID = listingID;
        this.price = price;
        this.roomType = roomType;
        this.bookingDate = bookingDate;
        this.username = username;
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
        return listingID;
    }

    public void setServiceID(int serviceID) {
        this.listingID = serviceID;
    }

    public int getTotalPrice() {
        return price;
    }

    public void setTotalPrice(int totalPrice) {
        this.price = totalPrice;
    }

    public String getServiceType() {
        return roomType;
    }

    public void setServiceType(String serviceType) {
        this.roomType = serviceType;
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
}
