package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import annotation.RequestHandlerContract;
import enums.EmployeeEnums;
import enums.HttpHeaders;
import enums.HttpStatus;
import model.ClientInformation;
import util.AnnotationUtil;
import util.HibernateUtil;
import util.SecurityUtil;
import util.TokenResponse;

public class RegistrationInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		RequestHandlerContract a = AnnotationUtil.getAnnontation(RequestHandlerContract.class, (HandlerMethod) handler);
		if (a != null) {
			if (a.needRegistration()) {
				ClientInformation ci = ClientInformation.getClientInformationByApyKey(session, request.getHeader(HttpHeaders.HEADER_APIKEY));
				if(ci == null) {
					response.sendError(HttpStatus.STATUS_UNAUTHORIZED, "the api key is not referenced");
					return false;
				}
				
				TokenResponse tr = SecurityUtil.isTokenValid(session, request.getHeader(HttpHeaders.HEADER_TOKEN), ci.getSecretKey());
				
				if(!tr.isValid) {
					response.sendError(HttpStatus.STATUS_UNAUTHORIZED, "token is not correct");
					return false;
				}
				
				if(!(a.needRights() == null || a.needRights().equals(""))) {
					if(!EmployeeEnums.haveRights(a.needRights(), tr.rule)) {
						response.sendError(HttpStatus.STATUS_UNAUTHORIZED, "The current user does not have the good rigths");
						return false;
					}
				}
			}
		}
		return true;
	}
}
