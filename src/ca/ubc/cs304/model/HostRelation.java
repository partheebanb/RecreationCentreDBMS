package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class HostRelation {
	private final int employeeId;
	private final int eventId;

	public HostRelation(int employeeId, int eventId) {
		this.employeeId = employeeId;
		this.eventId = eventId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public int getEventId() {
		return eventId;
	}
}
