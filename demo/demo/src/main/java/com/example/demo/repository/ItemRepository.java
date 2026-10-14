package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 특정 장소(location_id)에 설치된 비품 목록을 조회하는 커스텀 쿼리 메서드
    List<Item> findByLocationId(Long locationId);

    // 특정 카테고리의 비품 목록을 조회하는 커스텀 쿼리 메서드
    List<Item> findByItemCategory(String itemCategory);
}