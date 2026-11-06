package com.example.school_complaint_backend.controller;

import com.example.school_complaint_backend.dto.request.TagRequest;
import com.example.school_complaint_backend.entity.ComplaintTag;
import com.example.school_complaint_backend.service.TagService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/{id}/tags")
    public void addTag(
            @PathVariable("id") Long complaintId,
            @Valid @RequestBody TagRequest request,
            Authentication authentication
    ) {

        tagService.addTag(
                complaintId,
                request.getName()
        );
    }

    @GetMapping("/{id}/tags")
    public List<String> getTags(
            @PathVariable("id") Long complaintId
    ) {

        return tagService.getTags(complaintId)
                .stream()
                .map(ComplaintTag::getTag)
                .map(tag -> tag.getName())
                .toList();
    }
}