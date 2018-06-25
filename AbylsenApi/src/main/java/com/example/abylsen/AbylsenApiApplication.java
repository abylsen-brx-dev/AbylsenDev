package com.example.abylsen;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import Util.HibernateUtil;
import model.Person;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"controller", "service"})
public class AbylsenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbylsenApiApplication.class, args);
	
		initHibernate();
	}
	
	private static void initHibernate() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		Person p = new Person();
		//p.setId(1);
		p.setFirstName("arnaud");
		p.setLastName("schaal");
		
		Integer id = (Integer) session.save(p);
		System.out.println("Clé primaire : " + id);
		
		tx.commit();
		session.close();
	}
}
