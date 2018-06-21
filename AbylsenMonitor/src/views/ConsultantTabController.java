package views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class ConsultantTabController extends BorderPane {

	public ConsultantTabController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ConsultantTabView.fxml"));
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
