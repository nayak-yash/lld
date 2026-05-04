package repo;

import models.Rider;

import java.util.HashMap;
import java.util.Map;

public class RiderRepository {
    private final Map<String, Rider> riderMap = new HashMap<>();

    public void save(Rider rider) {
        riderMap.put(rider.getId(), rider);
    }

    public Rider getRiderById(String riderId) {
        return riderMap.get(riderId);
    }
}
