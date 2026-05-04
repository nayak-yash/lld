package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Screen {
    private final String id;
    private final List<Seat> seats;

    public Screen() {
        this.id = UUID.randomUUID().toString();
        this.seats = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
