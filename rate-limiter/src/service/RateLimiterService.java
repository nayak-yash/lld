package service;

import enums.RateLimitType;
import enums.UserTier;
import factory.RateLimiterFactory;
import limiter.RateLimitStrategy;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {

    private final Map<UserTier, RateLimitStrategy> rateLimiters = new HashMap<>();

    public RateLimiterService() {
        rateLimiters.put(UserTier.FREE, RateLimiterFactory.createRateLimiter(
                RateLimitType.TOKEN_BUCKET,
                new RateLimitConfig(10, 40)
        ));
        rateLimiters.put(UserTier.PREMIUM, RateLimiterFactory.createRateLimiter(
                RateLimitType.TOKEN_BUCKET,
                new RateLimitConfig(100, 40)
        ));
    }

    public boolean getRateLimiter(User user) {
        RateLimitStrategy rateLimiter = rateLimiters.get(user.getTier());
        if (rateLimiter == null) {
            throw new IllegalArgumentException("No limiter configured for tier: " + user.getTier());
        }
        return rateLimiter.allowRequest(user.getUserId());
    }
}
