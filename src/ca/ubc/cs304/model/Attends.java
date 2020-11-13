package ca.ubc.cs304.model;

public class Attends {
    private final int memberId;
    private final int eventId;

    public int getMemberId() {
        return memberId;
    }

    public int getEventId() {
        return eventId;
    }

    public Attends(int memberId, int eventId) {
        this.memberId = memberId;
        this.eventId = eventId;
    }
}
