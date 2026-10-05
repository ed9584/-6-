package com.example.school_complaint_backend.dto.response;

import com.example.school_complaint_backend.entity.ComplaintImage;

public class ComplaintImageResponse {

    private Long id;
    private String originalFileName;
    private String contentType;
    private long fileSize;

    public ComplaintImageResponse(ComplaintImage image) {
        this.id = image.getId();
        this.originalFileName = image.getOriginalFileName();
        this.contentType = image.getContentType();
        this.fileSize = image.getFileSize();
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getFileSize() {
        return fileSize;
    }
}