package ca.ubc.cs304.model;

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

	public int getEventId() {
		return eventId;
	}

	public int getBookableId() {
		return bookableId;
	}
}