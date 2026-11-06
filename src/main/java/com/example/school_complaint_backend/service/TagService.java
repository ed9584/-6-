package com.example.school_complaint_backend.service;

import com.example.school_complaint_backend.entity.Complaint;
import com.example.school_complaint_backend.entity.ComplaintTag;
import com.example.school_complaint_backend.entity.Tag;
import com.example.school_complaint_backend.repository.ComplaintRepository;
import com.example.school_complaint_backend.repository.ComplaintTagRepository;
import com.example.school_complaint_backend.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ComplaintRepository complaintRepository;
    private final ComplaintTagRepository complaintTagRepository;

    public TagService(
            TagRepository tagRepository,
            ComplaintRepository complaintRepository,
            ComplaintTagRepository complaintTagRepository
    ) {
        this.tagRepository = tagRepository;
        this.complaintRepository = complaintRepository;
        this.complaintTagRepository = complaintTagRepository;
    }

    @Transactional
    public void addTag(Long complaintId, String tagName) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "민원을 찾을 수 없습니다."
                        )
                );

        String name = tagName.trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException(
                    "태그 이름을 입력해주세요."
            );
        }

        Tag tag = tagRepository.findByName(name)
                .orElseGet(() ->
                        tagRepository.save(new Tag(name))
                );

        if (complaintTagRepository
                .existsByComplaintIdAndTagId(
                        complaintId,
                        tag.getId()
                )) {
            throw new IllegalArgumentException(
                    "이미 등록된 태그입니다."
            );
        }

        ComplaintTag complaintTag =
                new ComplaintTag(complaint, tag);

        complaintTagRepository.save(complaintTag);
    }

    public List<ComplaintTag> getTags(Long complaintId) {

        return complaintTagRepository
                .findByComplaintId(complaintId);
    }
}