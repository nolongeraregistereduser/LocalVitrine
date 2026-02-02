package com.localvitrine.controller;

import com.localvitrine.dto.BusinessProfileRequest;
import com.localvitrine.dto.BusinessProfileResponse;
import com.localvitrine.service.BusinessProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/business-profile")
public class BusinessProfileController {

    private final BusinessProfileService businessProfileService;

    public BusinessProfileController(BusinessProfileService businessProfileService) {
        this.businessProfileService = businessProfileService;
    }

    @PostMapping
    public ResponseEntity<BusinessProfileResponse> create(
            @PathVariable Long projectId,
            @Valid @RequestBody BusinessProfileRequest request) {
        BusinessProfileResponse body = businessProfileService.createBusinessProfile(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<BusinessProfileResponse> get(@PathVariable Long projectId) {
        return ResponseEntity.ok(businessProfileService.getBusinessProfile(projectId));
    }

    @PutMapping
    public ResponseEntity<BusinessProfileResponse> update(
            @PathVariable Long projectId,
            @Valid @RequestBody BusinessProfileRequest request) {
        return ResponseEntity.ok(businessProfileService.updateBusinessProfile(projectId, request));
    }
}
