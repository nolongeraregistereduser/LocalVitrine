package com.localvitrine.dto;

import com.localvitrine.entity.BusinessProfile;

import java.time.Instant;

public record BusinessProfileResponse(
        Long id,
        Long projectId,
        String businessName,
        String city,
        String description,
        String phone,
        String email,
        String goal,
        String sector,
        Instant createdAt,
        Instant updatedAt
) {

    public static BusinessProfileResponse fromEntity(BusinessProfile profile) {
        return new BusinessProfileResponse(
                profile.getId(),
                profile.getProject().getId(),
                profile.getBusinessName(),
                profile.getCity(),
                profile.getDescription(),
                profile.getPhone(),
                profile.getEmail(),
                profile.getGoal(),
                profile.getSector(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
