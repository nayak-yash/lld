package models;

import java.util.UUID;

public abstract class Seat {
    private final String id;
    private final double price;

    Seat(double price) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}
