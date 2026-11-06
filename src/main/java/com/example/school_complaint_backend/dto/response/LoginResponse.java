package com.example.school_complaint_backend.dto.response;

public class LoginResponse {

    private String studentId;
    private String token;

    public LoginResponse(String studentId, String token) {
        this.studentId = studentId;
        this.token = token;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getToken() {
        return token;
    }
}