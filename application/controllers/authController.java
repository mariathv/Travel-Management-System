package application.controllers;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class authController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	
	ScreenController screenController = new ScreenController();
	public void login_sp() throws SQLException, ClassNotFoundException {
		Connection connection = dbHandler.connect();
		
		//login implementation
		String getPassQuery = "SELECT password FROM serviceProviderAuth WHERE username = ?";
		PreparedStatement prepStatement = connection.prepareStatement(getPassQuery);
		prepStatement.setString(1, username.getText());
		
		System.out.println("auth controlling username " + username);
		
		ResultSet resultSet = prepStatement.executeQuery();
		if (resultSet.next()) { 
		    String retrievedPassword = resultSet.getString("password");
		    if (retrievedPassword.equals(password.getText())) {
		        System.out.println("Login successful.");
		    } else {
		        System.out.println("Incorrect password.");
		    }
		} else {
		    System.out.println("Username not found.");
		}

	}
	@FXML
	public void createNewAccount(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		System.out.println("pass");
		screenController.switchToRegisterScene(event);
	}
	
	
	

}