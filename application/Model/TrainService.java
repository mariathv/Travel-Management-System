package application.Model;

public class TrainService extends TravelService {
	String stationName;
	String stationLocation;
	String TrainNumber;

	public TrainService(
			String stationName,
			String stationLocation,
			String TrainNumber,
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
		this.TrainNumber = TrainNumber;
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

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public String getStationLocation() {
		return stationLocation;
	}

	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
	}

	public String getTrainNumber() {
		return TrainNumber;
	}

	public void setTrainNumber(String busNumber) {
		this.TrainNumber = busNumber;
	}
}
