package models;

import java.util.UUID;

public class Ride {
    private final String id;
    private final Product product;
    private final Location src;
    private final Location dest;
    private final Rider rider;
    private Driver driver;
    private Vehicle vehicle;

    public Ride(Product product, Location src, Location dest, Rider rider) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.product = product;
        this.src = src;
        this.dest = dest;
        this.rider = rider;
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Location getSrc() {
        return src;
    }

    public Location getDest() {
        return dest;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Ride{id=" + id + ", product=" + product + ", rider=" + rider +
                ", driver=" + driver + ", from=" + src + ", to=" + dest + "}";
    }
}
