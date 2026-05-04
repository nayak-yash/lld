package limiter;

import service.RateLimitConfig;

public abstract class RateLimitStrategy {
    protected RateLimitConfig rateLimitConfig;

    public RateLimitStrategy(RateLimitConfig rateLimitConfig) {
        this.rateLimitConfig = rateLimitConfig;
    }

    public abstract boolean allowRequest(String userId);
}
