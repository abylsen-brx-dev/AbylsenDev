package service;

import java.util.Hashtable;

import org.springframework.stereotype.Service;

import model.Person;

@Service
public class PersonService {
	
	private Hashtable<String,Person> persons = new Hashtable<String,Person>();
	
	public PersonService() {
		Person p = new Person();
		p.setId("1");
		p.setFirstName("arnaud");
		p.setLastName("schaal");
		
		persons.put(p.getId(), p);
		
		Person p2 = new Person();
		p2.setId("2");
		p2.setFirstName("charline");
		p2.setLastName("liegeois");

		persons.put(p2.getId(), p2);
	}
	
	public Person getPerson(String id) {
		if(persons.containsKey(id))
			return persons.get(id);
		else
			return null;
	}
	
	public Hashtable<String,Person> getAll(){
		return persons;
	}
}
