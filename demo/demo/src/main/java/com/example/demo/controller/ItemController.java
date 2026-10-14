package com.example.demo.controller;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items") // 기본 엔드포인트 URL 설정
public class ItemController {

    private final ItemRepository itemRepository;

    // 생성자 주입(Constructor Injection)을 통한 의존성 주입
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 1. 전체 비품 목록 조회 (GET: /api/items)
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // 2. 특정 비품 단건 조회 (GET: /api/items/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. 비품 정보 등록 (POST: /api/items)
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    // 4. 비품 정보 삭제 (DELETE: /api/items/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.ok().build(); // 삭제 성공 시 200 OK
        }
        return ResponseEntity.notFound().build(); // 대상이 없으면 404 Not Found
    }
}