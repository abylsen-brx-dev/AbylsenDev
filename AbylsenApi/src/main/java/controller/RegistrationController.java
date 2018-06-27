package controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
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
import util.HibernateUtil;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	@RequestHandlerContract
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CreateAccountResponse create(@RequestBody CreateAccountRequest request){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = null;
		try {
			if(request == null || request.account == null) {
				CreateAccountResponse response = new CreateAccountResponse();
				response.code = HttpStatus.STATUS_BAD_REQUEST;
				response.status = "the request is empty";
				return response;
			}
			
			Employee e = new Employee();
			e.fromDto(request.account);
			
			t = session.beginTransaction();
			
			CreateAccountResponse response = RegistrationService.getInstance().createAccount(session, e);
			
			t.commit();
			session.close();
			
			return response;	
		}
		catch(Exception e) {
			System.out.println("[RegistrationController.create] ERROR : " + e.getMessage());
			CreateAccountResponse response = new CreateAccountResponse();
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Error, please retry later";
			
			if(t != null)
				t.commit();
			
			session.close();
			return response;
		}
	}
	

	@RequestHandlerContract
	@RequestMapping("/register")
	public RegistrationResponse register(@RequestBody RegistrationRequest request){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = null;
		try {
			if(request == null) {
				RegistrationResponse response = new  RegistrationResponse();
				response.code = HttpStatus.STATUS_BAD_REQUEST;
				response.status = "the request is empty";
			}
			
			t = session.beginTransaction();
			
			RegistrationResponse response= RegistrationService.getInstance().register(session, request.email, request.password);
			
			t.commit();
			session.close();
			
			return response;	
		}
		catch(Exception e) {
			System.out.println("[RegistrationController.register] ERROR : " + e.getMessage());
			RegistrationResponse response = new RegistrationResponse();
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Error, please retry later";
			
			if(t != null)
				t.commit();
			
			session.close();
			return response;
		}
	}
}
