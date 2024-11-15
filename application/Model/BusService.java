package application.Model;

public class BusService extends TravelService {
	String stationName;
	String stationLocation;
	String BusNumber;
	
	public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
		
	public String getStationLocation() {
	    return stationLocation;
	}
	
	public void setStationLocation(String stationLocation) {
	    this.stationLocation = stationLocation;
	}
	
	public String getBusNumber() {
	    return BusNumber;
	}
	
	public void setBusNumber(String busNumber) {
	    this.BusNumber = busNumber;
	}
}
