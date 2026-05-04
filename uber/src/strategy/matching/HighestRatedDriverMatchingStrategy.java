package strategy.matching;

import models.Driver;
import models.Location;

import java.util.Comparator;
import java.util.List;

public class HighestRatedDriverMatchingStrategy implements DriverMatchingStrategy {
    @Override
    public Driver findDriver(Location src, List<Driver> drivers) {
        drivers.sort(Comparator.comparingDouble(Driver::getRating).reversed());
        for (Driver driver : drivers) {
            if (driver.markUnavailable()) {
                return driver;
            }
        }
        return null;
    }
}
