package com.localvitrine.controller;

import com.localvitrine.dto.ProjectContentRequest;
import com.localvitrine.dto.ProjectContentResponse;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;
import com.localvitrine.dto.PublishProjectRequest;
import com.localvitrine.dto.PublishedProjectResponse;
import com.localvitrine.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        ProjectResponse body = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> list() {
        return ResponseEntity.ok(projectService.getMyProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getMyProjectById(id));
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<ProjectContentResponse> getContent(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectContent(id));
    }

    @PutMapping("/{projectId}/template/{templateId}")
    public ResponseEntity<ProjectResponse> assignTemplate(
            @PathVariable Long projectId,
            @PathVariable Long templateId) {
        return ResponseEntity.ok(projectService.assignTemplateToProject(projectId, templateId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @PutMapping("/{id}/content")
    public ResponseEntity<ProjectContentResponse> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ProjectContentRequest request) {
        return ResponseEntity.ok(projectService.updateProjectContent(id, request));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<PublishedProjectResponse> publish(
            @PathVariable Long id,
            @RequestBody(required = false) PublishProjectRequest request) {
        String requestedSlug = request != null ? request.slug() : null;
        return ResponseEntity.ok(projectService.publishProject(id, requestedSlug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
