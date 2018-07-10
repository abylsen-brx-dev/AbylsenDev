package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LNG_LATS")
public class LngLat {
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;

	@Column(name="LNG")
	private float lng;

	@Column(name="LAT")
	private float lat;

	public LngLat() {
		
	}
	
	public LngLat(float lng, float lat) {
		this.lng = lng;
		this.lat = lat;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public float getLng() {
		return lng;
	}
	
	public void setLng(float lng) {
		this.lng = lng;
	}
	
	public float getLat() {
		return lat;
	}
	
	public void setLat(float lat) {
		this.lat = lat;
	}

}
