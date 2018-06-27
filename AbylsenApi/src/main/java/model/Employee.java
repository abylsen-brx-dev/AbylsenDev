package model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import Dto.EmployeeDto;
import Dto.PersonDto;
import Util.HibernateUtil;

public class Employee extends Person {
	
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public void fromDto(PersonDto e) {
		if(e instanceof EmployeeDto) {
			this.email = ((EmployeeDto)e).getEmail();
			this.password = ((EmployeeDto)e).getPassword();
		}
		
		super.fromDto(e);
	}
	
	public static Employee getEmployee(int id) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			return session.get(Employee.class, new Integer(id));
			
		} catch (Exception e) {
			System.out.println("[Employee.getEmployee] Error while looking for employee : " + e);
			return null;
		}
	}
	
	public static Employee getEmployeeByEmail(String email) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			@SuppressWarnings("deprecation")
			Criteria criteria = session.createCriteria(Employee.class);
			return (Employee) criteria.add(Restrictions.eq("email", email))
			                             .uniqueResult();
			
		} catch (Exception e) {
			System.out.println("[Employee.getEmployee] Error while looking for employee : " + e);
			throw(e);
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Employee> getAllEmployee() {
		try {
			List<Employee> list = new ArrayList<Employee>();
			Session session = HibernateUtil.getSessionFactory().openSession();

			list = session.createCriteria(Person.class).list();
			return list;
		} catch (Exception e) {
			System.out.println("[Employee.getAllEmployee] Error while getting all employees : " + e);
			return new ArrayList<Employee>();
		}
	}

	public static boolean addEmployee(Employee p) {
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();

			Transaction tx = session.beginTransaction();
			session.save(p);
			
			tx.commit();
			session.close();

			return true;
		} catch (Exception ex) {
			System.out.println("[PersonService.addPerson] Error while insert employee : " + ex);
			return false;
		}
	}
}
