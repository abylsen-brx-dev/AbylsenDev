package controller;

import java.util.Hashtable;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Person;
import service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController {
	
	private PersonService ps = new PersonService();
	
	@RequestMapping("/all")
	public Hashtable<String,Person> getAll(){
		return ps.getAll();
	}

	@RequestMapping("/{id}")
	public Person getPerson(@PathVariable("id") String id) {
		return ps.getPerson(id);
	}
}
