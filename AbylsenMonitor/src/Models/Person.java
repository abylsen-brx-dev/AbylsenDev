package Models;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person extends BaseModel {

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final ObjectProperty<LocalDate> birthDate;
	
	public Person() {
		this(null, null, null);
	}
	
	public Person(String firstName, String lastName, LocalDate birthDate) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.birthDate = new SimpleObjectProperty<LocalDate>(birthDate);
	}
}
