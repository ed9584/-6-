package com.example.school_complaint_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "complaint_tags",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"complaint_id", "tag_id"})
        }
)
public class ComplaintTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    protected ComplaintTag() {
    }

    public ComplaintTag(Complaint complaint, Tag tag) {
        this.complaint = complaint;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public Tag getTag() {
        return tag;
    }
}