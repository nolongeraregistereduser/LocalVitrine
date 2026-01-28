package com.localvitrine.dto;

import com.localvitrine.entity.User;

public record UserProfileResponse(Long id, String fullName, String email, String status, String role) {

    public static UserProfileResponse fromEntity(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus().name(),
                user.getRole().getName().name()
        );
    }
}
