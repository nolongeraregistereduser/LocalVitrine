package com.localvitrine.dto;

import com.localvitrine.entity.User;

import java.time.Instant;

public record AdminUserResponse(
        Long id,
        String fullName,
        String email,
        String role,
        String status,
        Instant createdAt
) {
    public static AdminUserResponse fromEntity(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getName().name(),
                user.getStatus().name(),
                user.getCreatedAt()
        );
    }
}
