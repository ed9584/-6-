package com.example.school_complaint_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png"
    );

    private final Path uploadRoot;

    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir
    ) {
        this.uploadRoot = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    public StoredFile store(MultipartFile file, Long complaintId) {

        validate(file);

        try {
            Path complaintDirectory = uploadRoot
                    .resolve(String.valueOf(complaintId))
                    .normalize();

            Files.createDirectories(complaintDirectory);

            String originalFileName = file.getOriginalFilename();
            String extension = getExtension(originalFileName);

            String storedFileName =
                    UUID.randomUUID() + extension;

            Path target = complaintDirectory
                    .resolve(storedFileName)
                    .normalize();

            file.transferTo(target);

            return new StoredFile(
                    originalFileName == null ? "unknown" : originalFileName,
                    storedFileName,
                    target.toString(),
                    file.getContentType(),
                    file.getSize()
            );

        } catch (IOException e) {
            throw new IllegalStateException(
                    "파일 저장에 실패했습니다.",
                    e
            );
        }
    }

    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "빈 파일은 업로드할 수 없습니다."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "사진은 5MB 이하만 업로드할 수 있습니다."
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !ALLOWED_TYPES.contains(contentType)) {

            throw new IllegalArgumentException(
                    "JPG 또는 PNG 이미지만 업로드할 수 있습니다."
            );
        }
    }

    private String getExtension(String fileName) {

        if (fileName == null) {
            return "";
        }

        int index = fileName.lastIndexOf('.');

        if (index == -1) {
            return "";
        }

        return fileName.substring(index).toLowerCase();
    }

    public record StoredFile(
            String originalFileName,
            String storedFileName,
            String filePath,
            String contentType,
            long fileSize
    ) {
    }
}