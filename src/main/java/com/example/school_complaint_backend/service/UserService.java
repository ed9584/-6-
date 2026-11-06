package com.example.school_complaint_backend.service;

import com.example.school_complaint_backend.entity.Member;
import com.example.school_complaint_backend.entity.Role;
import com.example.school_complaint_backend.repository.MemberRepository;
import com.example.school_complaint_backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            MemberRepository memberRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    public void signup(
            String studentId,
            String password,
            String name
    ) {
        if (memberRepository.findByStudentId(studentId).isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 학번입니다."
            );
        }

        String encodedPassword =
                passwordEncoder.encode(password);

        Member member = new Member(
                studentId,
                encodedPassword,
                name,
                Role.STUDENT
        );

        memberRepository.save(member);
    }

    // 로그인
    public String login(String studentId, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        studentId,
                        password
                )
        );

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "회원을 찾을 수 없습니다."
                        )
                );

        return jwtService.createToken(member);
    }
}