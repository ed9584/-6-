

// 비밀번호를 DB에 평문으로 저장하지 않고 Salt + PBKDF2로 안전하게 해시화합니다.


package com.project.security;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;

public class SecurityUtil {

    // OWASP 권장 범위에 맞춰 서버 성능을 확인한 뒤 운영 환경에 적용한다.
    private static final int PBKDF2_ITERATIONS = 600_000;
    private static final int DERIVED_KEY_LENGTH = 256;

    // 1. 무작위 솔트(Salt) 생성
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // [보강] 암호학적으로 안전한 128비트 솔트
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 2. 비밀번호 + 솔트 해시화 (PBKDF2-HMAC-SHA256)
    public static String hashPassword(String rawPassword, String salt) {
        requireText(rawPassword, "비밀번호");
        requireText(salt, "솔트");
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), saltBytes,
                    PBKDF2_ITERATIONS, DERIVED_KEY_LENGTH);
            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                String hash = Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
                return "$pbkdf2-sha256$" + PBKDF2_ITERATIONS + "$" + salt + "$" + hash;
            } finally {
                spec.clearPassword();
            }
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new RuntimeException("해시 알고리즘 생성 실패", e);
        }
    }

    // 3. 비밀번호 검증
    public static boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) {
        if (inputPassword == null || storedHash == null || storedSalt == null) return false;
        try {
            PasswordHash parsed = PasswordHash.parse(storedHash);
            if (!"pbkdf2-sha256".equals(parsed.algorithm)) return false;
            byte[] actual = derive(inputPassword, storedSalt, parsed.iterations);
            byte[] expected = Base64.getDecoder().decode(parsed.hash);
            return constantTimeEquals(expected, actual);
        } catch (Exception e) {
            // [보강] 입력 형식 오류나 구형 데이터는 해당 계정만 인증 실패 처리한다.
            return false;
        }
    }

    private static boolean constantTimeEquals(byte[] expected, byte[] actual) {
        byte[] fixedExpected = new byte[DERIVED_KEY_LENGTH / 8];
        byte[] fixedActual = new byte[DERIVED_KEY_LENGTH / 8];
        System.arraycopy(expected, 0, fixedExpected, 0, Math.min(expected.length, fixedExpected.length));
        System.arraycopy(actual, 0, fixedActual, 0, Math.min(actual.length, fixedActual.length));
        int result = 0;
        result |= expected.length ^ fixedExpected.length;
        result |= actual.length ^ fixedActual.length;
        for (int i = 0; i < fixedExpected.length; i++) {
            result |= fixedExpected[i] ^ fixedActual[i];
        }
        return result == 0;
    }

    // [추가] 회원가입/비밀번호 변경 전에 공통 정책을 적용한다.
    public static boolean isPasswordPolicyValid(String password) {
        if (password == null || password.length() < 8 || password.length() > 128) return false;
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) upper = true;
            else if (Character.isLowerCase(c)) lower = true;
            else if (Character.isDigit(c)) digit = true;
            else if (!Character.isLetterOrDigit(c)) special = true;
        }
        return upper && lower && digit && special;
    }

    public static boolean needsRehash(String storedHash) {
        try {
            PasswordHash parsed = PasswordHash.parse(storedHash);
            return parsed.iterations < PBKDF2_ITERATIONS;
        } catch (Exception e) {
            return true;
        }
    }

    private static byte[] derive(String password, String salt, int iterations)
            throws GeneralSecurityException {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, DERIVED_KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    private static final class PasswordHash {
        private final String algorithm;
        private final int iterations;
        private final String hash;

        private PasswordHash(String algorithm, int iterations, String hash) {
            this.algorithm = algorithm;
            this.iterations = iterations;
            this.hash = hash;
        }

        private static PasswordHash parse(String value) {
            String[] parts = value.split("\\$", -1);
            if (parts.length != 5 || !parts[1].equals("pbkdf2-sha256")) {
                throw new IllegalArgumentException("지원하지 않는 비밀번호 해시 형식입니다.");
            }
            int iterations = Integer.parseInt(parts[2]);
            if (iterations < 100_000) throw new IllegalArgumentException("비밀번호 반복 횟수가 너무 낮습니다.");
            Base64.getDecoder().decode(parts[4]);
            return new PasswordHash(parts[1], iterations, parts[4]);
        }
    }

    private static void requireText(String value, String description) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(description + "이(가) 비어 있습니다.");
        }
    }
}
