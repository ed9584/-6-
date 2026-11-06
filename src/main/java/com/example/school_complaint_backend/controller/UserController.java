package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.dto.request.LoginRequest;
import com.example.school_complaint_backend.dto.request.SignupRequest;
import com.example.school_complaint_backend.dto.response.LoginResponse;
import com.example.school_complaint_backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api/users")
public class UserController {
        @PostConstruct
public void init() {
    System.out.println("UserController 실행됨!");
}
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public void signup(
            @RequestBody SignupRequest request
    ) {
        userService.signup(
                request.getStudentId(),
                request.getPassword(),
                request.getName()
        );
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) 
    {
        System.out.println("로그인 요청 도착!");
        String token = userService.login(
                request.getStudentId(),
                request.getPassword()
        );

        return new LoginResponse(
                request.getStudentId(),
                token
        );
    }
}