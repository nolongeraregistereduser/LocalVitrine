package com.localvitrine.dto;

import com.localvitrine.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank @Size(max = 200) String title,
        @NotNull ProjectStatus status,
        @Size(max = 500) String publicUrl
) {
}
