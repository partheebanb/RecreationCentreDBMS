package ca.ubc.cs304.model;

public class Attend {
    private final int memberId;
    private final int eventId;

    public int getMemberId() {
        return memberId;
    }

    public int getEventId() {
        return eventId;
    }

    public Attend(int memberId, int eventId) {
        this.memberId = memberId;
        this.eventId = eventId;
    }
}
