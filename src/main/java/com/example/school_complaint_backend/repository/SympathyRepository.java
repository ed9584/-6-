
package com.example.school_complaint_backend.repository;

import com.example.school_complaint_backend.entity.Sympathy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SympathyRepository
        extends JpaRepository<Sympathy, Integer> {

    // 특정 회원이 특정 민원에 공감했는지 확인
    boolean existsByComplaint_IdAndMember_MemberId(
            Long complaintId,
            Integer memberId
    );

    // 특정 민원의 공감 개수 조회
    long countByComplaint_Id(Long complaintId);

    // 특정 회원의 특정 민원 공감 취소
    void deleteByComplaint_IdAndMember_MemberId(
            Long complaintId,
            Integer memberId
    );

    // 특정 민원의 공감 전체 삭제
    void deleteByComplaint_Id(Long complaintId);
}
