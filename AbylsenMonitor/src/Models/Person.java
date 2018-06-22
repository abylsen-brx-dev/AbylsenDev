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
	
	public String getFirstName() {
		if(firstName == null)
			return "";
		
		return firstName.get();
	}
	
	public void setFirstName(String value) {
		if(firstName != null)
			firstName.set(value);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	public String getLastName() {
		if(lastName == null)
			return "";
		
		return lastName.get();
	}
	
	public void setLastName(String value) {
		if(lastName != null)
			lastName.set(value);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
	}
	
	public LocalDate getBirhtDate() {
		if(birthDate == null)
			return null;
		
		return birthDate.get();
	}
	
	public void setBirthDate(LocalDate value) {
		if(birthDate != null)
			birthDate.set(value);
	}
	
	public ObjectProperty<LocalDate> birthDateProperty() {
		return birthDate;
	}
}
