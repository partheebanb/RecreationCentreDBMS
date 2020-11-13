package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class ProgramModel {
    private final int eventId;
    private final String name;
    private final java.sql.Date startDate;
    private final java.sql.Date endDate;
	
	public ProgramModel(int eventId, String name, java.sql.Date startDate, java.sql.Date endDate) {
		this.eventId = eventId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        
	}

	public String getEventId() {
		return this.eventId;
	}

	public String getName() {
		return this.name;
	}

    public String getStartDate() {
		return this.startDate;
    }

    public String getEndDate() {
		return this.endDate;
	}
    
