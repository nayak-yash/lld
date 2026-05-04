package factory;

import enums.RateLimitType;
import limiter.FixedWindowStrategy;
import limiter.SlidingWindowStrategy;
import service.RateLimitConfig;
import limiter.RateLimitStrategy;
import limiter.TokenBucketStrategy;

public class RateLimiterFactory {
    public static RateLimitStrategy createRateLimiter(RateLimitType rateLimitType, RateLimitConfig rateLimitConfig) {
        return switch (rateLimitType) {
            case TOKEN_BUCKET -> new TokenBucketStrategy(rateLimitConfig);
            case FIXED_WINDOW -> new FixedWindowStrategy(rateLimitConfig);
            case SLIDING_WINDOW -> new SlidingWindowStrategy(rateLimitConfig);
            default -> throw new IllegalArgumentException("Unsupported Rate Limit Type: " + rateLimitType);
        };
    }
}
