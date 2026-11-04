
/*

### 3. 세션 및 권한 검증 매니저 (`SessionManager.java`)
로그인 상태 유지 및 IDOR(부적절한 인가/타인 데이터 무단 수정) 방어를 위한 권한 검증 모듈입니다.

*/
package com.project.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    public enum Role {
        USER,
        STAFF,
        ADMIN
    }

    // [보강] 스레드 간 세션 혼선을 방지하기 위해 요청 스레드별로 세션을 보관한다.
    private static final ThreadLocal<String> CURRENT_USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USER_ROLE = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_SESSION_ID = new ThreadLocal<>();
    private static final long SESSION_TIMEOUT_MILLIS = 30L * 60L * 1000L;
    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    // 로그인 세션 저장
    public static void login(String userId, String role) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("로그인 사용자 ID가 비어 있습니다.");
        }
        try {
            Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 사용자 권한입니다.");
        }
        String normalizedUserId = userId.trim();
        String normalizedRole = role.toUpperCase();
        String oldSessionId = CURRENT_SESSION_ID.get();
        if (oldSessionId != null) SESSIONS.remove(oldSessionId);

        CURRENT_USER_ID.set(normalizedUserId);
        CURRENT_USER_ROLE.set(normalizedRole);
        String sessionId = createSessionId();
        CURRENT_SESSION_ID.set(sessionId);
        SESSIONS.put(sessionId, new Session(normalizedUserId, normalizedRole,
                System.currentTimeMillis() + SESSION_TIMEOUT_MILLIS));
        CURRENT_USER_ROLE.set(role.toUpperCase());
    }

    // 로그아웃
    public static void logout() {
        String sessionId = CURRENT_SESSION_ID.get();
        if (sessionId != null) SESSIONS.remove(sessionId);
        CURRENT_USER_ID.remove();
        CURRENT_USER_ROLE.remove();
        CURRENT_SESSION_ID.remove();
    }

    public static boolean isLoggedIn() {
        String sessionId = CURRENT_SESSION_ID.get();
        if (sessionId == null) return false;
        Session session = SESSIONS.get(sessionId);
        if (session == null || session.expired()) {
            logout();
            return false;
        }
        return session.userId.equals(CURRENT_USER_ID.get());
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(CURRENT_USER_ROLE.get());
    }

    public static String getCurrentUserId() {
        return CURRENT_USER_ID.get();
    }

    // [추가] 서버/DB가 보관할 세션 ID를 외부 요청 계층에서 검증할 때 사용한다.
    public static String getSessionId() {
        return CURRENT_SESSION_ID.get();
    }

    public static boolean restoreSession(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) return false;
        Session session = SESSIONS.get(sessionId);
        if (session == null || session.expired()) {
            SESSIONS.remove(sessionId);
            return false;
        }
        CURRENT_SESSION_ID.set(sessionId);
        CURRENT_USER_ID.set(session.userId);
        CURRENT_USER_ROLE.set(session.role);
        return true;
    }

    public static void touchSession() {
        String sessionId = CURRENT_SESSION_ID.get();
        Session session = sessionId == null ? null : SESSIONS.get(sessionId);
        if (session != null && !session.expired()) {
            session.expiryMillis = System.currentTimeMillis() + SESSION_TIMEOUT_MILLIS;
        }
    }

    private static String createSessionId() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static final class Session {
        private final String userId;
        private final String role;
        private volatile long expiryMillis;

        private Session(String userId, String role, long expiryMillis) {
            this.userId = userId;
            this.role = role;
            this.expiryMillis = expiryMillis;
        }

        private boolean expired() {
            return System.currentTimeMillis() >= expiryMillis;
        }
    }

    // IDOR 방어: 현재 로그인한 사용자가 해당 게시물/민원의 작성자인지 검증
    public static boolean checkOwnership(String resourceOwnerId) {
        if (!isLoggedIn() || resourceOwnerId == null || resourceOwnerId.isBlank()) return false;
        // 관리자이거나 작성자 본인일 때만 권한 승인
        return isAdmin() || CURRENT_USER_ID.get().equals(resourceOwnerId.trim());
    }
}