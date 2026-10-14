package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime; //작성일시

// 고장 원인을 정의하는 Enum (노후화, 파손, 초기불량, 미상)
enum FailureReason {
    AGING,    // 노후화
    DAMAGE,   // 파손
    DEFECT,   // 초기불량
    UNKNOWN   // 미상
}

@Entity
@Table(name = "complaint") // 매핑할 데이터베이스 테이블 이름 지정
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // INT AUTO_INCREMENT 매핑
    @Column(name = "complaint_id")
    private Integer complaintId; // 민원 고유 번호

    @Column(name = "location_id", nullable = false)
    private Long locationId; // 고장 장소 ID

    @Column(name = "item_id")
    private Long itemId; // 고장 비품 ID (NULL 허용)

    @Column(name = "title", length = 100, nullable = false)
    private String title; // 민원 제목

    @Column(name = "content", length = 300, nullable = false)
    private String content; // 상세 내용 (300자 제한)

    @Column(name = "image_url")
    private String imageUrl; // 첨부 사진 파일 경로 (NULL 허용)

    @Enumerated(EnumType.STRING) // DB의 ENUM 문자열 값을 자바 Enum 객체로 매핑
    @Column(name = "failure_reason", nullable = false)
    private FailureReason failureReason; // 고장 원인

    @Column(name = "status", length = 20)
    private String status = "접수대기"; // 처리 상태 (기본값: 접수대기)

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 공감 수 (기본값: 0)

    @Column(name = "is_urgent")
    private Boolean isUrgent = false; // 긴급 여부 (기본값: false)

    @Column(name = "member_id")
    private Integer memberId; // 작성한 학생의 회원 번호 (외래키 매핑용 컬럼)

    @CreationTimestamp // 엔티티 생성 시 현재 시간이 자동 입력됨 (CURRENT_TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 작성일시
}
