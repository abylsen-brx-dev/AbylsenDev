package models;

import Dto.PersonDto;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonModel extends BaseModel {
	private IntegerProperty id;
	private StringProperty firstName;
	private StringProperty lastName;
	
	public PersonModel() {
		this(0, null, null);
	}
	
	public PersonModel(int id, String firstName, String lastName) {
		super();
		
		this.id = new SimpleIntegerProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
	}
	
	public int getId() {
		return id.get();
	}
	
	public String getFirstName() {
		return firstName.get();
	}
	
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
	}
	
	@Override
	public void fromDto(Object dto) {
		if(dto instanceof PersonDto) {
			PersonDto p = (PersonDto)dto;
			
			firstName.set(p.getFirstName());
			lastName.set(p.getLastName());
			id.set(p.getId());
		}
		
		super.fromDto(dto);
	}
}
