package controller;

import java.util.ArrayList;

import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ArgumentResolver.HttpContext;
import Dto.MissionDto;
import Logging.LoggerManager;
import annotation.RequestHandlerContract;
import enums.EmployeeEnums;
import enums.HttpStatus;
import model.*;
import util.HibernateUtil;

@RestController
@RequestMapping("/missions")
public class MissionController {

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseResponse addMission(@RequestBody MissionDto mission, HttpContext cxt) {
		BaseResponse response = new BaseResponse();
		if (mission == null) {
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			response.message = "Request is empty";
			return response;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Mission m = new Mission();
			m.fromDto(mission);

			if (!Mission.addMission(session, m)) {
				response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
				response.message = response.message = "Error, try again later";
				return response;
			}

			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";
			return response;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[MissionController.getMissionsByConsultant] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			return response;
		}
	}

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = EmployeeEnums.TYPE_MANAGER)
	@RequestMapping(value = "/getByConsultant", method = RequestMethod.POST)
	public GetMissionsResponse getMissionsByConsultant(@RequestBody GetMissionsByConsultantRequest request,
			HttpContext cxt) {
		GetMissionsResponse response = new GetMissionsResponse();

		if (request.employee == null) {
			response.message = "Request is empty";
			response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
			return response;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {

			Employee e = Employee.getEmployee(session, request.employee.id);
			if (e == null) {
				response.message = "Employee not found";
				response.statusCode = HttpStatus.STATUS_BAD_REQUEST;
				return response;
			}

			response.missions = new ArrayList<MissionDto>();
			for (Mission m : Mission.getAllByConsultant(session, e)) {
				response.missions.add((MissionDto) m.toDto());
			}

			response.statusCode = HttpStatus.STATUS_OK;
			response.message = "done";
			return response;

		} catch (Exception e) {
			LoggerManager.getInstance().logError("[MissionController.getMissionsByConsultant] !!!!! ERROR !!!!!", e);
			response.statusCode = HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
			response.message = "Error, try again later";
			return response;
		}
	}
}
