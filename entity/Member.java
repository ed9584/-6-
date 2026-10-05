

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 선언합니다.
@Table(name = "member") // 매핑할 데이터베이스의 실제 테이블 이름이 'member'임을 지정합니다.
@Getter @Setter // Lombok 라이브러리를 사용해 모든 필드의 Getter/Setter 메서드를 자동으로 생성합니다.
public class Member {

    @Id // 이 필드가 테이블의 기본키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략을 Auto Increment(자동 증가)로 설정합니다.
    @Column(name = "member_id") // 데이터베이스의 'member_id' 컬럼과 매핑합니다.
    private Long memberId;

    @Column(name = "student_id", nullable = false, unique = true, length = 20)
    // student_id 컬럼: null 불가(nullable=false), 중복 불가(unique=true), 최대 길이 20
    private String studentId;

    @Column(nullable = false, length = 255)
    // password 컬럼: null 불가, 최대 길이 255
    private String password;

    @Column(nullable = false, length = 50)
    // name 컬럼: null 불가, 최대 길이 50
    private String name;

    @Column(nullable = false, length = 20)
    // role 컬럼 (STUDENT 또는 ADMIN): null 불가, 최대 길이 20
    private String role;
}