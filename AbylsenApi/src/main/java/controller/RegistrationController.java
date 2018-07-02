package controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ArgumentResolver.HttpContext;
import Logging.LoggerManager;
import annotation.RequestHandlerContract;
import enums.HttpHeaders;
import enums.HttpStatus;
import model.BaseResponse;
import model.CreateAccountRequest;
import model.CreateAccountResponse;
import model.Employee;
import model.RegistrationRequest;
import model.RegistrationResponse;
import service.RegistrationService;
import util.HibernateUtil;
import util.SecurityUtil;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

	@RequestHandlerContract
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public BaseResponse create(@RequestBody CreateAccountRequest request, HttpServletResponse httpResponse, HttpContext cxt){
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
			
			BaseResponse response = RegistrationService.getInstance().createAccount(session, e);
			
			t.commit();
			session.close();
			
			httpResponse.addHeader(
					HttpHeaders.HEADER_TOKEN,
					SecurityUtil.generateToken(
							e.getEmail(), 
							e.getPassword(), 
							cxt.getClientInformation().getSecretKey()));
			return response;	
		}
		catch(Exception e) {
			LoggerManager.getInstance().logError("[RegistrationController.create] !!!!! ERROR !!!!!", e);
			CreateAccountResponse response = new CreateAccountResponse();
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Error, please retry later";
			
			if(t != null && t.isActive())
				t.commit();
			
			session.close();
			return response;
		}
	}
	

	@RequestHandlerContract
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public BaseResponse register(@RequestBody RegistrationRequest request, HttpServletResponse httpResponse, HttpContext cxt){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = null;
		try {
			if(request == null) {
				RegistrationResponse response = new  RegistrationResponse();
				response.code = HttpStatus.STATUS_BAD_REQUEST;
				response.status = "the request is empty";
			}
			
			t = session.beginTransaction();
			
			BaseResponse response= RegistrationService.getInstance().register(session, request.email, request.password);
			
			t.commit();
			session.close();

			httpResponse.addHeader(
					HttpHeaders.HEADER_TOKEN,
					SecurityUtil.generateToken(
							request.email, 
							request.password, 
							cxt.getClientInformation().getSecretKey()));
			
			return response;	
		}
		catch(Exception e) {
			LoggerManager.getInstance().logError("[RegistrationController.register] !!!!! ERROR !!!!!", e);
			RegistrationResponse response = new RegistrationResponse();
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Error, please retry later";
			
			if(t != null && t.isActive())
				t.commit();
			
			session.close();
			return response;
		}
	}
	
	@RequestHandlerContract(needRegistration = true)
	@RequestMapping(value = "/keepalive", method = RequestMethod.POST)
	public BaseResponse keepAlive(HttpServletResponse httpResponse, HttpContext cxt) {
		BaseResponse response = new BaseResponse();
		
		response.code = HttpStatus.STATUS_OK;
		response.status = "Done";
		
		try {
			httpResponse.addHeader(
					HttpHeaders.HEADER_TOKEN,
					SecurityUtil.generateToken(
							cxt.getUser().getEmail(), 
							cxt.getUser().getPassword(), 
							cxt.getClientInformation().getSecretKey()));
		} catch (UnsupportedEncodingException e) {
			LoggerManager.getInstance().logError("[RegistrationController.keepAlive] !!!!! ERROR !!!!!", e);
			response.code = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.status = "Error, try again later";
		}
		
		return response;
	}
}
