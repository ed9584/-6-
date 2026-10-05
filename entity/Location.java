package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 나타냅니다.
@Table(
        name = "locations", // 매핑할 실제 MySQL 테이블 이름
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_locations_building_room", columnNames = {"building_name", "room_number"})
        }
)
@Getter // 롬복: 모든 필드의 Getter를 자동으로 생성합니다.
@Setter // 롬복: 모든 필드의 Setter를 자동으로 생성합니다.
@NoArgsConstructor // 롬복: 파라미터가 없는 기본 생성자를 자동으로 생성합니다.
public class Location {

    @Id // 이 필드가 테이블의 Primary Key(기본키)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략: AUTO_INCREMENT (DB가 자동으로 번호 생성)
    @Column(name = "location_id") // 실제 DB 컬럼명 매핑
    private Long locationId;

    @Column(name = "building_name", nullable = false, length = 50) // NOT NULL, 최대 길이 50
    private String buildingName; // 건물명 (예: 정보공학관)

    @Column(name = "room_number", nullable = false, length = 20) // NOT NULL, 최대 길이 20
    private String roomNumber; // 호수 (예: 501호)
}