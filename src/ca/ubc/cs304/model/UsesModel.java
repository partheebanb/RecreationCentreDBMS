package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class UsesModel {
    private final int eventId;
    private final int bookableId;

	public UsesModel(int eventId, int bookableId) {
		this.eventId = eventId;
        this.bookableId = bookableId;
	}

	public String getEventId() {
		return this.eventId;
	}

	public String getBookableId() {
		return this.bookableId;
	}


