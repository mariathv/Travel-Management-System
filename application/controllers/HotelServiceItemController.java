package application.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class HotelServiceItemController {
	@FXML
	private Text HotelName,HotelLoc,BRPrice,DRPrice;
	@FXML
	private HBox starBox;
	@FXML
	private ImageView serviceImage;
	
	public void setData(String hotelName, String hotelLoc, int BRPPrice, int DRPPrice, int rating) {
		HotelName.setText(hotelName);
		HotelLoc.setText(hotelLoc);
		BRPrice.setText(String.valueOf(BRPPrice));
		DRPrice.setText(String.valueOf(DRPPrice));
		
		for(int i=0; i<rating; i++) {
			FontAwesomeIcon icon = new FontAwesomeIcon();
			icon.setGlyphName("STAR");
			icon.setSize("2em");
			starBox.getChildren().add(icon);
			
		}
	}
}
