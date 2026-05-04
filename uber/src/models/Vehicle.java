package models;

import java.util.List;

public class Vehicle {
    private final String id;
    private final String licensePlate;
    private final List<Product> supportedProducts;

    public Vehicle(String id, String licensePlate, List<Product> supportedProducts) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.supportedProducts = supportedProducts;
    }

    public String getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public List<Product> getSupportedProducts() {
        return supportedProducts;
    }

    @Override
    public String toString() {
        return licensePlate;
    }
}
