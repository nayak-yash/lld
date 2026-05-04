package models;

import java.util.concurrent.atomic.AtomicBoolean;

public class Driver {
    private final String id;
    private final String name;
    private final double rating;
    private final Vehicle vehicle;
    private Location currentLocation;
    private final AtomicBoolean available;

    public Driver(String id, String name, double rating, Vehicle vehicle, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.vehicle = vehicle;
        this.currentLocation = currentLocation;
        this.available = new AtomicBoolean(true);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isAvailable() {
        return available.get();
    }

    public boolean markUnavailable() {
        return available.compareAndSet(true, false);
    }

    @Override
    public String toString() {
        return name + " (Rating: " + rating + ")";
    }
}
