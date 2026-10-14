package com.example.demo.controller;

import com.example.demo.entity.Complaint;
import com.example.demo.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    // 직접 생성자 주입
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // 1. 전체 민원 목록 조회 API (GET: /api/complaints)
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    // 2. 민원 등록 API (POST: /api/complaints)
    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    // 3. 우선순위 정렬된 미답변 민원 조회 API (GET: /api/complaints/priority)
    @GetMapping("/priority")
    public List<Complaint> getPriorityComplaints() {
        return complaintService.getPriorityComplaints();
    }
}