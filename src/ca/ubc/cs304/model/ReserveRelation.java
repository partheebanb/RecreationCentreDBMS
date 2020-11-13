package ca.ubc.cs304.model;

public class ReserveRelation {
    public final int bookingId;
    public final int bookableId;

    public ReserveRelation(int bookingId, int bookableId) {
        this.bookingId = bookingId;
        this.bookableId = bookableId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getBookableId() {
        return bookableId;
    }
}
