package Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.web.method.HandlerMethod;

public class AnnotationUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T getAnnontation(Class<? extends Annotation> returnType, HandlerMethod handler) throws Exception {
		HandlerMethod hm = (HandlerMethod) handler;
		Method method = hm.getMethod();
		
		if(method.isAnnotationPresent((Class<? extends Annotation>) returnType)) {
			return (T) method.getAnnotation((Class<? extends Annotation>) returnType);
		}
			
		return null;
	}
}
