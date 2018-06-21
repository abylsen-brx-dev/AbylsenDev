package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.RootController;

public class MainApp extends Application {

	private RootController root;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			root = new RootController();
			
			Scene scene = new Scene(root, 1000, 800);
			
			primaryStage.setTitle("Abyslen monitor");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
