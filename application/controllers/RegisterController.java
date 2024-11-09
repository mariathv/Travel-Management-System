package application.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class RegisterController {
	ScreenController screenController = new ScreenController();
	@FXML
	public void goBackLogin(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		System.out.println("pass");
		screenController.switchToRegisterScene(event);
	}
}
