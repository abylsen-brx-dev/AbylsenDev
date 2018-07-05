package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ArgumentResolver.HttpContext;
import Dto.EmployeeDto;
import Logging.LoggerManager;
import annotation.RequestHandlerContract;
import enums.HttpStatus;
import model.GetInfoResponse;

@RestController
@RequestMapping("/person")
public class PersonController {

	@RequestHandlerContract(needApiKey = true, needRegistration = true, needRights = "")
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public GetInfoResponse getInfo(HttpContext cxt) {
		GetInfoResponse response = new GetInfoResponse();

		try {
			EmployeeDto e = new EmployeeDto();
			
			e.setId(cxt.getUser().getId());
			e.setEmail(cxt.getUser().getEmail());
			e.setPassword(cxt.getUser().getPassword());
			e.setFirstName(cxt.getUser().getFirstName());
			e.setLastName(cxt.getUser().getLastName());
			e.setPoste(cxt.getUser().getPoste());
			
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
}
