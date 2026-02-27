package com.localvitrine.dto;

import com.localvitrine.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminTemplateRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 80) String code,
        @NotBlank String description,
        @NotNull ActivityType activityType,
        @NotBlank @Size(max = 2000) String previewUrl
) {
}
