package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ArgumentResolver.HttpContext;
import Dto.ClientDto;
import Dto.EmployeeDto;
import Logging.LoggerManager;
import annotation.RequestHandlerContract;
import enums.EmployeeEnums;
import enums.HttpStatus;
import model.BaseResponse;
import model.Client;
import model.GetAllClientResponse;
import model.LngLat;
import model.Mission;
import util.HibernateUtil;

@RestController
@RequestMapping("/client")
public class ClientController {

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public GetAllClientResponse getAll(HttpContext cxt) {
		GetAllClientResponse response = new GetAllClientResponse();

		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			response.clients = new ArrayList<ClientDto>();
			LocalDate localDate = LocalDate.now();
			
			ClientDto dto = null;
			for (Client c : Client.getAll(session)) {
				dto = (ClientDto) c.toDto();
				dto.linkedConsultants = new ArrayList<EmployeeDto>();
				
				for(Mission m : Mission.getAllByClient(session, c)) {
					if(m.getConsultant() != null)
						if(m.getTo().after(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))
							dto.linkedConsultants.add((EmployeeDto) m.getConsultant().toDto());
				}
				
				response.clients.add(dto);
			}
			
			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";

			return response;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientController.getAllClients] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			return response;
		}
	}
	
	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseResponse addClient(@RequestBody ClientDto request, HttpContext cxt) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = null;
		BaseResponse response = new BaseResponse();
		
		if(request == null) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Request is empty";
			return response;
		}
		
		try {
			
			Client c = new Client();
			c.fromDto(request);
			
			t = session.beginTransaction();
			
			session.save(c.getPosition());
			session.save(c);

			t.commit();
			session.close();
			
			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";

			return response;
			
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientController.addClient] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			
			if(t != null && t.isActive())
				t.commit();
			
			return response;
		}
	}
	
	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public BaseResponse updateClient(@RequestBody ClientDto request, HttpContext cxt) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = null;
		BaseResponse response = new BaseResponse();
		
		if(request == null) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Request is empty";
			return response;
		}
		
		try {
			t = session.beginTransaction();
			
			Client c = new Client();
			c.fromDto(request);

			LngLat position = LngLat.get(session, c.getId());
			if(position == null) {
				position = new LngLat();
				session.save(position);
			}
			
			position.setLng(request.lng);
			position.setLat(request.lat);
			
			c.setPosition(position);
			
			session.update(position);
			session.update(c);

			t.commit();
			session.close();
			
			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";

			return response;
			
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientController.updateClient] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			
			if(t != null && t.isActive())
				t.commit();
			
			return response;
		}
	}

}
