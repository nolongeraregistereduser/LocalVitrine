package com.localvitrine.service.impl;

import com.localvitrine.dto.ProjectContentRequest;
import com.localvitrine.dto.ProjectContentResponse;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;
import com.localvitrine.entity.BusinessProfile;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
        if (shouldApplyTemplateStarter(project)) {
            project.setHtmlContent(applyPlaceholders(template.getStarterHtml(), project, template));
            project.setCssContent(applyPlaceholders(template.getStarterCss(), project, template));
            project.setContentCustomized(false);
        }
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
        boolean changed = !Objects.equals(project.getHtmlContent(), request.htmlContent())
                || !Objects.equals(project.getCssContent(), request.cssContent());
        project.setHtmlContent(request.htmlContent());
        project.setCssContent(request.cssContent());
        if (changed) {
            project.setContentCustomized(true);
        }
        projectRepository.save(project);
        return ProjectContentResponse.fromEntity(project);
    }

    private static boolean shouldApplyTemplateStarter(Project project) {
        return !Boolean.TRUE.equals(project.getContentCustomized());
    }

    private static String applyPlaceholders(String source, Project project, Template template) {
        if (source == null || source.isBlank()) {
            return source;
        }
        Map<String, String> values = buildPlaceholderValues(project, template);
        String resolved = source;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            resolved = resolved.replace(entry.getKey(), entry.getValue());
        }
        return resolved;
    }

    private static Map<String, String> buildPlaceholderValues(Project project, Template template) {
        BusinessProfile profile = project.getBusinessProfile();
        String businessName = safe(profile != null ? profile.getBusinessName() : null, project.getTitle());
        String email = safe(profile != null ? profile.getEmail() : null, "contact@business.com");
        String phone = safe(profile != null ? profile.getPhone() : null, "+212 600-000000");
        String address = safe(profile != null ? profile.getAddress() : null, "Your business address");
        String city = safe(profile != null ? profile.getCity() : null, "Your city");
        String description = safe(profile != null ? profile.getDescription() : null,
                "Describe your business, your services, and what makes you unique.");
        String ctaPrimary = toCtaLabel(profile);
        String activityType = template.getActivityType() != null
                ? template.getActivityType().name().toLowerCase(Locale.ROOT).replace('_', ' ')
                : "business";

        Map<String, String> values = new LinkedHashMap<>();
        values.put("{{businessName}}", businessName);
        values.put("{{email}}", email);
        values.put("{{phone}}", phone);
        values.put("{{address}}", address);
        values.put("{{city}}", city);
        values.put("{{description}}", description);
        values.put("{{ctaPrimary}}", ctaPrimary);
        values.put("{{activityType}}", activityType);
        return values;
    }

    private static String toCtaLabel(BusinessProfile profile) {
        if (profile == null || profile.getPrimaryCTA() == null) {
            return "Contact us";
        }
        return switch (profile.getPrimaryCTA()) {
            case CALL_NOW -> "Call now";
            case BOOK_NOW -> "Book now";
            case GET_QUOTE -> "Get a quote";
            case CONTACT_US -> "Contact us";
            case ORDER_NOW -> "Order now";
            case SEND_MESSAGE -> "Send message";
        };
    }

    private static String safe(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? fallback : trimmed;
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
