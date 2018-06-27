package service;

import Util.SecurityUtil;
import enums.HttpStatus;
import model.CreateAccountResponse;
import model.Employee;
import model.RegistrationResponse;

public class RegistrationService extends BaseService{

	private static RegistrationService instance;
	
	public static RegistrationService getInstance() {
		if(instance == null)
			instance = new RegistrationService();
		
		return instance;
	}
	
	private RegistrationService() {
		
	}
	
	public CreateAccountResponse createAccount(Employee e) {
		CreateAccountResponse response = new CreateAccountResponse();
		
		if(e.getEmail() == null || e.getEmail() == "" ) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "Email is empty";
			return response;
		}
		
		if(Employee.getEmployeeByEmail(e.getEmail()) != null) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "Email is already taken";
			return response;
		}
		
		if(e.getPassword() == null || e.getPassword() == "" ) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "Password is empty";
			return response;
		}
		
		if(e.getFirstName() == null || e.getFirstName() == "" ) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "Firstname is empty";
			return response;
		}
		
		if(e.getLastName() == null || e.getLastName() == "" ) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "LastName is empty";
			return response;
		}
		
		if(!Employee.addEmployee(e)) {
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Internal error, try later";
			return response;
		}
		
		response.code = HttpStatus.STATUS_OK;
		response.status = "creation done";
		response.token = SecurityUtil.generateToken(e.getEmail(), e.getPassword());
		return response;
	}
	
	public RegistrationResponse register(String email, String password) {
		RegistrationResponse response = new RegistrationResponse();
		
		if(email == null || email == "") {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "email is empty";
		}
		
		if(password == null || password == "") {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "password is empty";
		}
		
		Employee e = Employee.getEmployeeByEmail(email);
		if(e == null) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "No account found with this email";
			return response;
		}
		
		if(!SecurityUtil.isPasswordValid(e.getPassword(), password)) {
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "Password is not valid";
		}

		response.code = HttpStatus.STATUS_OK;
		response.status = "registration done";
		response.token = SecurityUtil.generateToken(e.getEmail(), e.getPassword());
		
		return response;
	}
}
