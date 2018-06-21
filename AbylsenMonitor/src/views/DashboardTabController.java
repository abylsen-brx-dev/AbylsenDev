package views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class DashboardTabController extends BorderPane {

	public DashboardTabController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/DashboardTabView.fxml"));
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
