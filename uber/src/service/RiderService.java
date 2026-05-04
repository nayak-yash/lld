package service;

import models.Rider;
import repo.RiderRepository;

public class RiderService {
    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public void registerRider(Rider rider) {
        riderRepository.save(rider);
    }

    public Rider getRiderById(String riderId) {
        Rider rider = riderRepository.getRiderById(riderId);
        if (rider == null) {
            throw new RuntimeException("Rider not found with ID: " + riderId);
        }
        return rider;
    }
}
