package initialization;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import enums.EmployeeEnums;
import interfaces.IInitializer;
import model.Employee;
import model.Person;

public class PersonInitializer implements IInitializer {

	@Override
	public void startInit(Session session) {
		
		createPersonIfNotExists(session, "Person1", "Dev");
		createPersonIfNotExists(session, "Person2", "Dev");

		createEmployeeIfNotExists(session, "Employee1", "Dev", "employee1.dev@dev.com", "devPassword", EmployeeEnums.TYPE_MANAGER);
		createEmployeeIfNotExists(session, "Employee2", "Dev", "employee2.dev@dev.com", "devPassword", EmployeeEnums.TYPE_MANAGER);
		createEmployeeIfNotExists(session, "Employee3", "Dev", "employee3.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee4", "Dev", "employee4.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee5", "Dev", "employee5.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee6", "Dev", "employee6.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee7", "Dev", "employee7.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee8", "Dev", "employee8.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee9", "Dev", "employee9.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee10", "Dev", "employee10.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
		createEmployeeIfNotExists(session, "Employee11", "Dev", "employee11.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT);
	}

	public static Person createPersonIfNotExists(Session session, String firstName, String lastName) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Person.class);
		
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		
		Person p = (Person)criteria.uniqueResult();
		if(p != null)
			return p;
	
		p = new Person();
		
		p.setFirstName(firstName);
		p.setLastName(lastName);

		session.save(p);
		
		return p;
	}
	
	public static Employee createEmployeeIfNotExists(Session session, String firstName, String lastName, String email, String password, String poste) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Employee.class);
		
		criteria.add(Restrictions.eq("firstName", firstName));
		criteria.add(Restrictions.eq("lastName", lastName));
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("password", password));
		criteria.add(Restrictions.eq("poste", poste));
		
		
		Employee e = (Employee)criteria.uniqueResult();
		if(e != null)
			return e;
		
		e = new Employee();

		e.setFirstName(firstName);
		e.setLastName(lastName);
		e.setEmail(email);
		e.setPassword(password);
		e.setPoste(poste);

		session.save(e);
		
		return e;
	}
}
