package service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import Util.HibernateUtil;
import model.Person;

@Service
public class PersonService {

	private static PersonService instance;

	public static PersonService getInstance() {
		if (instance == null)
			instance = new PersonService();

		return instance;
	}

	private PersonService() {

	}

	public Person getPerson(int id) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			return session.get(Person.class, new Integer(id));
			
		} catch (Exception e) {
			System.out.println("[PersonService.addPerson] Error while insert person : " + e);
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Person> getAll() {
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

	public boolean addPerson(Person p) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();

			Transaction tx = session.beginTransaction();

			Integer id = (Integer) session.save(p);
			System.out.println("Clé primaire : " + id);

			tx.commit();
			session.close();

			return true;
		} catch (Exception ex) {
			System.out.println("[PersonService.addPerson] Error while insert person : " + ex);
			return false;
		}
	}
}
