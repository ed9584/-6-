
package com.example.school_complaint_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "sympathy",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_sympathy",
            columnNames = {"complaint_id", "member_id"}
        )
    }
)
public class Sympathy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sympathy_id")
    private Integer sympathyId;

    // 공감 대상 민원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    // 공감한 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Sympathy() {
    }

    public Sympathy(Complaint complaint, Member member) {
        this.complaint = complaint;
        this.member = member;
    }

    public Integer getSympathyId() {
        return sympathyId;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public Member getMember() {
        return member;
    }
}
