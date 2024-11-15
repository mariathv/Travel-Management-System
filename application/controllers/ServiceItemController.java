package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ServiceItemController {
	@FXML
	private Text destLoc, depLoc, arvTime, depTime;
	
	public void setData(String destLoc, String depLoc, String arvTime, String depTime) {
		this.destLoc.setText(destLoc);
		this.depLoc.setText(depLoc);
		this.arvTime.setText(arvTime);
		this.depTime.setText(depTime);
	}
}
