package com.localvitrine.dto;

import com.localvitrine.entity.Project;
import com.localvitrine.entity.ProjectStatus;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String title,
        ProjectStatus status,
        String publicUrl,
        Instant createdAt,
        Instant updatedAt
) {

    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getPublicUrl(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
