package strategy.matching;

import models.Driver;
import models.Location;

import java.util.List;

public interface DriverMatchingStrategy {
    Driver findDriver(Location src, List<Driver> drivers);
}
