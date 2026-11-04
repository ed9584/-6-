package com.project.security;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * [추가] 화면/API 경계에서 공통으로 사용하는 입력 검증 유틸리티.
 */
public final class InputValidator {
    private static final Pattern USER_ID = Pattern.compile("[a-z0-9_-]{4,30}");
    private static final Pattern EMAIL = Pattern.compile(
            "[a-z0-9.!#$%&'*+/=?^_`{|}~-]{1,64}@"
                    + "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9]"
                    + "(?:[a-z0-9-]{0,61}[a-z0-9])?)+");

    private InputValidator() {
    }

    public static String normalizeText(String value, int maxLength) {
        if (value == null) return null;
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFKC).trim();
        if (normalized.length() > maxLength) {
            throw new IllegalArgumentException("입력 길이가 허용 범위를 초과했습니다.");
        }
        return normalized;
    }

    public static boolean isValidUserId(String value) {
        return value != null && USER_ID.matcher(value).matches();
    }

    public static boolean isValidEmail(String value) {
        return value != null && value.length() <= 254
                && EMAIL.matcher(value.toLowerCase(Locale.ROOT)).matches();
    }

    public static boolean containsDangerousControlCharacter(String value) {
        if (value == null) return false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isISOControl(c) && c != '\n' && c != '\r' && c != '\t') {
                return true;
            }
        }
        return false;
    }

    // 로그인 실패 시 사용자 존재 여부와 무관하게 같은 형식의 오류를 사용한다.
    public static String authenticationFailureMessage() {
        return "아이디 또는 비밀번호가 올바르지 않습니다.";
    }
}
