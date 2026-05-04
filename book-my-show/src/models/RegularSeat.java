package models;

import enums.SeatType;

public class RegularSeat extends Seat {
    public RegularSeat(double price) {
        super(price);
    }

    public SeatType getSeatType() {
        return SeatType.REGULAR;
    }
}
