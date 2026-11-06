package com.example.school_complaint_backend.service;

import com.example.school_complaint_backend.entity.Member;
import com.example.school_complaint_backend.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(
            MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String studentId)
            throws UsernameNotFoundException {

        Member member = memberRepository
                .findByStudentId(studentId)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "회원을 찾을 수 없습니다."
                        )
                );

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(member.getStudentId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}