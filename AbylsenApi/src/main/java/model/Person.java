package model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import Dto.PersonDto;
import Logging.LoggerManager;

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
	
	public static Person getPerson(Session session, int id) {
		try {
			return session.get(Person.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonService.addPerson] Error while insert person : ", e);
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Person> getAll(Session session) {
		try {
			List<Person> list = new ArrayList<Person>();
			list = session.createCriteria(Person.class).list();
			return list;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonService.addPerson] Error while insert person : ", e);
			return new ArrayList<Person>();
		}
	}

	public static boolean addPerson(Session session, Person p) {
		try {
			session.save(p);
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonService.addPerson] Error while insert person : ", e);
			return false;
		}
	}
}
