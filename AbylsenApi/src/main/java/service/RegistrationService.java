package service;

import org.hibernate.Session;

import enums.EmployeeEnums;
import enums.HttpStatus;
import model.BaseResponse;
import model.Employee;
import util.SecurityUtil;

public class RegistrationService extends BaseService{

	private static RegistrationService instance;
	
	public static RegistrationService getInstance() {
		if(instance == null)
			instance = new RegistrationService();
		
		return instance;
	}
	
	private RegistrationService() {
		
	}
	
	public BaseResponse createAccount(Session session, Employee e) {
		BaseResponse response = new BaseResponse();
		
		if(e.getEmail() == null || e.getEmail() == "" ) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Email is empty";
			return response;
		}
		
		if(Employee.getEmployeeByEmail(session, e.getEmail()) != null) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Email is already taken";
			return response;
		}
		
		if(e.getPassword() == null || e.getPassword() == "" ) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Password is empty";
			return response;
		}
		
		if(e.getFirstName() == null || e.getFirstName() == "" ) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Firstname is empty";
			return response;
		}
		
		if(e.getLastName() == null || e.getLastName() == "" ) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "LastName is empty";
			return response;
		}
		
		if(e.getPoste() == null || e.getPoste() == "" || !EmployeeEnums.isTypeExists(e.getPoste())) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "AccountType is empty or not valid";
			return response;
		}
		
		if(!Employee.addEmployee(session, e)) {
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Internal error, try later";
			return response;
		}
		
		response.statusCode = HttpStatus.STATUS_OK;
		response.message = "creation done";
		return response;
	}
	
	public BaseResponse register(Session session, String email, String password) {
		BaseResponse response = new BaseResponse();
		
		if(email == null || email == "") {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "email is empty";
		}
		
		if(password == null || password == "") {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "password is empty";
		}
		
		Employee e = Employee.getEmployeeByEmail(session, email);
		if(e == null) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "No account found with this email";
			return response;
		}
		
		if(!SecurityUtil.isPasswordValid(e.getPassword(), password)) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Password is not valid";
			return response;
		}

		response.statusCode = HttpStatus.STATUS_OK;
		response.message = e.getPoste();
		
		return response;
	}
}
