package ArgumentResolver;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;

import Logging.LoggerManager;
import enums.HttpHeaders;
import model.ClientInformation;
import model.Employee;
import util.HibernateUtil;
import util.SecurityUtil;
import util.TokenResponse;

public class HttpContextArgumentResolver extends HandlerMethodArgumentResolverComposite {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(HttpContext.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Session session = HibernateUtil.getSessionFactory().openSession();
	    HttpContext cxt = new HttpContext();
	    
	    try {
	    	if(request.getHeader(HttpHeaders.HEADER_APIKEY) != null) {
	    		cxt.setClientInformation(
	    				ClientInformation.getClientInformationByApyKey(session, request.getHeader(HttpHeaders.HEADER_APIKEY)));
		    }
		    
		    if(request.getHeader(HttpHeaders.HEADER_TOKEN) != null) {
		    	if(cxt.getClientInformation() != null) {
		    		TokenResponse tr = SecurityUtil
		    				.isTokenValid(session, request.getHeader(HttpHeaders.HEADER_TOKEN), cxt.getClientInformation().getSecretKey());
	    			
		    		cxt.setUser(
		    				Employee.getEmployeeByEmail(session, tr.email));
		    	}
		    }
	    }
	    catch(Exception e) {
	    	LoggerManager.getInstance().logError("[HttpContextArgumentResolver.resolveArgument] !!!!! ERROR !!!!!" , e);
	    }
	    
	    session.close();
	    return cxt;
	}
}
