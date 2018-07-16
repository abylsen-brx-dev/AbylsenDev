package application;

import contexte.MainApplicationContexte;
import controls.Toast;
import interfaces.IView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.ConnectionController;

public class MainApp extends Application {
	
	private IView current;
	private IView previous;
	private Stage stage;
	private Scene oldScene;
	private VBox toastContainer;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			MainApplicationContexte.getInstance().setMainApp(this);
			
			stage = primaryStage;
			stage.setTitle("Abyslen monitor");
			navigateTo(new ConnectionController());
			stage.setMaximized(true);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void navigateTo(IView p) {
		if(p == null)
			return;
		
		previous = current;
		current = p;
		
		this.toastContainer = p.getToastContainer();
		
		oldScene = stage.getScene();
		if(oldScene == null)
			stage.setScene(new Scene((Parent)current));
		else
			stage.setScene(new Scene((Parent)current, oldScene.getWidth(), oldScene.getHeight()));
	}
	
	public void goBack() {
		if(previous == null)
			return;
		
		current = previous;
		previous = null;
		
		this.toastContainer = current.getToastContainer();
		
		oldScene = stage.getScene();
		stage.setScene(new Scene((Parent)current, oldScene.getWidth(), oldScene.getHeight()));
	}
	
	public void displayToast(String msg, double duration) {
		new Toast(toastContainer).show(msg, duration);
	}
}
