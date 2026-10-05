package com.example.school_complaint_backend.dto.response;

import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.entity.ComplaintCategory;
import com.example.school_complaint_backend.entity.ComplaintStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintResponse {

    private Long id;
    private String title;
    private String content;
    private ComplaintCategory category;
    private ComplaintStatus status;
    private String studentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String buildingNumber;
    private String buildingName;
    private String roomNumber;
    private List<ComplaintImageResponse> images;
    private List<String> tags;

    public ComplaintResponse(Complaint complaint) {
        this.id = complaint.getId();
        this.title = complaint.getTitle();
        this.content = complaint.getContent();
        this.category = complaint.getCategory();
        this.status = complaint.getStatus();

        this.studentId = complaint.getUser().getStudentId();

        this.createdAt = complaint.getCreatedAt();
        this.updatedAt = complaint.getUpdatedAt();

        this.buildingNumber = complaint.getBuildingNumber();
        this.buildingName = complaint.getBuildingName();
        this.roomNumber = complaint.getRoomNumber();

        this.images = complaint.getImages()
                .stream()
                .map(ComplaintImageResponse::new)
                .collect(Collectors.toList());

        this.tags = complaint.getTags()
                .stream()
                .map(complaintTag -> complaintTag.getTag().getName())
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ComplaintCategory getCategory() {
        return category;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public String getStudentId() {
        return studentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public List<ComplaintImageResponse> getImages() {
        return images;
    }

    public List<String> getTags() {
        return tags;
    }
}

