package contexte;

import Dto.EmployeeDto;
import application.MainApp;
import enums.HttpHeaders;
import models.EmployeeModel;
import okhttp3.Headers;

public class MainApplicationContexte {
	
	private static MainApplicationContexte instance;
	
	public static MainApplicationContexte getInstance() {
		if(instance == null)
			instance = new MainApplicationContexte();
		
		return instance;
	}
	
	private MainApplicationContexte() {
		//TODO : Here reload from files !!!
		apiKey = "devApiKey123456";
		token = "";
		abylsenApiUrl = "http://localhost:8080/";
	}
	
	private String token;
	
	private String apiKey;

	private MainApp mainApp;
	
	private EmployeeModel user;
	
	private String abylsenApiUrl;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public MainApp getMainApp() {
		return mainApp;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public EmployeeModel getUser() {
		return user;
	}
	
	public void setUser(EmployeeModel user) {
		this.user = user;
	}
	
	public void setUser(EmployeeDto dto) {
		if(user == null)
			user = new EmployeeModel();
		
		user.fromDto(dto);;
	}
	
	public String getAbylsenApiUrl() {
		return abylsenApiUrl;
	}
	
	public void manageHeaders(Headers headers) {
		if(headers == null)
			return;
		
		if(headers.get(HttpHeaders.HEADER_TOKEN) == null || headers.get(HttpHeaders.HEADER_TOKEN) == "")
			return;
		
		token = headers.get(HttpHeaders.HEADER_TOKEN);
	}

}
