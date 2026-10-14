package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "items") // 매핑할 데이터베이스 테이블 이름 지정
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 전략 매핑
    @Column(name = "item_id")
    private Long itemId; // 비품 고유 식별자

    @Column(name = "location_id", nullable = false)
    private Long locationId; // 비품이 위치한 장소 고유 식별자

    @Column(name = "item_category", length = 50, nullable = false)
    private String itemCategory; // 비품 카테고리 (예: 모니터, 책상)

    @Column(name = "installed_at")
    private LocalDate installedAt; // 설치/교체 일자 (노후화 예측 계산용)
}