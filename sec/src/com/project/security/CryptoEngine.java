/*
2. 양방향 데이터 암복호화 엔진 (`CryptoEngine.java`)
민원 상세 내용, 민감 정보, 파일 데이터 등을 AES-256 알고리즘으로 암호화하고 필요 시 복호화합니다.
*/

package com.project.security;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.GeneralSecurityException;

public class CryptoEngine {

    // [보강] 운영 환경에서는 반드시 외부 설정을 사용해야 한다.
    private static final String SECRET_KEY_PROPERTY = "app.security.crypto.key";
    private static final String SECRET_KEY_ENV = "APP_SECURITY_CRYPTO_KEY";
    private static final String SECURE_ALGORITHM = "AES/GCM/NoPadding";
    private static final byte[] VERSION = "AESGCM1".getBytes(StandardCharsets.US_ASCII);
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    // 1. 문자열 암호화
    public static String encrypt(String plainText) {
        requireText(plainText, "암호화할 평문");
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            SecretKeySpec keySpec = getSecretKey();
            Cipher cipher = Cipher.getInstance(SECURE_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH, iv));

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // [추가] 버전 + IV + 암호문 형식으로 저장하여 알고리즘 변경과 복호화 호환성을 관리한다.
            byte[] combined = new byte[VERSION.length + iv.length + encrypted.length];
            System.arraycopy(VERSION, 0, combined, 0, VERSION.length);
            System.arraycopy(iv, 0, combined, VERSION.length, iv.length);
            System.arraycopy(encrypted, 0, combined, VERSION.length + iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new RuntimeException("AES 암호화 실패", e);
        }
    }

    // 2. 문자열 복호화
    public static String decrypt(String cipherText) {
        requireText(cipherText, "복호화할 암호문");
        try {
            byte[] combined = Base64.getDecoder().decode(cipherText);
            if (startsWithVersion(combined)) {
                int encryptedOffset = VERSION.length + IV_LENGTH;
                if (combined.length <= encryptedOffset) {
                    throw new IllegalArgumentException("암호문 길이가 올바르지 않습니다.");
                }
                byte[] iv = copy(combined, VERSION.length, IV_LENGTH);
                byte[] encrypted = copy(combined, encryptedOffset, combined.length - encryptedOffset);

                Cipher cipher = Cipher.getInstance(SECURE_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new GCMParameterSpec(TAG_LENGTH, iv));
                return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
            }

            throw new IllegalArgumentException("인증되지 않은 레거시 암호문 형식은 더 이상 복호화할 수 없습니다.");
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new RuntimeException("AES 복호화 실패", e);
        }
    }

    private static SecretKeySpec getSecretKey() {
        String key = System.getProperty(SECRET_KEY_PROPERTY);
        if (key == null || key.isBlank()) {
            key = System.getenv(SECRET_KEY_ENV);
        }
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "암호키가 설정되지 않았습니다. app.security.crypto.key 또는 "
                            + SECRET_KEY_ENV + "를 설정하세요.");
        }

        byte[] keyBytes = decodeKey(key);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static byte[] decodeKey(String key) {
        byte[] keyBytes;
        if (key.startsWith("base64:")) {
            try {
                keyBytes = Base64.getDecoder().decode(key.substring("base64:".length()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Base64 암호키 형식이 올바르지 않습니다.", e);
            }
        } else {
            keyBytes = key.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("AES-256 비밀키는 UTF-8 기준 정확히 32바이트여야 합니다.");
        }
        return keyBytes;
    }

    private static void requireText(String value, String description) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(description + "이(가) 비어 있습니다.");
        }
    }

    private static boolean startsWithVersion(byte[] data) {
        if (data.length < VERSION.length) return false;
        for (int i = 0; i < VERSION.length; i++) {
            if (data[i] != VERSION[i]) return false;
        }
        return true;
    }

    private static byte[] copy(byte[] source, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(source, offset, result, 0, length);
        return result;
    }
}