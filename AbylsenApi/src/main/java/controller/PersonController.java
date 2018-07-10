package controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ArgumentResolver.HttpContext;
import Dto.EmployeeDto;
import Logging.LoggerManager;
import annotation.RequestHandlerContract;
import enums.EmployeeEnums;
import enums.HttpStatus;
import model.Employee;
import model.GetAllConsultantsResponse;
import model.GetInfoResponse;
import util.HibernateUtil;

@RestController
@RequestMapping("/person")
public class PersonController {

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = "")
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public GetInfoResponse getInfo(HttpContext cxt) {
		GetInfoResponse response = new GetInfoResponse();

		try {
			EmployeeDto e = (EmployeeDto) cxt.getUser().toDto();
			
			response.account = e;
			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";

			return response;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonController.GetInfoResponse] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			return response;
		}
	}

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/getAllConsultant", method = RequestMethod.GET)
	public GetAllConsultantsResponse getAllConsultant(HttpContext cxt) {
		GetAllConsultantsResponse response = new GetAllConsultantsResponse();
		response.consultants = new ArrayList<EmployeeDto>();
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();

			List<Employee> result = Employee.getAllEmployeeByPoste(session, EmployeeEnums.TYPE_CONSULTANT);
			
			for (Employee e : result)
				response.consultants.add((EmployeeDto) e.toDto());
			
			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";
			return response;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonController.GetAllConsultantsResponse] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			return response;
		}
	}

//	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
//	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
//	public BaseResponse linkToManager(LinkToManagerRequest request, HttpContext cxt) {
//
//	}
}
