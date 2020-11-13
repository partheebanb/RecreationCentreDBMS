package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class ProgramModel {
	private final int eventId;
	private final String name;
	private final Date startDate;
	private final Date endDate;

	public ProgramModel(int eventId, String name, Date startDate, Date endDate) {
		this.eventId = eventId;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
    
