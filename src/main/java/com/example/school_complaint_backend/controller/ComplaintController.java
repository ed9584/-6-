package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.dto.request.ComplaintCreateRequest;
import com.example.school_complaint_backend.dto.response.ComplaintImageResponse;
import com.example.school_complaint_backend.dto.response.ComplaintResponse;
import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.school_complaint_backend.dto.request.ComplaintUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // 민원 등록
    @PostMapping
    public ComplaintResponse createComplaint(
            @Valid @RequestBody ComplaintCreateRequest request,
            Authentication authentication
    ) {
        String studentId = authentication.getName();

        Complaint complaint = complaintService.createComplaint(
                request,
                studentId
        );

        return new ComplaintResponse(complaint);
    }

    // 민원 사진 업로드
    @PostMapping(
            value = "/{id}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public List<ComplaintImageResponse> uploadImages(
            @PathVariable("id") Long id,
            @RequestParam("images") List<MultipartFile> images,
            Authentication authentication
    ) {
        String studentId = authentication.getName();

        return complaintService
                .uploadImages(id, studentId, images)
                .stream()
                .map(ComplaintImageResponse::new)
                .toList();
    }

    // 전체 민원 조회
    @GetMapping
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAllComplaints()
                .stream()
                .map(ComplaintResponse::new)
                .toList();
    }

    // 민원 상세 조회
    @GetMapping("/{id}")
    public ComplaintResponse getComplaint(
            @PathVariable("id") Long id
    ) {
        Complaint complaint = complaintService.getComplaint(id);
        return new ComplaintResponse(complaint);
    }

    // 내 민원 조회
    @GetMapping("/my")
    public List<ComplaintResponse> getMyComplaints(
            Authentication authentication
    ) {
        String studentId = authentication.getName();

        return complaintService
                .getMyComplaints(studentId)
                .stream()
                .map(ComplaintResponse::new)
                .toList();
    }
        // 민원 수정
        @PutMapping("/{id}")
        public ComplaintResponse updateComplaint(
        @PathVariable("id") Long id,
        @Valid @RequestBody ComplaintUpdateRequest request,
        Authentication authentication
        ) {
        String studentId = authentication.getName();

        Complaint complaint = complaintService.updateComplaint(
            id,
            request,
            studentId
        );

        return new ComplaintResponse(complaint);
        }
        @DeleteMapping("/{id}")
        public void deleteComplaint(
        @PathVariable("id") Long id,
        Authentication authentication
        ) {
        String studentId = authentication.getName();

         complaintService.deleteComplaint(
            id,
            studentId
        );
        }
}       