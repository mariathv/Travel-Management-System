package application.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class feedbackItemController {
    @FXML
    private Text username, message;
    @FXML
    private HBox ratingHBOX;

    public void setData(String username, String message, int rating) {
        this.username.setText(username);
        this.message.setText(message);

        for (int i = 0; i < rating; i++) {
            FontAwesomeIcon fai = new FontAwesomeIcon();
            fai.setGlyphName("STAR");
            fai.setSize("1.25em");

            ratingHBOX.getChildren().addAll(fai);
        }
    }
}
