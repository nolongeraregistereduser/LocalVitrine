package com.localvitrine.dto;

import jakarta.validation.constraints.NotNull;

public record ProjectContentRequest(
        @NotNull String htmlContent,
        String cssContent
) {
}
