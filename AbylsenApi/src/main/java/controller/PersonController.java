package controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import annotation.RequestHandlerContract;
import model.Person;

@RestController
@RequestMapping("/persons")
public class PersonController {

	@RequestHandlerContract(needRegistration = true)
	@RequestMapping("/all")
	public List<Person> getAll(){
		return Person.getAll();
	}

	@RequestHandlerContract(needRegistration = true)
	@RequestMapping("/{id}")
	public Person getPerson(@PathVariable("id") int id) {
		return Person.getPerson(id);
	}

	@RequestHandlerContract(needRegistration = true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public boolean update(@RequestBody Person p) {
	    if(p == null)
	    	return false;
	    
	    return Person.addPerson(p);
	}
}
