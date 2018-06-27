package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import annotation.RequestHandlerContract;
import enums.HttpHeaders;
import enums.HttpStatus;
import model.ClientInformation;
import util.AnnotationUtil;
import util.HibernateUtil;

public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		// We have nothing to do here
		// System.out.println("[SecurityInterceptor.afterCompletion] the execution was
		// done !!");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// We have nothing to do here
		// System.out.println("[SecurityInterceptor.afterCompletion] the execution is
		// done !!");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		RequestHandlerContract a = AnnotationUtil.getAnnontation(RequestHandlerContract.class, (HandlerMethod) handler);
		if (a != null) {
			if(a.needApiKey()) {
				if(!isApiKeyValid(request.getHeader(HttpHeaders.HEADER_APIKEY), request.getHeader(HttpHeaders.HEADER_CLIENT_ID))){
					response.sendError(HttpStatus.STATUS_FORBIDDEN, "apikey is not correct");	
					return false;
				}
			}
		}
		
		return true;
	}

	public boolean isApiKeyValid(String apiKey, String clientId) {
		if (apiKey == null)
			return false;

		if (apiKey == "")
			return false;

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		if(ClientInformation.getClientInformationByApyKey(session, apiKey) == null)
			return false;
		
		return true;
	}
}
