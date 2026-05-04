package models;

import enums.ProductType;

public class Product {
    private final ProductType type;
    private final double baseFare;
    private final double perKmRate;
    private final double perMinRate;

    public Product(ProductType type, double baseFare, double perKmRate, double perMinRate) {
        this.type = type;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.perMinRate = perMinRate;
    }

    public ProductType getType() {
        return type;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public double getPerKmRate() {
        return perKmRate;
    }

    public double getPerMinRate() {
        return perMinRate;
    }

    @Override
    public String toString() {
        return type.name();
    }
}
