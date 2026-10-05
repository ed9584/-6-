package com.example.school_complaint_backend.dto.request;

import com.example.school_complaint_backend.entity.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComplaintUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ComplaintCategory category;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ComplaintCategory getCategory() {
        return category;
    }
}