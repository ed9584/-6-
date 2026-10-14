package com.example.demo.service;

import com.example.demo.entity.Complaint;
import com.example.demo.repository.ComplaintRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링이 서비스 빈으로 등록
@Transactional(readOnly = true) // 기본 읽기 전용 트랜잭션
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    // 직접 생성자 주입
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    // 1. 전체 민원 조회
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // 2. 민원 등록
    @Transactional // 데이터 변경 작업이므로 트랜잭션 쓰기 허용
    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // 3. 우선순위 정렬된 미답변 민원 조회 비즈니스 로직
    public List<Complaint> getPriorityComplaints() {
        return complaintRepository.findPriorityComplaintsWithoutAnswer();
    }
}