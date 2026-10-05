package com.example.school_complaint_backend.repository;

import com.example.school_complaint_backend.entity.ComplaintImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintImageRepository extends JpaRepository<ComplaintImage, Long> {
}