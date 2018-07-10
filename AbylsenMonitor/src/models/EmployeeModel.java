package models;

import Dto.EmployeeDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeModel extends PersonModel {
	private StringProperty password;
	private StringProperty email;
	private StringProperty poste;
	
	public EmployeeModel() {
		this(0, null, null, null, null, null);
	}
	public EmployeeModel(int id, String firstName, String lastName, String password, String email, String poste) {
		super(id, firstName, lastName);
		
		this.password = new SimpleStringProperty(password);	
		this.email = new SimpleStringProperty(email);	
		this.poste = new SimpleStringProperty(poste);
	}
	public String getPassword() {
		return password.get();
	}
	
	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);;
	}

	public StringProperty emailProperty() {
		return email;
	}
	
	public String getPoste() {
		return poste.get();
	}

	public void setPoste(String poste) {
		this.poste.set(poste);;
	}

	public StringProperty posteProperty() {
		return poste;
	}
	
	@Override
	public void fromDto(Object dto) {
		if(dto instanceof EmployeeDto) {
			EmployeeDto e = (EmployeeDto)dto;
			
			password.set(e.getPassword());
			email.set(e.getEmail());
			poste.set(e.getPoste());
		}
		
		super.fromDto(dto);
	}
}
