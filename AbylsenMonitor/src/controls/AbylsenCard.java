package controls;

import java.io.IOException;

import Dto.EmployeeDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AbylsenCard extends AnchorPane{
	
	@FXML
    private ImageView employeeImage;

    @FXML
    private Label employeeName;

    @FXML
    private Label employeePoste;

    @FXML
    private Label employeeEmail;

    @FXML
    private Label employeePhoneNumber;

    private StringProperty name; 
    private StringProperty poste; 
    private StringProperty email; 
    private StringProperty phoneNumber; 
    
	public AbylsenCard(EmployeeDto e) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/AbylsenCardView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
			
			if(e != null) {
				employeeImage.setImage(new Image("https://www.webengineering.fr/webengineering/var/uploads/societes/289/logo/Logo_signature.png"));
				name = new SimpleStringProperty(e.firstName + " " + e.lastName);
				poste = new SimpleStringProperty(e.poste);
				email = new SimpleStringProperty(e.email);
				phoneNumber = new SimpleStringProperty(e.phoneNumber);

				employeeName.textProperty().bind(name);
				employeePoste.textProperty().bind(poste);
				employeeEmail.textProperty().bind(email);
				employeePhoneNumber.textProperty().bind(phoneNumber);
			}
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
