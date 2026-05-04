package repo;

import models.Fare;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FareRepository {
    private static class FareEntry {
        final String riderId;
        final Fare fare;
        final Instant expiryTime;

        public FareEntry(String riderId, Fare fare, Instant expiryTime) {
            this.riderId = riderId;
            this.fare = fare;
            this.expiryTime = expiryTime;
        }
    }

    private final Map<String, FareEntry> fareCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public FareRepository() {
        cleaner.scheduleAtFixedRate(this::evictExpiredFares, 10, 10, TimeUnit.SECONDS);
    }

    private void evictExpiredFares() {
        Instant now = Instant.now();
        fareCache.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expiryTime));
    }

    public void save(String fareId, Fare fare, String riderId, Duration ttl) {
        Instant expiryTime = Instant.now().plus(ttl);
        fareCache.put(fareId, new FareEntry(riderId, fare, expiryTime));
    }

    public Fare getFare(String fareId, String riderId) {
        FareEntry fareEntry = fareCache.get(fareId);
        if (fareEntry == null || Instant.now().isAfter(fareEntry.expiryTime) || !fareEntry.riderId.equals(riderId)) {
            return null;
        }
        return fareEntry.fare;
    }
}
