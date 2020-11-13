package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class HostsModel {
	private final int employee_id;
	private final int event_id
	
	public HostsModel(int employee_id, int event_id) {
		this.employee_id = employee_id;
		this.event_id = event_id
	}

	public String getEmployeeId() {
		return employee_id;
	}

	public String getEventId() {
		return event_id;
	}
