package com.example.demo.repository;

import com.example.demo.entity.Sympathy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SympathyRepository extends JpaRepository<Sympathy, Integer> {

    // 특정 민원에 대해 특정 유저가 이미 공감을 눌렀는지 확인하는 메서드
    Optional<Sympathy> findByComplaintIdAndMemberId(Integer complaintId, Integer memberId);

    // 특정 민원의 총 공감 수를 카운트하는 메서드
    long countByComplaintId(Integer complaintId);
}