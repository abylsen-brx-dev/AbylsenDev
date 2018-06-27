package com.example.abylsen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import Interceptor.LogInterceptor;
import Interceptor.RegistrationInterceptor;
import Interceptor.SecurityInterceptor;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "controller", "service" })
public class AbylsenApiApplication extends WebMvcConfigurationSupport {

	private RegistrationInterceptor registrationInterceptor = new RegistrationInterceptor();
	private SecurityInterceptor sercurityInterceptor = new SecurityInterceptor();
	private LogInterceptor logInterceptor = new LogInterceptor();
	
	public static void main(String[] args) {
		SpringApplication.run(AbylsenApiApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//Watch out the order of the interceptor registration. 
		registry.addInterceptor(logInterceptor);
		registry.addInterceptor(sercurityInterceptor);
		registry.addInterceptor(registrationInterceptor);
	}

}
