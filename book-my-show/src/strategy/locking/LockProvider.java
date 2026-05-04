package strategy.locking;

public interface LockProvider {
    boolean lock(String key, String userId, long ttlMs);
    void unlock(String key);
    boolean isLockExpired(String key);
    boolean isLockedBy(String key, String userId);
}
