package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;

import Logging.LoggerManager;

@Entity
@Table(name="LNG_LATS")
public class LngLat {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;

	@Column(name="LNG")
	private double lng;

	@Column(name="LAT")
	private double lat;

	public LngLat() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}

	public static LngLat get(Session session, int id) {
		try {
			return session.get(LngLat.class, new Integer(id));
		} catch (Exception e) {
			LoggerManager.getInstance().logError("[LngLat.get] Error while getting client : ", e);
			return null;
		}
	}
}
