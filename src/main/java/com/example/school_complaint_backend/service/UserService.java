package com.example.school_complaint_backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.school_complaint_backend.dto.request.SignupRequest;
import com.example.school_complaint_backend.entity.Role;
import com.example.school_complaint_backend.entity.User;
import com.example.school_complaint_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.school_complaint_backend.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    }

    public User signup(SignupRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                encodedPassword,
                request.getName(),
                Role.USER
        );

        return userRepository.save(user);
    }
    public User login(String username, String password) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
    );

    return userRepository.findByUsername(username)
            .orElseThrow();
    }
}