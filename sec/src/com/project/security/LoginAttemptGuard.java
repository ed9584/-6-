package com.project.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [추가] 로그인 요청 계층에서 IP 또는 계정별 실패 횟수를 제한한다.
 * 영구 잠금 대신 시간 기반 제한으로 정상 사용자 차단을 줄인다.
 */
public final class LoginAttemptGuard {
    private static final long WINDOW_MILLIS = 15L * 60L * 1000L;
    private static final int MAX_FAILURES = 5;
    private static final Map<String, Attempt> ATTEMPTS = new ConcurrentHashMap<>();

    private LoginAttemptGuard() {
    }

    public static boolean isBlocked(String key) {
        if (key == null || key.isBlank()) return true;
        Attempt attempt = ATTEMPTS.get(key.trim());
        if (attempt == null) return false;
        if (System.currentTimeMillis() >= attempt.blockedUntilMillis) {
            ATTEMPTS.remove(key.trim(), attempt);
            return false;
        }
        return attempt.failures >= MAX_FAILURES;
    }

    public static void recordFailure(String key) {
        if (key == null || key.isBlank()) return;
        String normalizedKey = key.trim();
        long now = System.currentTimeMillis();
        ATTEMPTS.compute(normalizedKey, (ignored, attempt) -> {
            if (attempt == null || now - attempt.firstFailureMillis >= WINDOW_MILLIS) {
                return new Attempt(now, now, 1);
            }
            int failures = attempt.failures + 1;
            long blockedUntil = failures >= MAX_FAILURES
                    ? now + WINDOW_MILLIS
                    : attempt.blockedUntilMillis;
            return new Attempt(attempt.firstFailureMillis, blockedUntil, failures);
        });
    }

    public static void recordSuccess(String key) {
        if (key != null && !key.isBlank()) {
            ATTEMPTS.remove(key.trim());
        }
    }

    public static int remainingAttempts(String key) {
        if (key == null || key.isBlank()) return 0;
        Attempt attempt = ATTEMPTS.get(key.trim());
        if (attempt == null) return MAX_FAILURES;
        if (System.currentTimeMillis() >= attempt.blockedUntilMillis) return MAX_FAILURES;
        return Math.max(0, MAX_FAILURES - attempt.failures);
    }

    private static final class Attempt {
        private final long firstFailureMillis;
        private final long blockedUntilMillis;
        private final int failures;

        private Attempt(long firstFailureMillis, long blockedUntilMillis, int failures) {
            this.firstFailureMillis = firstFailureMillis;
            this.blockedUntilMillis = blockedUntilMillis;
            this.failures = failures;
        }
    }
}
