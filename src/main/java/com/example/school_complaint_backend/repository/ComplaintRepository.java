package com.example.school_complaint_backend.repository;

import com.example.school_complaint_backend.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // 전체 민원: 최신순
    List<Complaint> findAllByOrderByCreatedAtDesc();

    // 특정 사용자의 민원: 최신순
    List<Complaint> findByUser_StudentIdOrderByCreatedAtDesc(String studentId);
}

