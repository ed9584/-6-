
/*

### 3. 세션 및 권한 검증 매니저 (`SessionManager.java`)
로그인 상태 유지 및 IDOR(부적절한 인가/타인 데이터 무단 수정) 방어를 위한 권한 검증 모듈입니다.

*/
package com.project.security;

public class SessionManager {

    private static String currentUserId = null;
    private static String currentUserRole = null; // "USER", "ADMIN"

    // 로그인 세션 저장
    public static void login(String userId, String role) {
        currentUserId = userId;
        currentUserRole = role;
    }

    // 로그아웃
    public static void logout() {
        currentUserId = null;
        currentUserRole = null;
    }

    public static boolean isLoggedIn() {
        return currentUserId != null;
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(currentUserRole);
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    // IDOR 방어: 현재 로그인한 사용자가 해당 게시물/민원의 작성자인지 검증
    public static boolean checkOwnership(String resourceOwnerId) {
        if (!isLoggedIn()) return false;
        // 관리자이거나 작성자 본인일 때만 권한 승인
        return isAdmin() || currentUserId.equals(resourceOwnerId);
    }
}