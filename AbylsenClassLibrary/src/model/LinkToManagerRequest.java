package model;

import java.util.Date;

public class LinkToManagerRequest extends BaseRequest {
	public int managerId;
	public int consultantId;
	public Date From;
	public Date To;
}
