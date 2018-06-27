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

		response.setHeader(HttpHeaders.HEADER_SECRET_KEY, "hidden");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		response.setHeader(HttpHeaders.HEADER_SECRET_KEY, "hidden");
		// We have nothing to do here
		// System.out.println("[SecurityInterceptor.afterCompletion] the execution is
		// done !!");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		RequestHandlerContract a = AnnotationUtil.getAnnontation(RequestHandlerContract.class, (HandlerMethod) handler);
		if (a != null) {
			if (a.needApiKey()) {

				if (request.getHeader(HttpHeaders.HEADER_APIKEY) == null) {
					response.sendError(HttpStatus.STATUS_FORBIDDEN, "apikey is not correct");
					return false;
				}

				if (request.getHeader(HttpHeaders.HEADER_APIKEY) == "") {
					response.sendError(HttpStatus.STATUS_FORBIDDEN, "apikey is not correct");
					return false;
				}

				Session session = HibernateUtil.getSessionFactory().openSession();

				ClientInformation ci = ClientInformation.getClientInformationByApyKey(session, request.getHeader(HttpHeaders.HEADER_APIKEY));
				if (ci == null) {
					response.sendError(HttpStatus.STATUS_FORBIDDEN, "apikey is not correct");
					session.close();
					return false;
				}	
				
				session.close();
				response.addHeader(HttpHeaders.HEADER_SECRET_KEY, ci.getSecretKey());
			}
		}

		return true;
	}
}
