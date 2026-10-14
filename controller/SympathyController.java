package com.example.demo.controller;

import com.example.demo.entity.Sympathy;
import com.example.demo.repository.SympathyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sympathies")
public class SympathyController {

    private final SympathyRepository sympathyRepository;

    // 직접 생성자를 통한 의존성 주입
    public SympathyController(SympathyRepository sympathyRepository) {
        this.sympathyRepository = sympathyRepository;
    }

    // 1. 공감 누르기 (POST: /api/sympathies)
    @PostMapping
    public Sympathy createSympathy(@RequestBody Sympathy sympathy) {
        return sympathyRepository.save(sympathy);
    }

    // 2. 공감 취소하기 (DELETE: /api/sympathies/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSympathy(@PathVariable Integer id) {
        if (sympathyRepository.existsById(id)) {
            sympathyRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}