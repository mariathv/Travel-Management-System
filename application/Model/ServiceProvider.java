package application.Model;

public class ServiceProvider implements User {
	String email, phoneNum, name, username, agencyName; //implemented ones
	String serviceType; 
	int ServiceProviderID, rating;
	
	public ServiceProvider(int ServiceProviderID, String email,  String username, String agencyName){
		this.email=email;
		this.ServiceProviderID = ServiceProviderID;
		this.username = username;
		this.agencyName=agencyName;
		//defaults
		rating = 0;
		email = "-";
		phoneNum = "-";
		
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

}
