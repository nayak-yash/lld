package service;

import models.Driver;
import models.Location;
import models.Product;
import repo.DriverRepository;
import strategy.matching.DriverMatchingStrategy;

import java.util.List;

public class DriverMatchingService {
    private final DriverRepository driverRepository;
    private final DriverMatchingStrategy driverMatchingStrategy;

    public DriverMatchingService(DriverRepository driverRepository, DriverMatchingStrategy driverMatchingStrategy) {
        this.driverRepository = driverRepository;
        this.driverMatchingStrategy = driverMatchingStrategy;
    }

    public Driver findDriver(Location src, Product product) {
        List<Driver> nearbyDrivers = driverRepository.getNearbyDrivers(src, 2);
        List<Driver> filteredDrivers = nearbyDrivers.stream()
                .filter(driver -> driver.getVehicle() != null &&
                        driver.getVehicle().getSupportedProducts().contains(product))
                .toList();
        return driverMatchingStrategy.findDriver(src, filteredDrivers);
    }
}
