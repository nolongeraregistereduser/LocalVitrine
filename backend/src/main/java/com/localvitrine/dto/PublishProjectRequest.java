package com.localvitrine.dto;

import jakarta.validation.constraints.Size;

public record PublishProjectRequest(
        @Size(max = 200) String slug
) {
}
