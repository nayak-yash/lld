package strategy.locking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryLockProvider implements LockProvider {
    private static class Expiry {
        final long deadline;
        final String owner;

        Expiry(long deadline, String owner) {
            this.deadline = deadline;
            this.owner = owner;
        }
    }

    private final Map<String, Expiry> locks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService sweeper = Executors.newSingleThreadScheduledExecutor();

    public InMemoryLockProvider() {
        sweeper.scheduleAtFixedRate(this::sweep, 1, 1, TimeUnit.MINUTES);
    }

    private void sweep() {
        long now = System.currentTimeMillis();
        locks.entrySet().removeIf(entry -> entry.getValue().deadline <= now);
    }

    @Override
    public boolean lock(String key, String userId, long ttlMs) {
        long now = System.currentTimeMillis();
        Expiry expiry = new Expiry(now + ttlMs, userId);
        return locks.compute(key, (k, v) -> (v == null || v.deadline <= now) ? expiry : v) == expiry;
    }

    @Override
    public void unlock(String key) {
        locks.remove(key);
    }

    @Override
    public boolean isLockExpired(String key) {
        Expiry expiry = locks.get(key);
        return expiry != null && expiry.deadline < System.currentTimeMillis();
    }

    @Override
    public boolean isLockedBy(String key, String userId) {
        Expiry expiry = locks.get(key);
        return expiry != null && expiry.owner.equals(userId);
    }
}
