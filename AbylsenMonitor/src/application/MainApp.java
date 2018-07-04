package application;

import contexte.MainApplicationContexte;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.ConnectionController;

public class MainApp extends Application {
	
	private Parent current;
	private Parent previous;
	private Stage stage;
	private Scene oldScene;
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
		
		oldScene = stage.getScene();
		stage.setScene(new Scene(current, oldScene.getWidth(), oldScene.getHeight()));
	}
	
	public void goBack() {
		if(previous == null)
			return;
		
		current = previous;
		previous = null;
		
		oldScene = stage.getScene();
		stage.setScene(new Scene(current, oldScene.getWidth(), oldScene.getHeight()));
	}
}
