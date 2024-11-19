package application.Model;

public class ServiceProvider implements User {
	String email, phoneNum, name, username, agencyName; //implemented ones
	String serviceType; 
	int ServiceProviderID, rating;
	
	public ServiceProvider(int ServiceProviderID, String email,  String username, String agencyName, String serviceType){
		this.email=email;
		this.ServiceProviderID = ServiceProviderID;
		this.username = username;
		this.agencyName=agencyName;
		//defaults
		rating = 0;
		email = "-";
		phoneNum = "-";
		this.serviceType=serviceType;
		
	}
	
	public String getName() {
		return name;
	}
    public void setName(String name) {
    	this.name = name;
    }

    public String getEmail() {
    	return email;
    }
    public void setEmail(String email) {
    	this.email = email;
    }

    public String getPhoneNum() {
    	return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
    	this.phoneNum = phoneNum;
    }
    
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceProviderID() {
        return ServiceProviderID;
    }

    public void setServiceProviderID(int serviceProviderID) {
        this.ServiceProviderID = serviceProviderID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getUsername() {
    	return username;
    }
    public String getAgencyName() {
    	return agencyName;
    }
    
    public void printDetails() {
        System.out.println("Service Provider Details:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNum);
        System.out.println("Username: " + username);
        System.out.println("Agency Name: " + agencyName);
        System.out.println("Service Type: " + serviceType);
        System.out.println("Service Provider ID: " + ServiceProviderID);
        System.out.println("Rating: " + rating);
    }

}
