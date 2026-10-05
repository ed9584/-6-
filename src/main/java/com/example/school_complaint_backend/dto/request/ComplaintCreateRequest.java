package com.example.school_complaint_backend.dto.request;

import com.example.school_complaint_backend.entity.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComplaintCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ComplaintCategory category;
    
    @NotBlank
    private String buildingNumber;

    @NotBlank
    private String buildingName;

    @NotBlank
    private String roomNumber;

    public String getTitle() {
        return title;
    }
    
    public String getBuildingNumber() {
    return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
    }

    public String getBuildingName() {
    return buildingName;
    }

    public void setBuildingName(String buildingName) {
    this.buildingName = buildingName;
    }

    public String getRoomNumber() {
    return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ComplaintCategory getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategory category) {
        this.category = category;
    }
}