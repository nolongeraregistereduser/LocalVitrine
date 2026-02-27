package com.localvitrine.dto;

import com.localvitrine.entity.Template;
import com.localvitrine.enums.ActivityType;

import java.time.Instant;

public record TemplateResponse(
        Long id,
        String name,
        String code,
        String description,
        ActivityType activityType,
        String previewUrl,
        Instant createdAt,
        Instant updatedAt
) {

    public static TemplateResponse fromEntity(Template template) {
        return new TemplateResponse(
                template.getId(),
                template.getName(),
                template.getCode(),
                template.getDescription(),
                template.getActivityType(),
                template.getPreviewUrl(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}
