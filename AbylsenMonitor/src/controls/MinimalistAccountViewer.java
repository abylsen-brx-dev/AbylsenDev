package controls;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class RoundImageView extends HBox {

	private StringProperty image;
	
	@FXML
	private Circle circle;
	
	public RoundImageView(String image) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/RoundImageView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();
			
			this.image = new SimpleStringProperty(image);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public StringProperty imageProperty() {
		return image;
	}

	public String getImage() {
		return image.get();
	}
	
	public void setImage(String image) {
		this.image.set(image); 
		
		ImagePattern pattern = new ImagePattern(new Image(image));
		circle.setFill(pattern);
	}
}
