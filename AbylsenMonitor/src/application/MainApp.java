package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.ConnectionController;
import views.RootController;

public class MainApp extends Application {
	private Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			primaryStage.setTitle("Abyslen monitor");
			primaryStage.setScene(getConnectionScene());
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private Scene getMainScene() {
		RootController root = new RootController();
		
		Scene scene = new Scene(root);
		
		return scene;
	}
	
	private Scene getConnectionScene() {
		ConnectionController root = new ConnectionController();
		
		Scene scene = new Scene(root);
		
		return scene;
	}
}
