package service;

import java.util.Hashtable;

import org.springframework.stereotype.Service;

import model.Person;

@Service
public class PersonService {
	
	private Hashtable<Integer,Person> persons = new Hashtable<Integer,Person>();
	
	public PersonService() {
		Person p = new Person();
		//p.setId(1);
		p.setFirstName("arnaud");
		p.setLastName("schaal");
		
		persons.put(1, p);
		
		Person p2 = new Person();
		//p2.setId(2);
		p2.setFirstName("charline");
		p2.setLastName("liegeois");

		persons.put(2, p2);
	}
	
	public Person getPerson(int id) {
		if(persons.containsKey(id))
			return persons.get(id);
		else
			return null;
	}
	
	public Hashtable<Integer,Person> getAll(){
		return persons;
	}
}
