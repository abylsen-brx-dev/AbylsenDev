package model;

import java.util.Date;

public class ConsultantToManager {
	private int id;
	private int managerId;
	private int consultantId;
	private Date from;
	private Date to;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getManagerId() {
		return managerId;
	}
	
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	public int getconsultantId() {
		return consultantId;
	}
	
	public void setConsultantId(int employeeId) {
		this.consultantId = employeeId;
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
