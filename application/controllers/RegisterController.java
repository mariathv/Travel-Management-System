package application.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class RegisterController {
	ScreenController screenController = new ScreenController();
	@FXML
	public void goBackLogin(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToLoginScene(event);
	}
	
	public void RegisterAsServiceProvider(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToSPRegister(event);
	}
	
	
	//utils
	
	public void exitApplication() {
        Platform.exit(); 
 }
}
