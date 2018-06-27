package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import annotation.RequestHandlerContract;
import enums.HttpStatus;
import model.BaseResponse;

@RestController
@RequestMapping("/status")
public class ServerStatusController {

	@RequestMapping("/")
	@RequestHandlerContract(needApiKey = false)
	public BaseResponse  getStatus(){
		BaseResponse response = new BaseResponse();
		
		response.code = HttpStatus.STATUS_OK;
		response.status = "Server is ok !";
		
		return response;
	}
}
