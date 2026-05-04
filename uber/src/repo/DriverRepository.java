package repo;

import models.Driver;
import models.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRepository {
    private final Map<String, Driver> driverMap = new HashMap<>();

    public void save(Driver driver) {
        driverMap.put(driver.getId(), driver);
    }

    public Driver getById(String driverId) {
        return driverMap.get(driverId);
    }

    public List<Driver> getNearbyDrivers(Location src, int radius) {
        return driverMap.values().stream()
                .filter(driver -> driver.isAvailable() &&
                        src.distanceTo(driver.getCurrentLocation()) <= radius)
                .toList();
    }
}
