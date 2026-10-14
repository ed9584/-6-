package com.example.demo.controller;

import com.example.demo.entity.Answer;
import com.example.demo.repository.AnswerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerRepository answerRepository;

    // 직접 생성자를 통한 의존성 주입
    public AnswerController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    // 1. 관리자 답변 등록 (POST: /api/answers)
    @PostMapping
    public Answer createAnswer(@RequestBody Answer answer) {
        return answerRepository.save(answer);
    }

    // 2. 특정 민원의 답변 조회 (GET: /api/answers/complaint/{complaintId})
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<Answer> getAnswerByComplaintId(@PathVariable Integer complaintId) {
        return answerRepository.findByComplaintId(complaintId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
