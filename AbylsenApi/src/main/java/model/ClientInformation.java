package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import Logging.LoggerManager;

@Entity
@Table(name="CLIENT_INFORMATIONS",
		uniqueConstraints=@UniqueConstraint(columnNames={"APIKEY", "NAME"}))
public class ClientInformation {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Integer id;
	
	@Column(name="APIKEY")
	private String apikey;

	@Column(name="SECRETKEY")
	private String secretKey;

	@Column(name="NAME")
	private String name;

	@Column(name="LEVEL")
	private int level;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getApikey() {
		return apikey;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
	public String getSecretKey() {
		return secretKey;
	}
	
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public static ClientInformation getClientInformation(Session session, int id) {
		try {
			return session.get(ClientInformation.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientInformation.getClientInformation] Error while looking for ClientInformation : ", e);
			return null;
		}
	}

	public static ClientInformation getClientInformationByApyKey(Session session, String apiKey) {
		try {
			@SuppressWarnings("deprecation")
			Criteria criteria = session.createCriteria(ClientInformation.class);
			return (ClientInformation) criteria.add(Restrictions.eq("apikey", apiKey))
			                             .uniqueResult();
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientInformation.getClientInformationByApyKey] Error while getting ClientInformation : ", e);
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static List<ClientInformation> getAllClientInformation(Session session) {
		try {
			List<ClientInformation> list = new ArrayList<ClientInformation>();
			
			list = session.createCriteria(ClientInformation.class).list();
			return list;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[ClientInformation.getAllClientInformation] Error while getting all ClientInformation : ", e);
			return new ArrayList<ClientInformation>();
		}
	}

	public static boolean addClientInformation(Session session, ClientInformation c) {
		try {
			session.save(c);
			
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[PersonService.addClientInformation] Error while insert ClientInformation : ", e);
			return false;
		}
	}
}
