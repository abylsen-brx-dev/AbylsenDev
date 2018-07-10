package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
}
