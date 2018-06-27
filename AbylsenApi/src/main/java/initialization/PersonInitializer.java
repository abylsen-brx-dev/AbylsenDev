package initialization;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import interfaces.IInitializer;
import model.Employee;
import model.Person;

public class PersonInitializer implements IInitializer {

	@Override
	public void startInit(Session session) {
		
		createPersonIfNotExists(session, "Person1", "Dev");
		createPersonIfNotExists(session, "Person2", "Dev");

		createEmployeeIfNotExists(session, "Employee1", "Dev", "employee1.dev@dev.com", "devPassword");
		createEmployeeIfNotExists(session, "Employee2", "Dev", "employee2.dev@dev.com", "devPassword");
	}

	private Person createPersonIfNotExists(Session session, String firstName, String lastName) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Person.class);
		
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		
		Person p = new Person();

		p.setFirstName(firstName);
		p.setLastName(lastName);

		if(criteria.uniqueResult() == null)
			session.save(p);
		
		return p;
	}
	
	private Employee createEmployeeIfNotExists(Session session, String firstName, String lastName, String email, String password) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Employee.class);
		
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("password", password));
		
		
		Employee e = new Employee();

		e.setFirstName(firstName);
		e.setLastName(lastName);
		e.setEmail(email);
		e.setPassword(password);

		if(criteria.uniqueResult() == null)
			session.save(e);
		
		return e;
	}
}
