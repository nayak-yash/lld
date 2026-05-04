package limiter;

import service.RateLimitConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowStrategy extends RateLimitStrategy {
    private final Map<String, Window> userWindows = new ConcurrentHashMap<>();

    public FixedWindowStrategy(RateLimitConfig rateLimitConfig) {
        super(rateLimitConfig);
    }

    @Override
    public boolean allowRequest(String userId) {
        long currentWindowId = System.currentTimeMillis() / (rateLimitConfig.getWindowInSeconds() * 1000L);
        Window window = userWindows.computeIfAbsent(userId, key -> new Window(currentWindowId));
        return window.allow(currentWindowId, rateLimitConfig.getMaxRequests());
    }

    private static class Window {
        private long windowId;
        private int count;

        Window(long windowId) {
            this.windowId = windowId;
            this.count = 0;
        }

        public synchronized boolean allow(long currentId, int maxRequests) {
            if (windowId != currentId) {
                windowId = currentId;
                count = 0;
            }
            if (count < maxRequests) {
                count++;
                return true;
            }
            return false;
        }
    }
}
