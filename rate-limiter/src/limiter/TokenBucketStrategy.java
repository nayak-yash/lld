package limiter;

import service.RateLimitConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketStrategy extends RateLimitStrategy {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketStrategy(RateLimitConfig rateLimitConfig) {
        super(rateLimitConfig);
    }

    @Override
    public boolean allowRequest(String userId) {
        Bucket bucket = buckets.computeIfAbsent(userId, key -> new Bucket(rateLimitConfig.getMaxRequests()));
        return bucket.consume(rateLimitConfig.getMaxRequests(), rateLimitConfig.getWindowInSeconds());
    }

    private static class Bucket {
        private double tokens;
        private long lastRefillTime;

        Bucket(double initialTokens) {
            this.tokens = initialTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized boolean consume(int maxRequest, int windowInSeconds) {
            double refillRate = (double) maxRequest / windowInSeconds;
            long now = System.currentTimeMillis();
            double tokensToAdd = (now - lastRefillTime) / (refillRate * 1000.0);
            tokens = Math.min(maxRequest, tokens + tokensToAdd);
            lastRefillTime = now;
            if (tokens >= 1) {
                tokens--;
                return true;
            }
            return false;
        }
    }
}
