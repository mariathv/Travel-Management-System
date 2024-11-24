package application.Model;

public class FlightService extends TravelService {
	String AirportName;
	String AirportLocation;
	String FlightNumber;
	String GateNumber;

	public FlightService(
			String AirportName,
			String AirportLocation,
			String FlightNumber,
			int serviceID,
			int serviceProviderID,
			String description,
			String serviceType,
			String departureTime,
			String arrivalTime,
			String departureLocation,
			String arrivalLocation,
			String departureDate,
			String arrivalDate,
			String GateNumber) {
		this.AirportName = AirportName;
		this.AirportLocation = AirportLocation;
		this.FlightNumber = FlightNumber;
		this.GateNumber = GateNumber;
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

	public String getAirportName() {
		return AirportName;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setAirportName(String stationName) {
		this.AirportName = stationName;
	}

	public String getAirportLocation() {
		return AirportLocation;
	}

	public void setAirportLocation(String stationLocation) {
		this.AirportLocation = stationLocation;
	}

	public String getFlightNumber() {
		return FlightNumber;
	}

	public void setFlightNumber(String busNumber) {
		this.FlightNumber = busNumber;
	}

	public String getGateNumber() {
		return GateNumber;
	}

	public void setGateNumber(String busNumber) {
		this.GateNumber = busNumber;
	}
}
