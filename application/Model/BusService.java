package application.Model;

public class BusService extends TravelService {
	String stationName;
	String stationLocation;
	String BusNumber;

	public BusService(
			String stationName,
			String stationLocation,
			String busNumber,
			int serviceID,
			int serviceProviderID,
			String description,
			String serviceType,
			String departureTime,
			String arrivalTime,
			String departureLocation,
			String arrivalLocation,
			String departureDate,
			String arrivalDate) {
		this.stationName = stationName;
		this.stationLocation = stationLocation;
		this.BusNumber = busNumber;
		this.serviceID = serviceID;
		this.serviceProviderID = serviceProviderID;
		this.description = description;
		this.serviceType = serviceType;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.departureLocation = departureLocation;
		this.arrivalLocation = arrivalLocation;
		this.departureDate = departureDate;
		this.arrivalDate = arrivalDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

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
