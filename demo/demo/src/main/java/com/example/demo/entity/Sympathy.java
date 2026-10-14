package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "sympathy",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_sympathy", columnNames = {"complaint_id", "member_id"})
        }
) // 동일 유저의 중복 공감을 방지하는 유니크 제약 설정
@Getter
@Setter
@NoArgsConstructor
public class Sympathy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sympathy_id")
    private Integer sympathyId; // 공감 고유 번호

    @Column(name = "complaint_id", nullable = false)
    private Integer complaintId; // 공감을 받은 민원 번호

    @Column(name = "member_id", nullable = false)
    private Integer memberId; // 공감을 누른 회원 번호
}