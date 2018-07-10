package controls;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.EmployeeModel;

public class MinimalistAccountViewer extends HBox {
	@FXML
	private Circle circle;
	
	@FXML
	private Label userEmail;
	
	@FXML
	private Label userPoste;
	
	public MinimalistAccountViewer(EmployeeModel user) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/MinimalistAccountView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			// Load the view.
			fxmlLoader.load();

			userEmail.textProperty().bind(user.emailProperty());
			userPoste.textProperty().bind(user.posteProperty());
			circle.setFill(new ImagePattern(new Image("http://institutogoldenprana.com.br/wp-content/uploads/2015/08/no-avatar-25359d55aa3c93ab3466622fd2ce712d1.jpg")));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
