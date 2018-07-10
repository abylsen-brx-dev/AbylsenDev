package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.Session;

import Dto.PersonDto;
import Logging.LoggerManager;

@Entity
@Table(name="PERSONS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Integer id;
	
	@Column(name="FIRSTNAME")
	private String firstName;
	
	@Column(name="LASTNAME")
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
		if(p == null)
			return;
		
		this.id = (Integer) p.id;
		this.firstName = p.firstName;
		this.lastName = p.lastName;
	}
	
	public Object toDto() {
		PersonDto dto = new PersonDto();
		
		dto.id = id;
		dto.firstName = firstName;
		dto.lastName = lastName;
		
		return dto;
	}
	
	public static Person getPerson(Session session, int id) {
		try {
			return session.get(Person.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Person.getPerson] Error while getting person : ", e);
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
			LoggerManager.getInstance().logError("[Person.getAll] Error while getting all persons : ", e);
			return new ArrayList<Person>();
		}
	}

	public static boolean addPerson(Session session, Person p) {
		try {
			session.save(p);
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Person.addPerson] Error while inserting person : ", e);
			return false;
		}
	}
}
