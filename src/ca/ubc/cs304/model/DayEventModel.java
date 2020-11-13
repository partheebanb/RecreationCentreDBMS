package ca.ubc.cs304.model;
import java.sql.Date;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class DayEventModel {
    private final int eventId;
    private final String name;
    private final java.sql.Date date;
    private final java.sql.Time time;
	
	public DayEventModel(int eventId, String name, java.sql.Date date, java.sql.Date endDate) {
		this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.time = time;
	}

	public String getEventId() {
		return this.eventId;
	}

	public String getName() {
		return this.name;
	}

    public String getDate() {
		return this.date;
    }

    public String getTime() {
		return this.time;
	}
    
    