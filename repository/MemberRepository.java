package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 스프링이 이 인터페이스를 데이터 저장소(Repository)로 인식하도록 등록합니다.
public interface MemberRepository extends JpaRepository<Member, Long> {
    /*
     * JpaRepository<사용할엔티티클래스, 기본키타입(Long)>을 상속받으면
     * save() (저장/수정), findById() (조회), findAll() (전체조회), deleteById() (삭제) 등
     * 기본적인 데이터베이스 조작 메서드들이 자동으로 제공됩니다!
     */
}