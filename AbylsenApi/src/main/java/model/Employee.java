package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import Dto.EmployeeDto;
import Dto.PersonDto;
import Logging.LoggerManager;


@Entity
@Table(name="EMPLOYEES",
		uniqueConstraints=@UniqueConstraint(columnNames={"EMAIL"}))
public class Employee extends Person {
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="POSTE")
	private String poste;
	
	@ManyToOne
	@JoinColumn(name="MASTER_ID")
	private Employee master;
	
	@Transient
	private List<Employee> children;
	
	public Employee() {
		children = new ArrayList<Employee>();
	}
	
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
	
	public String getPoste() {
		return poste;
	}
	
	public void setPoste(String poste) {
		this.poste = poste;
	}
	
	public Employee getMaster() {
		return master;
	}

	public void setMaster(Employee master) {
		this.master = master;
	}

	public List<Employee> getChildren() {
		return children;
	}

	public void setChildren(List<Employee> children) {
		this.children = children;
	}
	
	@Override
	public void fromDto(PersonDto e) {
		if(e instanceof EmployeeDto) {
			this.email = ((EmployeeDto)e).getEmail();
			this.password = ((EmployeeDto)e).getPassword();
			this.poste = ((EmployeeDto)e).getPoste();
		}
		
		super.fromDto(e);
	}
	
	@Override
	public Object toDto() {
		EmployeeDto dto = new EmployeeDto();
		
		dto.setId(getId());
		dto.setFirstName(getFirstName());
		dto.setLastName(getLastName());
		dto.setEmail(email);
		dto.setPassword(password);
		dto.setPoste(poste);
		
		return dto;
	}
	
	public static Employee getEmployee(Session session, int id) {
		try {
			return session.get(Employee.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Employee.getEmployee] Error while looking for employee : ", e);
			return null;
		}
	}
	
	public static Employee getEmployeeByEmail(Session session, String email) {
		try {
			@SuppressWarnings("deprecation")
			Criteria criteria = session.createCriteria(Employee.class);
			return (Employee) criteria.add(Restrictions.eq("email", email))
			                             .uniqueResult();
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Employee.getEmployee] Error while looking for employee : ", e);
			throw(e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static List<Employee> getAllEmployee(Session session) {
		try {
			List<Employee> list = new ArrayList<Employee>();
			list = session.createCriteria(Employee.class).list();
			return list;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Employee.getAllEmployee] Error while getting all employees : ", e);
			return new ArrayList<Employee>();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static List<Employee> getAllEmployeeByPoste(Session session, String poste) {
		try {
			Criteria criteria = session.createCriteria(Employee.class);
			return criteria.add(Restrictions.eq("poste", poste)).list();
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Employee.getAllEmployee] Error while getting all employees : ", e);
			return new ArrayList<Employee>();
		}
	}
	
	public static boolean addEmployee(Session session, Employee p) {
		try {
			session.save(p);
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonService.addPerson] Error while insert employee : ", e);
			return false;
		}
	}

}
