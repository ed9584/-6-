package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.dto.request.SignupRequest;
import com.example.school_complaint_backend.dto.response.UserResponse;
import com.example.school_complaint_backend.entity.User;
import com.example.school_complaint_backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.example.school_complaint_backend.dto.request.LoginRequest;
import com.example.school_complaint_backend.dto.response.LoginResponse;
import com.example.school_complaint_backend.security.JwtService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
        UserService userService,
        JwtService jwtService
    ) {
    this.userService = userService;
    this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody SignupRequest request) {
        User user = userService.signup(request);
        return new UserResponse(user);
    }
    @PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest request) {

    User user = userService.login(
            request.getUsername(),
            request.getPassword()
    );

    String token = jwtService.createToken(user);

    return new LoginResponse(
            user.getUsername(),
            user.getRole().name(),
            token
    );
    }
}