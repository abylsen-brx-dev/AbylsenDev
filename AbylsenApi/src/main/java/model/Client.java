package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;

import Dto.ClientDto;
import Logging.LoggerManager;

@Entity
@Table(name="CLIENT")
public class Client {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;
	
	@Column(name="NAME")
	private String name;

	@Column(name="ADDRESS")
	private String address;

	@ManyToOne
	@JoinColumn(name="POSITION_ID")
	private LngLat position;
	
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
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public LngLat getPosition() {
		return position;
	}

	public void setPosition(LngLat position) {
		this.position = position;
	}
	
	public void fromDto(ClientDto c) {
		if(c == null)
			return;
		
		this.id = c.id;
		this.name = c.name;
		this.address = c.address;
		this.position = new LngLat();
		this.position.setLng(c.lng);
		this.position.setLat(c.lat);
	}
	
	public Object toDto() {
		ClientDto dto = new ClientDto();
		
		dto.id = id;
		dto.address = address;
		dto.name = name;
		dto.lng = this.position.getLng();
		dto.lat = this.position.getLat(); 
		
		return dto;
	}

	public static Client getClient(Session session, int id) {
		try {
			return session.get(Client.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Client.getClient] Error while getting client : ", e);
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static List<Client> getAll(Session session) {
		try {
			List<Client> list = new ArrayList<Client>();
			list = session.createCriteria(Client.class).list();
			return list;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Client.getAll] error while getting all clients : ", e);
			return new ArrayList<Client>();
		}
	}

	public static boolean addClient(Session session, Client c) {
		try {
			session.save(c);
			return true;
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[Client.addClient] Error while inserting client : ", e);
			return false;
		}
	}
}
