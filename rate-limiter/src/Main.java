import enums.UserTier;
import models.User;
import service.RateLimiterService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("========== Rate Limiter Tests ==========\n");

        testFreeUserAllowedUpToLimit();
        testFreeUserDeniedAfterLimit();
        testPremiumUserHigherLimit();
        testDifferentUsersHaveIndependentLimits();
        testConcurrentRequestsSingleUser();
        testConcurrentRequestsMultipleUsers();
        testConcurrencyNoExtraAllowed();

        System.out.println("\n========== Results ==========");
        System.out.println("Passed: " + passed + " | Failed: " + failed);
        if (failed > 0) {
            System.out.println("SOME TESTS FAILED!");
        } else {
            System.out.println("ALL TESTS PASSED!");
        }
    }

    // --- Helper ---
    private static User createUser(String userId, UserTier tier) {
        User user = new User();
        user.setUserId(userId);
        user.setTier(tier);
        return user;
    }

    private static void assertCondition(String testName, boolean condition, String message) {
        if (condition) {
            System.out.println("  ✅ PASS: " + testName);
            passed++;
        } else {
            System.out.println("  ❌ FAIL: " + testName + " — " + message);
            failed++;
        }
    }

    // ===================== TEST CASES =====================

    /**
     * Test 1: A FREE user should be allowed up to 10 requests (maxRequests=10).
     */
    private static void testFreeUserAllowedUpToLimit() {
        System.out.println("[Test] Free user allowed up to limit");
        RateLimiterService service = new RateLimiterService();
        User freeUser = createUser("free-1", UserTier.FREE);

        int allowedCount = 0;
        for (int i = 0; i < 10; i++) {
            if (service.getRateLimiter(freeUser)) {
                allowedCount++;
            }
        }
        assertCondition("All 10 requests allowed", allowedCount == 10,
                "Expected 10 allowed, got " + allowedCount);
    }

    /**
     * Test 2: A FREE user should be denied after exhausting 10 tokens.
     */
    private static void testFreeUserDeniedAfterLimit() {
        System.out.println("[Test] Free user denied after limit");
        RateLimiterService service = new RateLimiterService();
        User freeUser = createUser("free-2", UserTier.FREE);

        // exhaust all 10 tokens
        for (int i = 0; i < 10; i++) {
            service.getRateLimiter(freeUser);
        }
        boolean eleventhAllowed = service.getRateLimiter(freeUser);
        assertCondition("11th request denied", !eleventhAllowed,
                "Expected 11th request to be denied but it was allowed");
    }

    /**
     * Test 3: A PREMIUM user should have a higher limit (100 requests).
     */
    private static void testPremiumUserHigherLimit() {
        System.out.println("[Test] Premium user higher limit");
        RateLimiterService service = new RateLimiterService();
        User premiumUser = createUser("premium-1", UserTier.PREMIUM);

        int allowedCount = 0;
        for (int i = 0; i < 100; i++) {
            if (service.getRateLimiter(premiumUser)) {
                allowedCount++;
            }
        }
        assertCondition("All 100 requests allowed for premium", allowedCount == 100,
                "Expected 100 allowed, got " + allowedCount);

        boolean next = service.getRateLimiter(premiumUser);
        assertCondition("101st request denied for premium", !next,
                "Expected 101st request to be denied but it was allowed");
    }

    /**
     * Test 4: Different users should have independent rate limits.
     */
    private static void testDifferentUsersHaveIndependentLimits() {
        System.out.println("[Test] Different users have independent limits");
        RateLimiterService service = new RateLimiterService();
        User userA = createUser("indep-A", UserTier.FREE);
        User userB = createUser("indep-B", UserTier.FREE);

        // exhaust userA's tokens
        for (int i = 0; i < 10; i++) {
            service.getRateLimiter(userA);
        }
        boolean userADenied = !service.getRateLimiter(userA);
        boolean userBAllowed = service.getRateLimiter(userB);

        assertCondition("User A denied after exhaustion", userADenied,
                "Expected User A to be denied");
        assertCondition("User B still allowed", userBAllowed,
                "Expected User B to still be allowed");
    }

    /**
     * Test 5 (Concurrency): Multiple threads hit the rate limiter for the same FREE user.
     * Total allowed should not exceed maxRequests (10).
     */
    private static void testConcurrentRequestsSingleUser() throws Exception {
        System.out.println("[Test] Concurrent requests — single FREE user");
        RateLimiterService service = new RateLimiterService();
        User user = createUser("conc-single", UserTier.FREE);

        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger denied = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    if (service.getRateLimiter(user)) {
                        allowed.incrementAndGet();
                    } else {
                        denied.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        System.out.println("    Allowed: " + allowed.get() + ", Denied: " + denied.get());
        assertCondition("Allowed <= 10 (no over-granting)", allowed.get() <= 10,
                "Expected at most 10 allowed, got " + allowed.get());
        assertCondition("Total = 20", allowed.get() + denied.get() == 20,
                "Expected 20 total, got " + (allowed.get() + denied.get()));
    }

    /**
     * Test 6 (Concurrency): Multiple users sending requests concurrently.
     * Each FREE user should get at most 10 allowed out of 15 attempts.
     */
    private static void testConcurrentRequestsMultipleUsers() throws Exception {
        System.out.println("[Test] Concurrent requests — multiple FREE users");
        RateLimiterService service = new RateLimiterService();
        int userCount = 5;
        int requestsPerUser = 15;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            users.add(createUser("multi-" + i, UserTier.FREE));
        }

        ExecutorService executor = Executors.newFixedThreadPool(userCount * requestsPerUser);
        ConcurrentHashMap<String, AtomicInteger> allowedPerUser = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(userCount * requestsPerUser);

        for (User user : users) {
            allowedPerUser.put(user.getUserId(), new AtomicInteger(0));
            for (int r = 0; r < requestsPerUser; r++) {
                executor.submit(() -> {
                    try {
                        if (service.getRateLimiter(user)) {
                            allowedPerUser.get(user.getUserId()).incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        boolean allWithinLimit = true;
        for (User user : users) {
            int count = allowedPerUser.get(user.getUserId()).get();
            System.out.println("    " + user.getUserId() + " allowed: " + count);
            if (count > 10) {
                allWithinLimit = false;
            }
        }
        assertCondition("Each user allowed <= 10", allWithinLimit,
                "Some user exceeded 10 allowed requests");
    }

    /**
     * Test 7 (Concurrency - strict): Send exactly 10 requests concurrently for a FREE user.
     * All 10 should be allowed. Then send 10 more — all should be denied.
     */
    private static void testConcurrencyNoExtraAllowed() throws Exception {
        System.out.println("[Test] Concurrency strict — exactly 10 then 10 more");
        RateLimiterService service = new RateLimiterService();
        User user = createUser("conc-strict", UserTier.FREE);

        // Phase 1: send exactly 10 concurrent requests
        int phase1Count = 10;
        ExecutorService executor1 = Executors.newFixedThreadPool(phase1Count);
        AtomicInteger allowedPhase1 = new AtomicInteger(0);
        CountDownLatch latch1 = new CountDownLatch(phase1Count);

        for (int i = 0; i < phase1Count; i++) {
            executor1.submit(() -> {
                try {
                    if (service.getRateLimiter(user)) {
                        allowedPhase1.incrementAndGet();
                    }
                } finally {
                    latch1.countDown();
                }
            });
        }
        latch1.await(5, TimeUnit.SECONDS);
        executor1.shutdown();

        assertCondition("Phase 1: all 10 allowed", allowedPhase1.get() == 10,
                "Expected 10 allowed, got " + allowedPhase1.get());

        // Phase 2: send 10 more concurrent requests (should all be denied)
        int phase2Count = 10;
        ExecutorService executor2 = Executors.newFixedThreadPool(phase2Count);
        AtomicInteger allowedPhase2 = new AtomicInteger(0);
        CountDownLatch latch2 = new CountDownLatch(phase2Count);

        for (int i = 0; i < phase2Count; i++) {
            executor2.submit(() -> {
                try {
                    if (service.getRateLimiter(user)) {
                        allowedPhase2.incrementAndGet();
                    }
                } finally {
                    latch2.countDown();
                }
            });
        }
        latch2.await(5, TimeUnit.SECONDS);
        executor2.shutdown();

        assertCondition("Phase 2: all 10 denied", allowedPhase2.get() == 0,
                "Expected 0 allowed, got " + allowedPhase2.get());
    }
}