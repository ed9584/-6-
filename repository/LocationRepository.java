package com.example.demo.repository;

import com.example.demo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JpaRepository<다룰 엔티티, PK의 데이터 타입>을 상속받으면 기본적인 CRUD(생성, 조회, 수정, 삭제) 메서드가 자동 제공됩니다.
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // 기본적인 기능 외에 커스텀 조회가 필요하면 여기에 메서드를 추가할 수 있습니다.
}
