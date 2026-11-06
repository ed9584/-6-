
package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.dto.response.ComplaintResponse;
import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.entity.ComplaintStatus;
import com.example.school_complaint_backend.service.ComplaintService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/complaints")
public class AdminComplaintController {

    private final ComplaintService complaintService;

    public AdminComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // 관리자 전체 민원 조회
    @GetMapping
    public List<ComplaintResponse> getAllComplaints() {

        return complaintService.getAllComplaints()
                .stream()
                .map(ComplaintResponse::new)
                .toList();
    }

    // 관리자 민원 삭제
    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable("id") Long id) {

        complaintService.adminDeleteComplaint(id);
    }

    // 관리자 민원 상태 변경
    @PatchMapping("/{id}/status")
    public ComplaintResponse changeStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") ComplaintStatus status
    ) {

        Complaint complaint =
                complaintService.adminChangeStatus(id, status);

        return new ComplaintResponse(complaint);
    }
}
