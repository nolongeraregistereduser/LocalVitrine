package com.localvitrine.service.impl;

import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;
import com.localvitrine.entity.Project;
import com.localvitrine.entity.User;
import com.localvitrine.repository.ProjectRepository;
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

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
