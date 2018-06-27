package ArgumentResolver;

import model.ClientInformation;
import model.Employee;

public class HttpContext {
	
	private ClientInformation clientInformation;
	private Employee user;
	
	public ClientInformation getClientInformation() {
		return clientInformation;
	}
	
	public void setClientInformation(ClientInformation clientInformation) {
		this.clientInformation = clientInformation;
	}
	
	public Employee getUser() {
		return user;
	}
	
	public void setUser(Employee user) {
		this.user = user;
	}
}
