package limiter;

import service.RateLimitConfig;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowStrategy extends RateLimitStrategy {
    private final Map<String, Queue<Long>> userTimeStamps = new ConcurrentHashMap<>();

    public SlidingWindowStrategy(RateLimitConfig rateLimitConfig) {
        super(rateLimitConfig);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        long windowMillis = rateLimitConfig.getWindowInSeconds() * 1000L;
        Queue<Long> timeStamps = userTimeStamps.computeIfAbsent(userId, key -> new ArrayDeque<>());
        synchronized (timeStamps) {
            while (!timeStamps.isEmpty() && now - timeStamps.peek() > windowMillis) {
                timeStamps.poll();
            }
            if (timeStamps.size() < rateLimitConfig.getMaxRequests()) {
                timeStamps.add(now);
                return true;
            }
            return false;
        }
    }
}
