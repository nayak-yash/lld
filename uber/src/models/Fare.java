package models;

import java.util.UUID;

public class Fare {
    private final String id;
    private final Product product;
    private final Location src;
    private final Location dest;
    private final double price;

    public Fare(Product product, Location src, Location dest, double price) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.product = product;
        this.src = src;
        this.dest = dest;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Fare{id=" + id + ", product=" + product + ", price=" + price + "}";
    }
}
