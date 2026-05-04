package strategy.pricing;

import models.Location;

import java.time.LocalTime;

public class NightPricingStrategy implements PricingStrategy {
    @Override
    public double calculateFare(Location src, Location dest) {
        //In real application, use user's time zone not server
        LocalTime now = LocalTime.now();
        boolean isNight = now.isAfter(LocalTime.of(22, 0)) || now.isBefore(LocalTime.of(6, 0));
        return isNight ? 50.0 : 0.0; // Flat surge of Rs 50
    }
}
