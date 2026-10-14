package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer answerId; // 답변 고유 번호

    @Column(name = "complaint_id")
    private Integer complaintId; // 답변 대상 민원 번호

    @Column(name = "admin_id")
    private Integer adminId; // 답변을 작성한 관리자 회원 번호

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 답변 내용 (TEXT 타입)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 답변 등록일시 (자동 생성)
}
