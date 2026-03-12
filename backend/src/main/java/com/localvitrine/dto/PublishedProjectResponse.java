package com.localvitrine.dto;

public record PublishedProjectResponse(
        Long projectId,
        String slug,
        String publicUrl
) {
}
