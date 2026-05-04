import enums.ProductType;
import models.*;
import repo.*;
import service.*;
import strategy.matching.NearestDriverMatchingStrategy;
import strategy.pricing.NightPricingStrategy;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 1. Setup repositories
        DriverRepository driverRepository = new DriverRepository();
        RiderRepository riderRepository = new RiderRepository();
        RideRepository rideRepository = new RideRepository();
        FareRepository fareRepository = new FareRepository();

        // 2. Setup services
        DriverService driverService = new DriverService(driverRepository);
        RiderService riderService = new RiderService(riderRepository);
        DriverMatchingService driverMatchingService = new DriverMatchingService(
                driverRepository, new NearestDriverMatchingStrategy());
        FareEstimationService fareEstimationService = new FareEstimationService(
                fareRepository, new NightPricingStrategy());
        RideService rideService = new RideService(fareRepository, rideRepository, driverMatchingService);

        // 3. Create products
        Product uberGo = new Product(ProductType.UBER_GO, 50, 10, 2);
        Product uberXl = new Product(ProductType.UBER_XL, 100, 15, 3);
        Product uberPremier = new Product(ProductType.UBER_PREMIER, 150, 20, 5);

        // 4. Register drivers with vehicles
        Vehicle vehicle1 = new Vehicle("V1", "KA-01-1234", List.of(uberGo, uberXl));
        Vehicle vehicle2 = new Vehicle("V2", "KA-01-5678", List.of(uberGo));
        Vehicle vehicle3 = new Vehicle("V3", "KA-01-9999", List.of(uberGo, uberXl, uberPremier));

        Driver driver1 = new Driver("D1", "Rahul", 4.5, vehicle1, new Location(12.97, 77.59));
        Driver driver2 = new Driver("D2", "Amit", 4.8, vehicle2, new Location(12.96, 77.58));
        Driver driver3 = new Driver("D3", "Suresh", 4.2, vehicle3, new Location(12.98, 77.60));

        driverService.registerDriver(driver1);
        driverService.registerDriver(driver2);
        driverService.registerDriver(driver3);

        // 5. Register a rider
        Rider rider = new Rider("R1", "Yash");
        riderService.registerRider(rider);

        // 6. Define pickup and drop locations
        Location pickup = new Location(12.97, 77.59);
        Location drop = new Location(13.00, 77.65);

        // 7. Get fare estimates for all products
        System.out.println("=== Fare Estimates ===");
        Map<Product, Double> estimates = fareEstimationService.getFareEstimates(
                pickup, drop, List.of(uberGo, uberXl, uberPremier));
        for (Map.Entry<Product, Double> entry : estimates.entrySet()) {
            System.out.println(entry.getKey() + " : Rs " + String.format("%.2f", entry.getValue()));
        }

        // 8. Rider selects UberGo and creates a fare
        System.out.println("\n=== Creating Fare for UBER_GO ===");
        Fare fare = fareEstimationService.createFare(rider.getId(), uberGo, pickup, drop);
        System.out.println("Fare created: " + fare);

        // 9. Request a ride using the fare
        System.out.println("\n=== Requesting Ride ===");
        try {
            Ride ride = rideService.requestRide(fare.getId(), uberGo, pickup, drop, rider);
            System.out.println("Ride confirmed: " + ride);
            System.out.println("Driver assigned: " + ride.getDriver());
            System.out.println("Vehicle: " + ride.getVehicle());
        } catch (RuntimeException e) {
            System.out.println("Ride request failed: " + e.getMessage());
        }

        // 10. Try requesting another ride (driver pool is smaller now)
        System.out.println("\n=== Requesting Another Ride (UBER_XL) ===");
        Fare fare2 = fareEstimationService.createFare(rider.getId(), uberXl, pickup, drop);
        try {
            Ride ride2 = rideService.requestRide(fare2.getId(), uberXl, pickup, drop, rider);
            System.out.println("Ride confirmed: " + ride2);
            System.out.println("Driver assigned: " + ride2.getDriver());
        } catch (RuntimeException e) {
            System.out.println("Ride request failed: " + e.getMessage());
        }

        // 11. Update driver location
        System.out.println("\n=== Updating Driver Location ===");
        driverService.updateLocation("D3", new Location(13.01, 77.66));
        System.out.println("Driver D3 location updated.");

        System.exit(0);
    }
}
