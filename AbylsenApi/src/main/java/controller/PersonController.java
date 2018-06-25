package controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Person;
import service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController {
	
	@RequestMapping("/all")
	public List<Person> getAll(){
		return PersonService.getInstance().getAll();
	}

	@RequestMapping("/{id}")
	public Person getPerson(@PathVariable("id") int id) {
		return PersonService.getInstance().getPerson(id);
	}
}
