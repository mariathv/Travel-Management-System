package application.Model;

public class Customer implements User {
    private int customerID;  // Maps to CustomerID in the database
    private String name;     // Maps to name in the database
    private String email;    // Maps to email in the database
    private String phoneNo;  // Maps to phoneNo in the database
    private String username; // New attribute for username

    // Default constructor
    public Customer() {}

    // Parameterized constructor
    public Customer(int customerID, String name, String email, String phoneNo, String username) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.username = username;
    }

    // Getters and setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNum() {
        return phoneNo;
    }

    @Override
    public void setPhoneNum(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Print details for debugging
    public void printDetails() {
        System.out.println("Customer Details:");
        System.out.println("Customer ID: " + customerID);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNo);
        System.out.println("Username: " + username);
    }
}
