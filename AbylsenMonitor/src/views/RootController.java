package views;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class RootController extends BorderPane {
	@FXML
	private TabPane tabPane;

	public RootController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/RootView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			//Load the view.
			fxmlLoader.load();
			
			//Init tabs.
			for(Tab t : tabPane.getTabs()) {
				initTab(t);
			}
			
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	private void initTab(Tab t) {
		if(t == null)
			return;
		
		switch(t.getText().toLowerCase()) {
		case "dashboard":
			t.setContent(new DashboardTabController());
			break;
		case "consultant":
			t.setContent(new ConsultantTabController());
			break;
		}
	}
}