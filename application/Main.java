package application;
	
import application.controllers.AppController;

import application.controllers.ScreenController;
import application.controllers.authController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	private double xOffset = 0;
    private double yOffset = 0;
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			AppController.setPrimaryStage(primaryStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/LoginRegister.fxml"));
			Parent root = loader.load(); // Load the FXML and get the root node

			primaryStage.initStyle(StageStyle.TRANSPARENT);

//			authController authController = loader.getController();
//		    authController.setPrimaryStage(primaryStage); // Assuming you want to pass primaryStage
			
	        root.setOnMousePressed(event -> {
	            xOffset = event.getSceneX();
	            yOffset = event.getSceneY();
	        });

	        root.setOnMouseDragged(event -> {
	            primaryStage.setX(event.getScreenX() - xOffset);
	            primaryStage.setY(event.getScreenY() - yOffset);
	        });
	        
			Scene scene = new Scene(root,1100,600);
			scene.setFill(Color.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
