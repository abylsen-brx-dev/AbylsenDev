package Dto;

public class EmployeeDto extends PersonDto {
	private String password;
	private String email;
	private String poste;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPoste() {
		return poste;
	}
	
	public void setPoste(String poste) {
		this.poste = poste;
	}
}
