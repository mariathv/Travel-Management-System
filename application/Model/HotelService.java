package application.Model;

public class HotelService {
	public HotelService() {}
	
	 public HotelService(int serviceID, int serviceProviderID, String hotelName, String hotelLocation,
             int rating, int basicRoomPrice, int doubleRoomPrice, String City) {
		this.serviceID = serviceID;
		this.serviceProviderID = serviceProviderID;
		this.HotelName = hotelName;
		this.HotelLocation = hotelLocation;
		this.rating = rating;
		this.basicRoomPrice = basicRoomPrice;
		this.doubleRoomPrice = doubleRoomPrice;
		this.City = City;
	}
	int serviceID;
	int serviceProviderID;
	String HotelName;
	String HotelLocation;
	int rating;
	int basicRoomPrice;
	int doubleRoomPrice;
	String City;
	
	public int getServiceID() {
	    return serviceID;
	}

	public void setServiceID(int serviceID) {
	    this.serviceID = serviceID;
	}

	public int getServiceProviderID() {
	    return serviceProviderID;
	}

	public void setServiceProviderID(int serviceProviderID) {
	    this.serviceProviderID = serviceProviderID;
	}

	public String getHotelName() {
	    return HotelName;
	}

	public void setHotelName(String hotelName) {
	    this.HotelName = hotelName;
	}

	public String getHotelLocation() {
	    return HotelLocation;
	}

	public void setHotelLocation(String hotelLocation) {
	    this.HotelLocation = hotelLocation;
	}
	
	public String getCity() {
	    return City;
	}

	public void setCity(String City) {
	    this.City = City;
	}

	public int getRating() {
	    return rating;
	}

	public void setRating(int rating) {
	    this.rating = rating;
	}

	public int getBasicRoomPrice() {
	    return basicRoomPrice;
	}

	public void setBasicRoomPrice(int basicRoomPrice) {
	    this.basicRoomPrice = basicRoomPrice;
	}

	public int getDoubleRoomPrice() {
	    return doubleRoomPrice;
	}

	public void setDoubleRoomPrice(int doubleRoomPrice) {
	    this.doubleRoomPrice = doubleRoomPrice;
	}

}
