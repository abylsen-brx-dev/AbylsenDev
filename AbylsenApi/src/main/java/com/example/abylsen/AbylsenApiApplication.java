package com.example.abylsen;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import Interceptor.LogInterceptor;
import Interceptor.SecurityInterceptor;
import Util.HibernateUtil;
import model.Person;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "controller", "service" })
public class AbylsenApiApplication extends WebMvcConfigurationSupport {

	private SecurityInterceptor sercurityInterceptor = new SecurityInterceptor();
	private LogInterceptor logInterceptor = new LogInterceptor();
	
	public static void main(String[] args) {
		SpringApplication.run(AbylsenApiApplication.class, args);

		initHibernate();
	}

	private static void initHibernate() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = session.beginTransaction();
		Person p = new Person();
		// p.setId(1);
		p.setFirstName("arnaud");
		p.setLastName("schaal");

		Integer id = (Integer) session.save(p);
		System.out.println("Clé primaire : " + id);

		tx.commit();
		session.close();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//Watch out the order of the interceptor registration. 
		registry.addInterceptor(logInterceptor);
		registry.addInterceptor(sercurityInterceptor);
	}

}
