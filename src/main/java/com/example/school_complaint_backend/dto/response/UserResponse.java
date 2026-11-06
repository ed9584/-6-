package com.example.school_complaint_backend.dto.response;

import com.example.school_complaint_backend.entity.Member;
import com.example.school_complaint_backend.entity.Role;

public class UserResponse {

    private Integer id;
    private String studentId;
    private String name;
    private Role role;

    public UserResponse(Member user) {
        this.id = user.getMemberId();
        this.studentId = user.getStudentId();
        this.name = user.getName();
        this.role = user.getRole();
    }

    public Integer getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
