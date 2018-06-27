package model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ClientInformation {
	private Integer id;
	private String apikey;
	private String secretKey;
	private String name;
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
			System.out.println("[ClientInformation.getClientInformation] Error while looking for ClientInformation : " + e);
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
			System.out.println("[ClientInformation.getClientInformationByApyKey] Error while getting ClientInformation : " + e);
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
			System.out.println("[ClientInformation.getAllClientInformation] Error while getting all ClientInformation : " + e);
			return new ArrayList<ClientInformation>();
		}
	}

	public static boolean addClientInformation(Session session, ClientInformation c) {
		try {
			session.save(c);
			
			return true;
		} catch (Exception ex) {
			System.out.println("[PersonService.addClientInformation] Error while insert ClientInformation : " + ex);
			return false;
		}
	}
}
