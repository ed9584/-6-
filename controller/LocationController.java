package com.example.demo.controller;

import com.example.demo.entity.Location;
import com.example.demo.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 나타냅니다. (결과가 주로 JSON으로 반환됨)
@RequestMapping("/api/locations") // 이 컨트롤러의 모든 API 주소 앞에 공통으로 붙는 기본 주소입니다.
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // 1. 장소 목록 조회 API
    // 최종 요청 주소: GET http://localhost:8080/api/locations
    @GetMapping
    public List<Location> getLocations() {
        return locationService.getAllLocations(); // 서비스 계층을 통해 전체 목록을 가져와 반환합니다.
    }

    // 2. 장소 등록 API
    // 최종 요청 주소: POST http://localhost:8080/api/locations
    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        // @RequestBody: HTTP 요청의 본문에 담긴 JSON 데이터를 Location 객체로 변환해 줍니다.
        return locationService.createLocation(location);
    }
}