

// 비밀번호를 DB에 평문으로 저장하지 않고 SHA-256 + Salt(솔트) 기법으로 안전하게 해시화합니다.


package com.project.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtil {

    // 1. 무작위 솔트(Salt) 생성
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 2. 비밀번호 + 솔트 해시화 (SHA-256)
    public static String hashPassword(String rawPassword, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            
            // Byte to Hex String
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 알고리즘 생성 실패", e);
        }
    }

    // 3. 비밀번호 검증
    public static boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) {
        String inputHash = hashPassword(inputPassword, storedSalt);
        return inputHash.equals(storedHash);
    }
}
