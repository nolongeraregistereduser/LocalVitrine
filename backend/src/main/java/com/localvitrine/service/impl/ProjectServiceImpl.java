package com.localvitrine.service.impl;

import com.localvitrine.dto.ProjectContentRequest;
import com.localvitrine.dto.ProjectContentResponse;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;
import com.localvitrine.entity.Project;
import com.localvitrine.entity.Template;
import com.localvitrine.entity.User;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.TemplateRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            TemplateRepository templateRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = requireCurrentUser();
        Project project = Project.builder()
                .title(request.title().trim())
                .status(request.status())
                .publicUrl(normalizePublicUrl(request.publicUrl()))
                .owner(owner)
                .build();
        projectRepository.save(project);
        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getMyProjects() {
        User owner = requireCurrentUser();
        return projectRepository.findByOwnerIdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getMyProjectById(Long id) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        project.setTitle(request.title().trim());
        project.setStatus(request.status());
        project.setPublicUrl(normalizePublicUrl(request.publicUrl()));
        projectRepository.save(project);
        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectResponse assignTemplateToProject(Long projectId, Long templateId) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(projectId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        Template template = templateRepository.findByIdAndIsActiveTrue(templateId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Template not found"));
        project.setTemplate(template);
        projectRepository.save(project);
        return ProjectResponse.fromEntity(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectContentResponse getProjectContent(Long id) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        return ProjectContentResponse.fromEntity(project);
    }

    @Override
    @Transactional
    public ProjectContentResponse updateProjectContent(Long id, ProjectContentRequest request) {
        User owner = requireCurrentUser();
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
        project.setHtmlContent(request.htmlContent());
        project.setCssContent(request.cssContent());
        projectRepository.save(project);
        return ProjectContentResponse.fromEntity(project);
    }

    private User requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
    }

    private static String normalizePublicUrl(String publicUrl) {
        if (publicUrl == null) {
            return null;
        }
        String trimmed = publicUrl.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
