package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class UseRelation {
	private final int eventId;
	private final int bookableId;

	public UseRelation(int eventId, int bookableId) {
		this.eventId = eventId;
		this.bookableId = bookableId;
	}

	public int getEventId() {
		return this.eventId;
	}

	public int getBookableId() {
		return this.bookableId;
	}
}


