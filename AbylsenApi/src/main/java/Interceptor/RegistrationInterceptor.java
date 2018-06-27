package Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import Util.AnnotationUtil;
import Util.SecurityUtil;
import annotation.RequestHandlerContract;
import enums.HttpHeaders;
import enums.HttpStatus;

public class RegistrationInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		// We have nothing to do here
		// System.out.println("[SecurityInterceptor.afterCompletion] the execution was done !!");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// We have nothing to do here
		// System.out.println("[SecurityInterceptor.afterCompletion] the execution is done !!");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = true;

		RequestHandlerContract a = AnnotationUtil.getAnnontation(RequestHandlerContract.class, (HandlerMethod) handler);
		if (a != null) {
			if (a.needRegistration()) {
				result = SecurityUtil.isTokenValid(request.getHeader(HttpHeaders.HEADER_TOKEN));

				if (!result)
					response.sendError(HttpStatus.STATUS_UNAUTHORIZED, "token is not correct");
			}
		}

		return result;
	}
}
