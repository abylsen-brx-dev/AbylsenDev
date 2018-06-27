package model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Dto.PersonDto;
import Util.HibernateUtil;

public class Person {
	private Integer id;
	private String firstName;
	private String lastName;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void fromDto(PersonDto p) {
		this.id = (Integer) p.getId();
		this.firstName = p.getFirstName();
		this.lastName = p.getLastName();
	}
	
	public static Person getPerson(int id) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			return session.get(Person.class, new Integer(id));
			
		} catch (Exception e) {
			System.out.println("[PersonService.addPerson] Error while insert person : " + e);
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Person> getAll() {
		try {
			List<Person> list = new ArrayList<Person>();
			Session session = HibernateUtil.getSessionFactory().openSession();

			list = session.createCriteria(Person.class).list();
			return list;
		} catch (Exception e) {
			System.out.println("[PersonService.addPerson] Error while insert person : " + e);
			return new ArrayList<Person>();
		}
	}

	public static boolean addPerson(Person p) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();

			Transaction tx = session.beginTransaction();
			session.save(p);
			
			tx.commit();
			session.close();

			return true;
		} catch (Exception ex) {
			System.out.println("[PersonService.addPerson] Error while insert person : " + ex);
			return false;
		}
	}
}
