package com.localvitrine.dto;

import com.localvitrine.entity.Project;
import com.localvitrine.entity.ProjectStatus;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String title,
        ProjectStatus status,
        String publicUrl,
        Long templateId,
        String templateName,
        String templateCode,
        Instant createdAt,
        Instant updatedAt
) {

    public static ProjectResponse fromEntity(Project project) {
        Long templateId = project.getTemplate() != null ? project.getTemplate().getId() : null;
        String templateName = project.getTemplate() != null ? project.getTemplate().getName() : null;
        String templateCode = project.getTemplate() != null ? project.getTemplate().getCode() : null;
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getPublicUrl(),
                templateId,
                templateName,
                templateCode,
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
