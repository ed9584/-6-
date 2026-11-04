package com.project.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Locale;

/**
 * [추가] 회원가입 처리를 한 곳에서 검증하고 계정 저장소와 분리한다.
 * 서버가 역할을 USER로 고정하므로 요청에서 관리자 권한을 받을 수 없다.
 */
public final class AccountRegistrationService {
    private static final int MIN_USER_ID_LENGTH = 4;
    private static final int MAX_USER_ID_LENGTH = 30;
    private static final int MAX_EMAIL_LENGTH = 254;

    private final AccountRepository accountRepository;

    public AccountRegistrationService(AccountRepository accountRepository) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("회원 저장소가 필요합니다.");
        }
        this.accountRepository = accountRepository;
    }

    public RegistrationResult register(RegistrationRequest request) {
        try {
            if (request == null) return RegistrationResult.INVALID_INPUT;

            String userId = normalizeUserId(request.userId);
            String email = normalizeEmail(request.email);
            if (!isValidUserId(userId) || !isValidEmail(email)) {
                return RegistrationResult.INVALID_INPUT;
            }
            if (!isMatchingPassword(request.password, request.confirmPassword)) {
                return RegistrationResult.PASSWORD_MISMATCH;
            }
            if (!SecurityUtil.isPasswordPolicyValid(request.password)) {
                return RegistrationResult.PASSWORD_POLICY_FAILED;
            }
            if (isCommonPassword(request.password)) {
                return RegistrationResult.PASSWORD_POLICY_FAILED;
            }

            String canonicalIdentity = canonicalizeIdentity(userId, email);
            if (accountRepository.exists(canonicalizeKey(userId))
                    || accountRepository.exists(canonicalizeKey(email))) {
                // [보강] 기존 계정 존재 여부를 직접 노출하지 않는 일반적인 응답을 제공한다.
                return RegistrationResult.REGISTRATION_PROCESSED;
            }

            // [보강] 계정마다 새 솔트를 생성하고 저장소에서 원자적으로 생성한다.
            String salt = SecurityUtil.generateSalt();
            String hash = SecurityUtil.hashPassword(request.password, salt);
            NewAccount account = new NewAccount(
                    userId, email, salt, hash, canonicalIdentity, "USER");

            boolean created = accountRepository.createIfAbsent(account);
            return created
                    ? RegistrationResult.SUCCESS
                    : RegistrationResult.REGISTRATION_PROCESSED;
        } catch (Exception e) {
            // 저장소 오류나 해시 오류를 사용자에게 노출하지 않는다.
            return RegistrationResult.REJECTED;
        }
    }

    private static String normalizeUserId(String userId) {
        if (userId == null) return "";
        return Normalizer.normalize(userId, Normalizer.Form.NFKC)
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private static String normalizeEmail(String email) {
        if (email == null) return "";
        String normalized = Normalizer.normalize(email, Normalizer.Form.NFKC)
                .trim()
                .toLowerCase(Locale.ROOT);
        return normalized;
    }

    private static boolean isValidUserId(String userId) {
        if (userId.length() < MIN_USER_ID_LENGTH || userId.length() > MAX_USER_ID_LENGTH) {
            return false;
        }
        if (!userId.matches("[a-z0-9_-]+")) return false;
        return !userId.equals("admin")
                && !userId.equals("administrator")
                && !userId.startsWith("system")
                && !userId.contains(".")
                && !userId.startsWith("-");
    }

    private static boolean isValidEmail(String email) {
        if (email.isEmpty() || email.length() > MAX_EMAIL_LENGTH) return false;
        if (email.indexOf('\u200b') >= 0 || email.indexOf('\u200c') >= 0
                || email.indexOf('\u200d') >= 0 || email.indexOf('\ufeff') >= 0) {
            return false;
        }
        return email.matches("[a-z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-z0-9.-]+\\.[a-z]{2,}");
    }

    private static boolean isMatchingPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return MessageDigest.isEqual(
                password.getBytes(StandardCharsets.UTF_8),
                confirmPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean isCommonPassword(String password) {
        String normalized = Normalizer.normalize(password, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT);
        return normalized.equals("password1!")
                || normalized.equals("qwerty123!")
                || normalized.equals("welcome123!")
                || normalized.equals("admin123!");
    }

    private static String canonicalizeIdentity(String userId, String email) {
        return canonicalizeKey(userId + "\n" + email);
    }

    private static String canonicalizeKey(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            ByteBuffer encoded = StandardCharsets.UTF_8.encode(value);
            byte[] bytes = new byte[encoded.remaining()];
            encoded.get(bytes);
            return "acct:" + toHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 provider unavailable", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            result.append(String.format("%02x", value));
        }
        return result.toString();
    }

    public enum RegistrationResult {
        SUCCESS,
        REGISTRATION_PROCESSED,
        INVALID_INPUT,
        PASSWORD_MISMATCH,
        PASSWORD_POLICY_FAILED,
        REJECTED
    }

    public static final class RegistrationRequest {
        private final String userId;
        private final String email;
        private final String password;
        private final String confirmPassword;

        public RegistrationRequest(String userId, String email,
                                   String password, String confirmPassword) {
            this.userId = userId;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }
    }

    public static final class NewAccount {
        private final String userId;
        private final String email;
        private final String passwordSalt;
        private final String passwordHash;
        private final String canonicalIdentity;
        private final String role;

        private NewAccount(String userId, String email, String passwordSalt,
                           String passwordHash, String canonicalIdentity, String role) {
            this.userId = userId;
            this.email = email;
            this.passwordSalt = passwordSalt;
            this.passwordHash = passwordHash;
            this.canonicalIdentity = canonicalIdentity;
            this.role = role;
        }

        public String getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getPasswordSalt() {
            return passwordSalt;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public String getCanonicalIdentity() {
            return canonicalIdentity;
        }

        public String getRole() {
            return role;
        }
    }

    public interface AccountRepository {
        boolean exists(String canonicalIdentity);

        boolean createIfAbsent(NewAccount account);
    }
}
