package com.localvitrine.controller;

import com.localvitrine.dto.AiGeneratedContentResponse;
import com.localvitrine.service.AiContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiContentController {

    private final AiContentService aiContentService;

    public AiContentController(AiContentService aiContentService) {
        this.aiContentService = aiContentService;
    }

    @PostMapping("/generate/{projectId}")
    public ResponseEntity<AiGeneratedContentResponse> generate(@PathVariable Long projectId) {
        return ResponseEntity.ok(aiContentService.generateForProject(projectId));
    }
}
