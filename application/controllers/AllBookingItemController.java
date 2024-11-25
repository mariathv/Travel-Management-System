package application.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class AllBookingItemController {

    @FXML
    private Text serialno, Bookingid, bookingDate, price;

    @FXML
    private FontAwesomeIcon icon;

    @FXML
    private Circle circle;

    public void setData(
            int x,
            String bookingId,
            String bookingDate,
            String priceText,
            String glyphName,
            int color) {

        if (icon != null) {
            icon.setGlyphName(glyphName);
        }

        if (circle != null) {
            if (color == 1)
                circle.setFill(Color.web("Green"));
            else if (color == 0)
                circle.setFill(Color.web("Red"));
            
        }

        if (serialno != null) {
            serialno.setText(String.valueOf(x));
        }
        if (this.bookingDate != null) {
            this.bookingDate.setText(bookingDate);
        }
        if (price != null) {
            price.setText(priceText);
        }
    }
}
