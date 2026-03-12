package com.localvitrine.controller;

import com.localvitrine.dto.PublicLandingPageResponse;
import com.localvitrine.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicProjectController {

    private final ProjectService projectService;

    public PublicProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PublicLandingPageResponse> getPublicLandingPage(@PathVariable String slug) {
        return ResponseEntity.ok(projectService.getPublicLandingPage(slug));
    }
}
