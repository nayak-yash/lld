package strategy.matching;

import models.Driver;
import models.Location;

import java.util.Comparator;
import java.util.List;

public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
    @Override
    public Driver findDriver(Location src, List<Driver> drivers) {
        drivers.sort(Comparator.comparingDouble(driver -> driver.getCurrentLocation().distanceTo(src)));
        for (Driver driver : drivers) {
            if (driver.markUnavailable()) {
                return driver;
            }
        }
        return null;
    }
}
