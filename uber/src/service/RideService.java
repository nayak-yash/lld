package service;

import models.Driver;
import models.Fare;
import models.Location;
import models.Product;
import models.Ride;
import models.Rider;
import repo.FareRepository;
import repo.RideRepository;

public class RideService {
    private final FareRepository fareRepository;
    private final RideRepository rideRepository;
    private final DriverMatchingService driverMatchingService;

    public RideService(FareRepository fareRepository, RideRepository rideRepository, DriverMatchingService driverMatchingService) {
        this.fareRepository = fareRepository;
        this.rideRepository = rideRepository;
        this.driverMatchingService = driverMatchingService;
    }

    public Ride requestRide(String fareId, Product product, Location src, Location dest, Rider rider) {
        Fare fare = fareRepository.getFare(fareId, rider.getId());
        if (fare == null) {
            throw new RuntimeException("Fare has expired. Please try again.");
        }
        if (fare.getProduct().getType() != product.getType()) {
            throw new RuntimeException("Fare doesn't match selected product.");
        }
        Ride ride = new Ride(product, src, dest, rider);
        Driver driver = driverMatchingService.findDriver(src, product);
        if (driver == null) {
            throw new RuntimeException("Driver not found for this ride.");
        }
        ride.setDriver(driver);
        ride.setVehicle(driver.getVehicle());
        rideRepository.save(ride);
        return ride;
    }
}
