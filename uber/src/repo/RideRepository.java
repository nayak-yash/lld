package repo;

import models.Ride;

import java.util.HashMap;
import java.util.Map;

public class RideRepository {
    private final Map<String, Ride> rideMap = new HashMap<>();

    public void save(Ride ride) {
        rideMap.put(ride.getId(), ride);
    }

    public Ride getById(String rideId) {
        return rideMap.get(rideId);
    }
}
