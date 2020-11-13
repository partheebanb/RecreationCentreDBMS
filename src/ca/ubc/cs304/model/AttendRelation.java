package ca.ubc.cs304.model;

public class AttendRelation {
    private final int memberId;
    private final int eventId;

    public int getMemberId() {
        return memberId;
    }

    public int getEventId() {
        return eventId;
    }

    public AttendRelation(int memberId, int eventId) {
        this.memberId = memberId;
        this.eventId = eventId;
    }
}
