
package com.example.school_complaint_backend.service;

import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.entity.Member;
import com.example.school_complaint_backend.entity.Sympathy;
import com.example.school_complaint_backend.repository.ComplaintRepository;
import com.example.school_complaint_backend.repository.MemberRepository;
import com.example.school_complaint_backend.repository.SympathyRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SympathyService {

    private final SympathyRepository sympathyRepository;
    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;

    public SympathyService(
            SympathyRepository sympathyRepository,
            ComplaintRepository complaintRepository,
            MemberRepository memberRepository
    ) {
        this.sympathyRepository = sympathyRepository;
        this.complaintRepository = complaintRepository;
        this.memberRepository = memberRepository;
    }

    // 공감 등록
    @Transactional
    public void addSympathy(Long complaintId, String studentId) {

        // 공감할 민원 조회
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "민원을 찾을 수 없습니다."
                ));

        // 로그인한 회원 조회
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."
                ));

        // 이미 공감했는지 확인
        boolean alreadySympathized =
                sympathyRepository.existsByComplaint_IdAndMember_MemberId(
                        complaintId,
                        member.getMemberId()
                );

        if (alreadySympathized) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "이미 공감한 민원입니다."
            );
        }

        // 공감 저장
        Sympathy sympathy = new Sympathy(complaint, member);
        sympathyRepository.save(sympathy);
    }

    // 공감 취소
    @Transactional
    public void cancelSympathy(Long complaintId, String studentId) {

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."
                ));

        boolean exists =
                sympathyRepository.existsByComplaint_IdAndMember_MemberId(
                        complaintId,
                        member.getMemberId()
                );

        if (!exists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "공감 기록이 없습니다."
            );
        }

        sympathyRepository.deleteByComplaint_IdAndMember_MemberId(
                complaintId,
                member.getMemberId()
        );
    }

    // 민원별 공감 개수 조회
    @Transactional(readOnly = true)
    public long getSympathyCount(Long complaintId) {

        if (!complaintRepository.existsById(complaintId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "민원을 찾을 수 없습니다."
            );
        }

        return sympathyRepository.countByComplaint_Id(complaintId);
    }
}
