package strategy.pricing;

import models.Location;

public interface PricingStrategy {
    double calculateFare(Location src, Location dest);
}
