package application.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import application.Managers.BookingManager;
import application.Managers.ServiceManager;
import application.Model.HotelBooking;
import application.Model.ServiceProvider;
import application.Model.TravelBooking;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ServiceProviderController {
	@FXML
	private AnchorPane mainPanel;
	@FXML
	private Button nav_home, nav_notifs, nav_profile, nav_service, logoutBtn, confirmPhoneChange, confirmPassChange;
	@FXML
	private Text profileUsername, profileAgencyName, profileEmail, profileName, profilePhoneNum;
	@FXML
	private Text tServices, tBookings, onServices, ratingNum, errPN, errPass, totalEarnings;
	@FXML
	private HBox hboxRating;
	@FXML
	private VBox vBoxBookings;
	@FXML
	private Pane modify_basePane, modify_passwordPane, modify_phonePane;
	@FXML
	private TextField changePNField, currPass, newPass;
	@FXML
	private FontAwesomeIcon gobackManage1, gobackManage2, icon;

	private ServiceProvider serviceProvider;
	private ServiceManager serviceManager = new ServiceManager();
	private BookingManager bookingManager = new BookingManager();

	ScreenController screenController = new ScreenController();

	boolean flagFirst = true;
	int currentTab = 1;

	@FXML
	Text usernameProfilePane, agencyProfilePane;

	public void resetPassword() {
		modify_basePane.setVisible(false);
		modify_passwordPane.setVisible(true);
	}

	public void resetPhoneNumber() {
		modify_basePane.setVisible(false);
		modify_phonePane.setVisible(true);
	}

	public void confirmPasswordChange() {
		// will be implementing backend later
		modify_passwordPane.setVisible(false);
		modify_basePane.setVisible(true);
	}

	public void confirmPhoneChange() {
		// will be implementing backend later
		modify_phonePane.setVisible(false);
		modify_basePane.setVisible(true);
	}

	void loadRecentBookings() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("fetching recent bookings");
		if (!serviceProvider.getServiceType().equals("Hotel")) {

			List<TravelBooking> travelBookings = bookingManager
					.getRecentBookingsByServiceProvider(serviceProvider.getServiceProviderID());
			if (travelBookings.isEmpty()) {
				System.out.println("Epty");
			}
			for (TravelBooking booking : travelBookings) {
				System.out.println("loading " + booking.getBookingID());
				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("../scenes/ServiceProviderHomePane.fxml"));

				Parent root = fxmlloader.load();

				HBox hboxTemplate = (HBox) fxmlloader.getNamespace().get("sampleHBOX");
				Text username = (Text) fxmlloader.getNamespace().get("username");
				Text bookingDate = (Text) fxmlloader.getNamespace().get("bookingDate");
				hboxTemplate.getChildren().remove(2);

				username.setText(booking.getUsername());
				bookingDate.setText(booking.getBookingDate());
				Text Number = new Text(username.getText());
				Number.setFont(username.getFont());
				Number.setFill(username.getFill());
				Number.setStyle(username.getStyle());

				if (serviceProvider.getServiceType().equals("Bus")) {
					Number.setText(serviceManager.getBusNumber(booking.getServiceID()));
				} else if (serviceProvider.getServiceType().equals("Train")) {
					Number.setText(serviceManager.getTrainNumber(booking.getServiceID()));
				} else if (serviceProvider.getServiceType().equals("Flight")) {
					Number.setText(serviceManager.getFlightNumber(booking.getServiceID()));
				} else {
					return;
				}

				hboxTemplate.getChildren().add(Number);
				vBoxBookings.getChildren().add(hboxTemplate);
			}
		} else {
			List<HotelBooking> hotelBookings = bookingManager
					.getRecentHotelBookingsByServiceProvider(serviceProvider.getServiceProviderID());
			if (hotelBookings.isEmpty()) {
				System.out.println("Epty");
			}
			for (HotelBooking booking : hotelBookings) {
				System.out.println("loading " + booking.getBookingID());
				FXMLLoader fxmlloader = new FXMLLoader();
				fxmlloader.setLocation(getClass().getResource("../scenes/ServiceProviderHomePane.fxml"));

				Parent root = fxmlloader.load();

				HBox hboxTemplate = (HBox) fxmlloader.getNamespace().get("sampleHBOX");
				Text username = (Text) fxmlloader.getNamespace().get("username");
				Text bookingDate = (Text) fxmlloader.getNamespace().get("bookingDate");
				hboxTemplate.getChildren().remove(2);

				username.setText(booking.getUsername());
				bookingDate.setText(booking.getBookingDate());
				Text Number = new Text(username.getText());
				Number.setFont(username.getFont());
				Number.setFill(username.getFill());
				Number.setStyle(username.getStyle());

				Number.setText(booking.getRoomType());

				hboxTemplate.getChildren().add(Number);
				vBoxBookings.getChildren().add(hboxTemplate);
			}
		}
	}

	void loadProfileData() throws ClassNotFoundException, SQLException {
		flagFirst = false;
		profileUsername.setText(serviceProvider.getUsername());
		profileAgencyName.setText(serviceProvider.getAgencyName());
		profileEmail.setText(serviceProvider.getEmail());
		profileName.setText(serviceProvider.getUsername());
		profilePhoneNum.setText(serviceProvider.getPhoneNum());

		int totalServices = serviceManager.getTotalServices(serviceProvider.getServiceProviderID());
		int onGoingServices = serviceManager.getOnGoingServices(serviceProvider.getServiceProviderID());
		int totalBookings = serviceManager.getTotalBookings(serviceProvider.getServiceProviderID());

		tServices.setText("" + totalServices);
		tBookings.setText("" + totalBookings);
		onServices.setText("" + onGoingServices);

		int rating = serviceManager.getServiceProviderRating(serviceProvider.getServiceProviderID());
		int totalFeedbacks;
		if (serviceProvider.getServiceType().equals("Hotel")) {
			totalFeedbacks = serviceManager.getTotalHotelFeedbacks(serviceProvider.getServiceProviderID());
		} else {
			totalFeedbacks = serviceManager.getTotalTravelFeedbacks(serviceProvider.getServiceProviderID());
		}
		ratingNum.setText("" + totalFeedbacks);

		for (int i = 0; i < rating; i++) {
			FontAwesomeIcon fai = new FontAwesomeIcon();
			fai.setGlyphName("STAR");
			fai.setSize("2em");
			hboxRating.getChildren().add(fai);
		}

	}

	public void loadNewPanel() { // service pane
		flagFirst = false;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ServiceProviderServices.fxml"));
			AnchorPane newPanel = loader.load();
			ServiceController serviceC = loader.getController();
			serviceC.setServiceProvider(serviceProvider);
			serviceC.initServicesFS();
			mainPanel.getChildren().setAll(newPanel);
			changeBackButtonBG();
			nav_service.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");
			currentTab = 2;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
		// updateDashboard();
	}

	public void exitApplication() {
		Platform.exit();
	}

	public void loadHomePane() {
		flagFirst = false;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ServiceProviderHomePane.fxml"));
			loader.setController(this);

			AnchorPane newPanel = loader.load();
			mainPanel.getChildren().setAll(newPanel);
			changeBackButtonBG();
			nav_home.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");
			updateDashboard();
			loadRecentBookings();
			if (serviceProvider.getServiceType().equals("Hotel")) {
				totalEarnings.setText(
						"" + bookingManager.getHotelTotalEarnings(serviceProvider.getServiceProviderID()) + " PKR");
				icon.setGlyphName("HOTEL");
			} else {
				totalEarnings
						.setText("" + bookingManager.getTotalEarnings(serviceProvider.getServiceProviderID()) + " PKR");
			}
			logoutBtn.setOnMouseClicked(arg0 -> {
				try {
					logout(arg0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadProfilePane() {
		flagFirst = false;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ServiceProviderProfilePane.fxml"));
			loader.setController(this);
			AnchorPane newPanel = loader.load();
			mainPanel.getChildren().setAll(newPanel);
			loadProfileData();
			changeBackButtonBG();
			nav_profile.setStyle("-fx-background-color:  #212832;-fx-background-radius: 30px 0 0 30px;");

			changePNField.setStyle("-fx-padding: 0 0 0 10px;");
			currPass.setStyle("-fx-padding: 0 0 0 10px;");
			newPass.setStyle("-fx-padding: 0 0 0 10px;");

			modify_basePane.setVisible(true);
			modify_passwordPane.setVisible(false);
			modify_phonePane.setVisible(false);

			confirmPhoneChange.setOnMouseClicked(arg0 -> {
				try {
					PhoneNumberChange();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			});

			confirmPassChange.setOnMouseClicked(arg0 -> {
				try {
					PasswordChange();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			});

			gobackManage1.setOnMouseClicked(arg0 -> {
				modify_basePane.setVisible(true);
				modify_passwordPane.setVisible(false);
				modify_phonePane.setVisible(false);
			});

			gobackManage2.setOnMouseClicked(arg0 -> {
				modify_basePane.setVisible(true);
				modify_passwordPane.setVisible(false);
				modify_phonePane.setVisible(false);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		currentTab = 4;
	}

	private void updateDashboard() {
		// implementing logic to update dashboard with the new SP data
		System.out.println("> updating dashboard for " + serviceProvider.getUsername());
		usernameProfilePane.setText(serviceProvider.getUsername());
		agencyProfilePane.setText(serviceProvider.getAgencyName());

	}

	private void changeBackButtonBG() {
		switch (currentTab) {
			case 1:
				nav_home.setStyle("-fx-background-color:  #393D46;");
				nav_home.setStyle("-fx-background-radius: 15px 0 0 15px;");
				System.out.println("changed bg color");
				break;
			case 2:
				nav_service.setStyle("-fx-background-color:  #393D46;");
				nav_service.setStyle("-fx-background-radius: 15px 0 0 15px;");
				System.out.println("changed bg color");
				break;
			case 3:
				nav_notifs.setStyle("-fx-background-color:  #393D46;");
				nav_notifs.setStyle("-fx-background-radius: 15px 0 0 15px;");
				System.out.println("changed bg color");
				break;
			case 4:
				nav_profile.setStyle("-fx-background-color:  #393D46;");
				nav_profile.setStyle("-fx-background-radius: 15px 0 0 15px;");
				System.out.println("changed bg color");
				break;
		}
	}

	private void logout(MouseEvent event) throws IOException {
		System.out.println("logging out");
		serviceProvider = null;
		screenController.switchToLoginScene(event, true);

	}

	private void PhoneNumberChange() throws ClassNotFoundException, SQLException {
		errPN.setFill(Color.RED);
		if (changePNField.getText().equals("")) {
			errPN.setText("* Phone Number required");
			return;
		}
		if (!isValidPhoneNumber(changePNField.getText())) {
			errPN.setText("* Please enter a valid phone number");
			return;
		}
		if (!serviceManager.setPhoneNumber(serviceProvider.getServiceProviderID(), changePNField.getText())) {
			errPN.setText("Error changing phone number");
			return;
		} else {
			errPN.setFill(Color.BLACK);
			errPN.setText("Phone Number changed successfully");
			serviceProvider.setPhoneNum(changePNField.getText());

			loadProfileData();
		}

	}

	private void PasswordChange() throws ClassNotFoundException, SQLException {
		errPass.setFill(Color.RED);
		if (currPass.getText().equals("") || newPass.getText().equals("")) {
			errPass.setText("* Above fields are required");
			return;
		}
		if (!serviceManager.isCurrentPassword(currPass.getText(), serviceProvider.getServiceProviderID())) {
			errPass.setText("* Current password incorrect");
			return;
		}
		if (!serviceManager.setPassword(serviceProvider.getServiceProviderID(), newPass.getText())) {
			errPass.setText("Error changing password");
			return;
		} else {
			errPass.setFill(Color.BLACK);
			errPass.setText("Password changed successfully");
			loadProfileData();
		}

	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		System.out.println("checking : " + phoneNumber);
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			return false;
		}
		return phoneNumber.matches("\\d+"); // Regex to check if it contains only digits
	}

}
