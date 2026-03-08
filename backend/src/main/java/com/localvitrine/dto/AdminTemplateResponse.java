package com.localvitrine.dto;

import com.localvitrine.entity.Template;
import com.localvitrine.enums.ActivityType;

import java.time.Instant;

public record AdminTemplateResponse(
        Long id,
        String name,
        String code,
        String description,
        ActivityType activityType,
        String previewUrl,
        String starterHtml,
        String starterCss,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
    public static AdminTemplateResponse fromEntity(Template template) {
        return new AdminTemplateResponse(
                template.getId(),
                template.getName(),
                template.getCode(),
                template.getDescription(),
                template.getActivityType(),
                template.getPreviewUrl(),
                template.getStarterHtml(),
                template.getStarterCss(),
                template.getIsActive(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}
