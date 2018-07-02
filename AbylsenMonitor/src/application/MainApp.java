package application;

import contexte.MainApplicationContexte;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.ConnectionController;
import views.RootController;

public class MainApp extends Application {
	
	private Parent current;
	private Parent previous;
	private Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			MainApplicationContexte.getInstance().setMainApp(this);
			
			this.stage = primaryStage;
			stage.setTitle("Abyslen monitor");
			stage.setScene(new Scene(new ConnectionController()));
			stage.setMaximized(true);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void navigateTo(Parent p) {
		if(p == null)
			return;
		
		previous = current;
		current = p;
		
		stage.setScene(new Scene(current));
	}
	
	public void goBack() {
		if(previous == null)
			return;
		
		current = previous;
		previous = null;
		
		stage.setScene(new Scene(current));
	}
}
