package com.example.school_complaint_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.school_complaint_backend.dto.request.ComplaintCreateRequest;
import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.entity.Member;
import com.example.school_complaint_backend.entity.ComplaintImage;
import com.example.school_complaint_backend.repository.ComplaintRepository;
import com.example.school_complaint_backend.repository.MemberRepository;
import com.example.school_complaint_backend.repository.ComplaintImageRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.school_complaint_backend.dto.request.ComplaintUpdateRequest;


import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final MemberRepository userRepository;
    private final ComplaintImageRepository complaintImageRepository;
    private final FileStorageService fileStorageService;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            MemberRepository userRepository,
            ComplaintImageRepository complaintImageRepository,
            FileStorageService fileStorageService
    ) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.complaintImageRepository = complaintImageRepository;
        this.fileStorageService = fileStorageService;
    }

    // 민원 등록
    public Complaint createComplaint(
            ComplaintCreateRequest request,
            String studentId
    ) {
        Member user = userRepository.findByStudentId(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        Complaint complaint = new Complaint(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getBuildingNumber(),
                request.getBuildingName(),
                request.getRoomNumber(),
                user
        );

        return complaintRepository.save(complaint);
    }

    // 전체 민원 조회
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    // 민원 상세 조회
     public Complaint getComplaint(Long id) {
         return complaintRepository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "민원을 찾을 수 없습니다."
                    )
            );
        }
    // 내 민원 조회
    public List<Complaint> getMyComplaints(String studentId) {
        return complaintRepository
                .findByUser_StudentIdOrderByCreatedAtDesc(studentId);
    }
        // 민원 수정
        @Transactional
        public Complaint updateComplaint(
                Long complaintId,
                ComplaintUpdateRequest request,
                String studentId
        ) {
        Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() ->
                    new IllegalArgumentException("민원을 찾을 수 없습니다.")
            );

        if (!complaint.getUser().getStudentId().equals(studentId)) {
                throw new AccessDeniedException(
                        "본인이 작성한 민원만 수정할 수 있습니다."
        );
    }

    complaint.update(
            request.getTitle(),
            request.getContent(),
            request.getCategory()
    );

    return complaint;
}
@Transactional
public void deleteComplaint(
        Long complaintId,
        String studentId
) {
    Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() ->
                    new IllegalArgumentException("민원을 찾을 수 없습니다.")
            );

    if (!complaint.getUser().getStudentId().equals(studentId)) {
        throw new AccessDeniedException(
                "본인이 작성한 민원만 삭제할 수 있습니다."
        );
    }

    complaintRepository.delete(complaint);
}
    // 민원 사진 업로드
    @Transactional
    public List<ComplaintImage> uploadImages(
            Long complaintId,
            String studentId,
            List<MultipartFile> files
    ) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "민원을 찾을 수 없습니다."
                        )
                );

        // 본인이 작성한 민원인지 확인
        if (!complaint.getUser().getStudentId().equals(studentId)) {
            throw new AccessDeniedException(
                    "본인이 작성한 민원에만 사진을 추가할 수 있습니다."
            );
        }

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException(
                    "업로드할 사진이 없습니다."
            );
        }

        if (files.size() > 5) {
            throw new IllegalArgumentException(
                    "사진은 최대 5장까지 업로드할 수 있습니다."
            );
        }

        List<ComplaintImage> images = new ArrayList<>();

        for (MultipartFile file : files) {

            FileStorageService.StoredFile storedFile =
                    fileStorageService.store(file, complaintId);

            ComplaintImage image = new ComplaintImage(
                    storedFile.originalFileName(),
                    storedFile.storedFileName(),
                    storedFile.filePath(),
                    storedFile.contentType(),
                    storedFile.fileSize(),
                    complaint
            );

            images.add(
                    complaintImageRepository.save(image)
            );
        }

        return images;
    }
}

