package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    // 작성해주신 SQL 쿼리를 Spring Data JPA의 @Query(Native Query)로 구현
    @Query(value = "SELECT c.* " +
            "FROM complaint c " +
            "JOIN member m ON c.member_id = m.member_id " +
            "WHERE NOT EXISTS (SELECT 1 FROM answer a WHERE a.complaint_id = c.complaint_id) " +
            "ORDER BY c.is_urgent DESC, c.like_count DESC, c.created_at DESC",
            nativeQuery = true)
    List<Complaint> findPriorityComplaintsWithoutAnswer();
}