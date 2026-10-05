package com.example.school_complaint_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintCategory category;
    @Column(nullable = false)
    private String buildingNumber;

    @Column(nullable = false)
    private String buildingName;

    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ComplaintImage> images = new ArrayList<>();
    @OneToMany(
        mappedBy = "complaint",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
private List<ComplaintTag> tags = new ArrayList<>();

    protected Complaint() {
    }

    public Complaint(
        String title,
        String content,
        ComplaintCategory category,
        String buildingNumber,
        String buildingName,
        String roomNumber,
        Member user
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.buildingNumber = buildingNumber;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
        this.status = ComplaintStatus.RECEIVED;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Member getUser() {
        return user;
    }

    public List<ComplaintImage> getImages() {
        return images;
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

    public void changeStatus(ComplaintStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content, ComplaintCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public void addImage(ComplaintImage image) {
        images.add(image);
    }
    public List<ComplaintTag> getTags() {
    return tags;
    }
    
}