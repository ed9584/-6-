package com.example.demo.repository;

import com.example.demo.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    // 특정 민원에 달린 관리자 답변을 조회하는 메서드
    Optional<Answer> findByComplaintId(Integer complaintId);
}