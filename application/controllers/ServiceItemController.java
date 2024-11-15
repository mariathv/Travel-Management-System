package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ServiceItemController {
	@FXML
	private Text destLoc, depLoc, arvTime, depTime;
	@FXML
	private ImageView serviceImage;
	
	public void setData(String destLoc, String depLoc, String arvTime, String depTime, String serviceType) {
		this.destLoc.setText(destLoc);
		this.depLoc.setText(depLoc);
		this.arvTime.setText(arvTime);
		this.depTime.setText(depTime);
		
		if(serviceType.equals("Bus")) {
			Image busImage = new Image(getClass().getResourceAsStream("../assets/images/pngs/bus.png"));
	        serviceImage.setImage(busImage);
		}else if(serviceType.equals("Train")) {
			Image busImage = new Image(getClass().getResourceAsStream("../assets/images/pngs/train.png"));
	        serviceImage.setImage(busImage);
		}else if(serviceType.equals("Flight")) {
			Image busImage = new Image(getClass().getResourceAsStream("../assets/images/pngs/flight.png"));
	        serviceImage.setImage(busImage);
		}else {
			Image busImage = new Image(getClass().getResourceAsStream("../assets/images/pngs/hotel.png"));
	        serviceImage.setImage(busImage);
		}
	}
}
