package controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
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
import model.Client;
import model.GetAllClientResponse;
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
}
