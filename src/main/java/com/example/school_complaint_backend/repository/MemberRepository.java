package com.example.school_complaint_backend.repository;

import com.example.school_complaint_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Integer> {

    Optional<Member> findByStudentId(String studentId);
}