
package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.service.SympathyService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class SympathyController {

    private final SympathyService sympathyService;

    public SympathyController(SympathyService sympathyService) {
        this.sympathyService = sympathyService;
    }

    // 공감 등록
    @PostMapping("/{id}/sympathies")
    public ResponseEntity<Map<String, String>> addSympathy(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String studentId = authentication.getName();

        sympathyService.addSympathy(id, studentId);

        return ResponseEntity.ok(
                Map.of("message", "공감이 등록되었습니다.")
        );
    }

    // 공감 취소
    @DeleteMapping("/{id}/sympathies")
    public ResponseEntity<Map<String, String>> cancelSympathy(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String studentId = authentication.getName();

        sympathyService.cancelSympathy(id, studentId);

        return ResponseEntity.ok(
                Map.of("message", "공감이 취소되었습니다.")
        );
    }

    // 공감 개수 조회
    @GetMapping("/{id}/sympathies/count")
    public ResponseEntity<Map<String, Long>> getSympathyCount(
            @PathVariable Long id
    ) {
        long count = sympathyService.getSympathyCount(id);

        return ResponseEntity.ok(
                Map.of("count", count)
        );
    }
}
