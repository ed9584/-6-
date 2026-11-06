package com.example.school_complaint_backend.repository;

import com.example.school_complaint_backend.entity.ComplaintTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintTagRepository
        extends JpaRepository<ComplaintTag, Long> {

    List<ComplaintTag> findByComplaintId(Long complaintId); //특정 민원에 붙은 태그

    boolean existsByComplaintIdAndTagId( 
            Long complaintId,
            Long tagId
    ); // 이미 같은 태그가 붙어 있는지 확인
}