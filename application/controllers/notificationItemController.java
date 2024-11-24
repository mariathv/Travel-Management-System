package application.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class notificationItemController {
    @FXML
    Text notif_date, notif_message, notif_agency, notif_tag;
    @FXML
    FontAwesomeIcon notif_icon;

    public void setData(String date, String message, String agency, String tag, String type) {
        this.notif_date.setText(date);
        this.notif_message.setText(message);
        this.notif_agency.setText(agency);
        this.notif_tag.setText(tag);

        if (type.equals("Hotel"))
            notif_icon.setGlyphName("HOTEL");
        else if (type.equals("Bus"))
            notif_icon.setGlyphName("BUS");
        else if (type.equals("Train"))
            notif_icon.setGlyphName("TRAIN");
        else if (type.equals("Flight"))
            notif_icon.setGlyphName("PLANE");

    }
}
