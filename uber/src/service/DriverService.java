package service;

import models.Driver;
import models.Location;
import repo.DriverRepository;

public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void registerDriver(Driver driver) {
        driverRepository.save(driver);
    }

    public void updateLocation(String driverId, Location newLocation) {
        Driver driver = driverRepository.getById(driverId);
        if (driver == null) {
            throw new RuntimeException("Driver with id " + driverId + " not found");
        }
        driver.setCurrentLocation(newLocation);
    }
}
