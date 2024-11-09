package application.controllers;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ScreenController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToRegisterScene(MouseEvent event) throws IOException {
    	System.out.println("Switching Scenes");	
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/application/scenes/Register.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	//get dimensions of the window
    	double width = stage.getWidth();
        double height = stage.getHeight();
    	scene = new Scene(root, width, height);
    	stage.setScene(scene);
    	stage.show();
    }
    public void switchToLoginScene(MouseEvent event) throws IOException {
    	System.out.println("Switching Scenes");	
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/application/scenes/LoginRegister.fxml"));
    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	//get dimensions of the window
    	double width = stage.getWidth();
        double height = stage.getHeight();
    	scene = new Scene(root, width, height);
    	stage.setScene(scene);
    	stage.show();
    }
}