package ca.ubc.cs304.model;
import java.sql.Date;
import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a single program
 */
public class DayEventModel {
	private final int eventId;
	private final String name;
	private final java.sql.Date date;
	private final java.sql.Time time;

	public DayEventModel(int eventId, String name, Date date, Time time) {
		this.eventId = eventId;
		this.name = name;
		this.date = date;
		this.time = time;
	}

	public int getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}
}
    