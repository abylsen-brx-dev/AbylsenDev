package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import annotation.RequestHandlerContract;
import enums.HttpStatus;
import model.BaseResponse;

@RestController
@RequestMapping("/server")
public class ServerStatusController {

	@RequestMapping("status")
	@RequestHandlerContract(needApiKey = false)
	public BaseResponse getStatus() {
		BaseResponse response = new BaseResponse();

		response.statusCode = HttpStatus.STATUS_OK;
		response.message = "Server is ok !";

		return response;
	}

	private final RequestMappingHandlerMapping handlerMapping;

	@Autowired
	public ServerStatusController(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	@RequestMapping(value = "/endpoints", method = RequestMethod.GET)
	@RequestHandlerContract(needApiKey = false)
	String getEndPointsInView() {
	    String result = "";
	    result += "<table style=\"width:100%\"> \n" + 
	    		"<tr>";
	    for (RequestMappingInfo element : handlerMapping.getHandlerMethods().keySet()) {

	    	result += "<tr>\r\n" + 
	    			"    <th>" + element.getPatternsCondition() + "</th>\r\n" + 
	    			"    <th>" + element.getMethodsCondition() + "</th> \r\n" + 
	    			"    <th>" + element.getParamsCondition() + "</th> \r\n" + 
	    			"    <th>" + element.getConsumesCondition() + "</th>\r\n" + 
	    			"  </tr> \n";
	    }
	    
	    result += "</tr> \n" + 
	    		"</table>";
	    return result;
	}
}
