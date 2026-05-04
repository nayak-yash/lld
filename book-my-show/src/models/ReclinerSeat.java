package models;

import enums.SeatType;

public class ReclinerSeat extends Seat {
    public ReclinerSeat(double price) {
        super(price);
    }

    public SeatType getSeatType() {
        return SeatType.RECLINER;
    }
}
