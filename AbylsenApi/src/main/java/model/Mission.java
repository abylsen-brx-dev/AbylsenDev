package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import Dto.ClientDto;
import Dto.EmployeeDto;
import Dto.MissionDto;
import Logging.LoggerManager;

@Entity
@Table(name="MISSIONS")
public class Mission {
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;

	@Column(name="NAME")
	private String name;

	@Column(name="DESCRIPTION")
	private String description;

	@OneToOne
	@JoinColumn(name="CONSULTANT_ID")
	private Employee consultant;

	@OneToOne
	@JoinColumn(name="CLIENT_ID")
	private Client client;

	@Column(name="FROM_DATE")
	@Temporal(TemporalType.DATE)
	private Date from;

	@Column(name="TO_DATE")
	@Temporal(TemporalType.DATE)
	private Date to;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Employee getConsultant() {
		return consultant;
	}

	public void setConsultant(Employee consultant) {
		this.consultant = consultant;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}


	public Date getFrom() {
		return from;
	}

	
	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
	
	public void fromDto(MissionDto m) {
		if(m == null)
			return;
		
		this.id = m.id;
		this.name = m.name;
		this.description = m.description;
		this.from = m.from;
		this.to = m.to;
		
		Employee e = new Employee();
		e.fromDto(m.consultant);
		this.consultant = e;
		
		Client c = new Client();
		c.fromDto(m.client);
		this.client = c;
	}
	
	public Object toDto() {
		MissionDto dto = new MissionDto();
		
		dto.id = getId();
		dto.name = name;
		dto.description = description;
		dto.consultant = (EmployeeDto) consultant.toDto();
		dto.client = (ClientDto) client.toDto();
		dto.to = to;
		dto.from = from;
		
		return dto;
	}

	public static Mission getMission(Session session, int id) {
		try {
			return session.get(Mission.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Mission.getMission] Error while getting mission : ", e);
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Mission> getAll(Session session) {
		try {
			List<Mission> list = new ArrayList<Mission>();
			list = session.createCriteria(Mission.class).list();
			return list;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Mission.getAll] Error while getting all missions : ", e);
			return new ArrayList<Mission>();
		}
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Mission> getAllByClient(Session session, Client c) {
		try {
			return session.createCriteria(Mission.class)
					.add(Restrictions.eq("client", c))
					.list();
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Mission.getAll] Error while getting all missions : ", e);
			return new ArrayList<Mission>();
		}
	}

	public static boolean addMission(Session session, Mission m) {
		try {
			session.save(m);
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Mission.addMission] Error while inserting mission : ", e);
			return false;
		}
	}
}
