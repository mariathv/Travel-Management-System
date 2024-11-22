package application.controllers;

import java.io.IOException;

import application.Model.Customer;
import application.Model.ServiceProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ScreenController {
	private Stage stage;
	private Scene scene;

	private double xOffset = 0;
	private double yOffset = 0;

	private static Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void switchToRegisterScene(MouseEvent event) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());

		Parent root = FXMLLoader.load(getClass().getResource("/application/scenes/Register.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		double width = stage.getWidth();
		double height = stage.getHeight();
		Scene scene = new Scene(root, width, height);
		scene.setFill(Color.TRANSPARENT);

		stage.setScene(scene);
		stage.show();
	}

	public void switchToLoginScene(MouseEvent event) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());

		Parent root = FXMLLoader.load(getClass().getResource("/application/scenes/LoginRegister.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		double width = stage.getWidth();
		double height = stage.getHeight();
		scene = new Scene(root, width, height);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToSPRegister(MouseEvent event) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());

		Parent root = FXMLLoader.load(getClass().getResource("/application/scenes/RegisterServiceProvider.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		double width = stage.getWidth();
		double height = stage.getHeight();
		scene = new Scene(root, width, height);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToCusRegister(MouseEvent event) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/application/scenes/CustomerRegister.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			return; // Exit the method if the FXML fails to load
		}
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		System.out.println("Switching Scenes");
		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});
		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		double width = stage.getWidth();
		double height = stage.getHeight();
		scene = new Scene(root, width, height);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();

	}

	public void switchToSPHome(MouseEvent event, ServiceProvider serviceProvider) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/scenes/ServiceProviderHome.fxml"));

		Parent root = loader.load();

		ServiceProviderController controller = loader.getController();

		controller.setServiceProvider(serviceProvider);
		controller.loadHomePane();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToCusHome(MouseEvent event, Customer cus) throws IOException {
		System.out.println("Switching Scenes");

		setPrimaryStage(AppController.getPrimaryStage());

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/scenes/CustomerHome.fxml"));
		Parent root = loader.load();

		CustomerController controller = loader.getController();

		controller.setCustomer(cus);
		controller.loadHomePane_cus();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		root.setOnMousePressed(mouseEvent -> {
			xOffset = mouseEvent.getSceneX();
			yOffset = mouseEvent.getSceneY();
		});

		root.setOnMouseDragged(mouseEvent -> {
			primaryStage.setX(mouseEvent.getScreenX() - xOffset);
			primaryStage.setY(mouseEvent.getScreenY() - yOffset);
		});
		// get dimensions of the window
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}
}