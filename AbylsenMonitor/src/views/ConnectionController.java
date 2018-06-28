package views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ConnectionController extends AnchorPane {
	
	@FXML
	private VBox connectionBox;

	@FXML
	private ImageView background;
	
	public ConnectionController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ConnectionView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			//Load the view.
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
