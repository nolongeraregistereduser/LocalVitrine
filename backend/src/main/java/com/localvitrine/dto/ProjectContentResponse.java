package com.localvitrine.dto;

import com.localvitrine.entity.Project;

import java.time.Instant;

public record ProjectContentResponse(
        Long projectId,
        String htmlContent,
        String cssContent,
        Instant updatedAt
) {
    public static ProjectContentResponse fromEntity(Project project) {
        return new ProjectContentResponse(
                project.getId(),
                project.getHtmlContent(),
                project.getCssContent(),
                project.getUpdatedAt()
        );
    }
}
