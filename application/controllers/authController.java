package application.controllers;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

//adding sql executions here for now, will add it in separate classes later
public class authController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private TextField reg_username;
	@FXML
	private TextField email;
	@FXML
	private TextField reg_password;
	@FXML
	private TextField password_re;
	@FXML
	private TextField agencyName;
	
	ScreenController screenController = new ScreenController();
	private Stage primaryStage;
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
		screenController.switchToRegisterScene(event);
	}
	
	public void RegisterServiceProvider(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
		Connection connection = dbHandler.connect();
		
		String insertQuery0 = "INSERT INTO serviceProviderAuth(username, password) VALUES (?,?)";
		String insertQuery1 = "INSERT INTO ServiceProvider(serviceProviderID, email, travelAgencyName, rating) VALUES (?,?,?,?)";
		
		if (!reg_password.getText().equals(password_re.getText())) {
		    System.out.println("Incorrect Password: " + reg_password.getText() + " " + password_re.getText());
		}
		PreparedStatement prepStatement = connection.prepareStatement(insertQuery0, Statement.RETURN_GENERATED_KEYS);
		
		prepStatement.setString(1, reg_username.getText());
		prepStatement.setString(2, reg_password.getText());
		System.out.println("executing statement 1");
		int affectedRows = prepStatement.executeUpdate();
		int userID=-1;
		if (affectedRows > 0) {
		    ResultSet generatedKeys = prepStatement.getGeneratedKeys();
		    
		    if (generatedKeys.next()) {
		        userID = generatedKeys.getInt(1); 
		    }else {
		    	System.out.println("Key generation error");
		    	return;
		    }
		    
		   
		    prepStatement.close();
		} else {
		  
		    prepStatement.close();
		    connection.close();
		    return;
		}
		
		PreparedStatement prepStatement1 = connection.prepareStatement(insertQuery1);
		
		prepStatement1.setInt(1, userID);
		prepStatement1.setString(2, email.getText());
		prepStatement1.setString(3, agencyName.getText());
		prepStatement1.setInt(4, 0);
		System.out.println("executing statement 2");
		if (prepStatement1.executeUpdate() > 0) {
			System.out.println("successful operation :: adding new service provieder");
			prepStatement1.close();
		} else {
			System.out.println("failed operation :: adding new service provieder");
			prepStatement1.close();
		    connection.close();
		    return;
		}
		System.out.println("switching scenes");
		screenController.switchToSPHome(event);
		
		
	
	}
	
	
	
	
	
	//utils
	
	 public void setPrimaryStage(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	    }
	 public void exitApplication() {
	        Platform.exit(); 
	 }
	
	
	

}
