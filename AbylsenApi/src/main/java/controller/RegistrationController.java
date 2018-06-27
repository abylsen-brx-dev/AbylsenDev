package controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import annotation.RequestHandlerContract;
import enums.HttpStatus;
import model.CreateAccountRequest;
import model.CreateAccountResponse;
import model.Employee;
import model.RegistrationRequest;
import model.RegistrationResponse;
import service.RegistrationService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	@RequestHandlerContract
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CreateAccountResponse create(@RequestBody CreateAccountRequest request){
		
		
		if(request == null || request.account == null) {
			CreateAccountResponse response = new CreateAccountResponse();
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "the request is empty";
			return response;
		}
		
		Employee e = new Employee();
		e.fromDto(request.account);
		
		return RegistrationService.getInstance().createAccount(e);
	}
	

	@RequestHandlerContract
	@RequestMapping("/register")
	public RegistrationResponse register(@RequestBody RegistrationRequest request){
		
		if(request == null) {
			RegistrationResponse response = new  RegistrationResponse();
			response.code = HttpStatus.STATUS_BAD_REQUEST;
			response.status = "the request is empty";
		}
		
		return RegistrationService.getInstance().register(request.email, request.password);
	}
}
