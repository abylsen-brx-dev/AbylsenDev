package com.example.abylsen;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import ArgumentResolver.HttpContextArgumentResolver;
import Logging.LoggerManager;
import initialization.InitializationEngine;
import interceptor.LogInterceptor;
import interceptor.RegistrationInterceptor;
import interceptor.SecurityInterceptor;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "controller", "service" })
public class AbylsenApiApplication extends WebMvcConfigurationSupport {
	
	public static void main(String[] args) {
		LoggerManager.getInstance().logDebug("********* SERVER STARTING ***************");
		//First we init the DB.
		InitializationEngine ie = new InitializationEngine();
		ie.start();
		
		SpringApplication.run(AbylsenApiApplication.class, args);

		LoggerManager.getInstance().logDebug("*********SERVER IS STARTED***************");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//Watch out the order of the interceptor registration. 
		registry.addInterceptor(new LogInterceptor());
		registry.addInterceptor(new SecurityInterceptor());
		registry.addInterceptor(new RegistrationInterceptor());
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new HttpContextArgumentResolver());
	}
}
