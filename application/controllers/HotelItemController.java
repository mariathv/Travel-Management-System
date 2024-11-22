package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HotelItemController {

	@FXML
	private Text serialNo,HotelName, City, HotelRating;
	
	public void setData(int x,String hotelName, String city, double rating) {
		serialNo.setText(String.valueOf(x));
		HotelName.setText(hotelName); // Hotel Name
		City.setText(city); // Hotel Location
		if (rating >= 0) {
	        HotelRating.setText(String.format("%.1f ★", rating)); // Format to one decimal place with a star
	    } else {
	        HotelRating.setText("Not Rated"); // Default text for invalid ratings
	    }
	}
	
}