package ca.ubc.cs304.model;

public class Reserve {
    public final int bookingId;
    public final int bookableId;

    public Reserve(int bookingId, int bookableId) {
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
