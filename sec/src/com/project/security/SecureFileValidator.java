package com.project.security;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

/**
 * [추가] 업로드 파일의 확장자, 이름, 크기를 검증한다.
 * 실제 저장은 검증된 내용과 서버 생성 파일명으로 별도 구현해야 한다.
 */
public final class SecureFileValidator {
    private static final long MAX_FILE_SIZE = 10L * 1024L * 1024L;
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png");

    private SecureFileValidator() {
    }

    public static boolean validate(String originalName, long size) {
        if (originalName == null || originalName.isBlank()
                || originalName.length() > 100 || size <= 0 || size > MAX_FILE_SIZE) {
            return false;
        }
        Path name = Path.of(originalName).getFileName();
        String safeName = name.toString();
        if (!safeName.equals(originalName)
                || safeName.contains("..")
                || originalName.indexOf('/') >= 0
                || originalName.indexOf('\\') >= 0) {
            return false;
        }

        int dot = safeName.lastIndexOf('.');
        if (dot <= 0 || dot == safeName.length() - 1) return false;
        String extension = safeName.substring(dot + 1).toLowerCase(Locale.ROOT);
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }
}
