package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 선언합니다. (JSON 형태로 데이터를 반환)
@RequestMapping("/api/members") // 이 컨트롤러의 모든 API 주소는 기본적으로 '/api/members'로 시작합니다.
public class MemberController {

    @Autowired // 스프링이 미리 만들어 둔 MemberRepository 객체를 자동으로 주입(DI)해 줍니다.
    private MemberRepository memberRepository;

    // ==========================================
    // 1. 회원 저장 (Create) - POST 방식
    // 요청 주소: http://localhost:8080/api/members
    // ==========================================
    @PostMapping
    public Member createMember(@RequestBody Member member) {
        // @RequestBody: 클라이언트가 보낸 JSON 데이터를 자바의 Member 객체로 변환해 줍니다.
        // memberRepository.save(): 전달받은 회원 정보를 데이터베이스에 INSERT 합니다.
        return memberRepository.save(member);
    }

    // ==========================================
    // 2-1. 전체 회원 조회 (Read - All) - GET 방식
    // 요청 주소: http://localhost:8080/api/members
    // ==========================================
    @GetMapping
    public List<Member> getAllMembers() {
        // memberRepository.findAll(): 데이터베이스에 저장된 모든 회원 목록을 조회하여 리스트로 반환합니다.
        return memberRepository.findAll();
    }

    // ==========================================
    // 2-2. 단건 회원 조회 (Read - One) - GET 방식
    // 요청 주소: http://localhost:8080/api/members/{id} (예: /api/members/1)
    // ==========================================
    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        // @PathVariable: 주소창의 {id} 값을 가져와서 메서드의 파라미터(Long id)에 넣어줍니다.
        // memberRepository.findById(): 해당 ID를 가진 회원을 찾습니다. (없을 수도 있으므로 Optional로 감싸져서 나옴)
        Optional<Member> member = memberRepository.findById(id);

        return member.orElse(null); // 회원이 존재하면 객체를 반환하고, 없으면 null을 반환합니다.
    }

    // ==========================================
    // 3. 회원 삭제 (Delete) - DELETE 방식
    // 요청 주소: http://localhost:8080/api/members/{id} (예: /api/members/1)
    // ==========================================
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        // memberRepository.existsById(): 해당 ID의 회원이 데이터베이스에 존재하는지 확인합니다.
        if (memberRepository.existsById(id)) {
            // 존재한다면 해당 ID의 데이터를 DELETE 합니다.
            memberRepository.deleteById(id);
            return "회원 삭제 성공 (ID: " + id + ")";
        } else {
            return "존재하지 않는 회원입니다.";
        }
    }
}