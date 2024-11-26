package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class bookingItemController {

    @FXML
    private HBox sampleHBOX;

    @FXML
    private Text userID;
    @FXML
    private Text username;
    @FXML
    private Text bookingDate;
    @FXML
    private Text price;
    @FXML
    private Circle circle;

    public HBox getSampleHBOX() {
        return sampleHBOX;
    }

    public void setUserIDText(String text) {
        userID.setText(text);
    }

    public void setUsernameText(String text) {
        username.setText(text);
    }

    public void setBookingDateText(String text) {
        bookingDate.setText(text);
    }

    public void setPriceText(String text) {
        price.setText(text);
    }

    @FXML
    public void initialize() {
        userID.setWrappingWidth(80);
        username.setWrappingWidth(80);
        bookingDate.setWrappingWidth(80);
        price.setWrappingWidth(80);
    }

    public void setData(int id, String name, String date, String cost, int color) {
        userID.setText(String.valueOf("   " + id));
        userID.setWrappingWidth(80);
        username.setText(name);
        username.setWrappingWidth(80);
        bookingDate.setText(date);
        bookingDate.setWrappingWidth(90);
        price.setText(cost);
        price.setWrappingWidth(80);
        if (circle != null) {
            if (color == 1)
                circle.setFill(Color.web("Green"));
            else if (color == 0)
                circle.setFill(Color.web("Red"));
            
        }

    }

}
