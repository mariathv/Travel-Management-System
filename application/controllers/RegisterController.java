package application.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class RegisterController {
	ScreenController screenController = new ScreenController();

	@FXML
	public void goBackLogin(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToLoginScene(event);
	}

	@FXML
	public void RegisterAsServiceProvider(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToSPRegister(event);
	}

	@FXML
	public void RegisterAsCustomer(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		screenController.switchToCusRegister(event);
	}

	// utils

	public void exitApplication() {
		Platform.exit();
	}
}
