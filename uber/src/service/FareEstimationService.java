package service;

import models.Fare;
import models.Product;
import models.Location;
import repo.FareRepository;
import strategy.pricing.PricingStrategy;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FareEstimationService {
    private final FareRepository fareRepository;
    private final PricingStrategy pricingStrategy;

    public FareEstimationService(FareRepository fareRepository, PricingStrategy pricingStrategy) {
        this.fareRepository = fareRepository;
        this.pricingStrategy = pricingStrategy;
    }

    public Map<Product,Double> getFareEstimates(Location src, Location dest, List<Product> products) {
        Map<Product,Double> estimates = new HashMap<>();
        double distanceKm = src.distanceTo(dest);
        int durationMin = (int) distanceKm * 2;
        for  (Product product : products) {
            double baseFare = calculateBaseFare(product, distanceKm, durationMin);
            double surge = pricingStrategy.calculateFare(src, dest);
            baseFare += surge;
            estimates.put(product, baseFare);
        }
        return estimates;
    }

    public Fare createFare(String riderId, Product product, Location src, Location dest) {
        double distanceKm = src.distanceTo(dest);
        int durationMin = (int) distanceKm * 2;
        double baseFare = calculateBaseFare(product, distanceKm, durationMin);
        double surge = pricingStrategy.calculateFare(src, dest);
        baseFare += surge;
        Fare fare = new Fare(product, src, dest, baseFare);
        fareRepository.save(fare.getId(), fare, riderId, Duration.ofSeconds(5));
        return fare;
    }

    private double calculateBaseFare(Product product, double distanceKm, double durationMin) {
        return product.getBaseFare() + (product.getPerKmRate() * distanceKm) + (product.getPerMinRate() * durationMin);
    }
}
