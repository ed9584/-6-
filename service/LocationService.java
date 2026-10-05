package com.example.demo.service;

import com.example.demo.entity.Location;
import com.example.demo.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링이 이 클래스를 비즈니스 로직을 처리하는 서비스 빈(Bean)으로 등록합니다.
@RequiredArgsConstructor // final이 붙은 필드를 매개변수로 하는 생성자를 자동 생성하여 의존성을 주입받습니다.
@Transactional(readOnly = true) // 데이터 조회 작업이 많으므로 기본적으로 읽기 전용 트랜잭션 설정
public class LocationService {

    private final LocationRepository locationRepository;

    // 장소 전체 목록 조회 메서드
    public List<Location> getAllLocations() {
        return locationRepository.findAll(); // DB에서 모든 location 데이터를 조회해 반환
    }

    // 새로운 장소를 등록하는 메서드
    @Transactional // 데이터를 변경(저장)하는 작업이므로 읽기 전용을 해제하고 트랜잭션을 적용합니다.
    public Location createLocation(Location location) {
        return locationRepository.save(location); // DB에 데이터를 저장합니다.
    }
}