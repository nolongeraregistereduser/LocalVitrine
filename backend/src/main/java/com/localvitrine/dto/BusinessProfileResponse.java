package com.localvitrine.dto;

import com.localvitrine.entity.BusinessProfile;
import com.localvitrine.enums.Goal;
import com.localvitrine.enums.PrimaryCTA;
import com.localvitrine.enums.Sector;

import java.time.Instant;

public record BusinessProfileResponse(
        Long id,
        Long projectId,
        String businessName,
        String city,
        String address,
        String description,
        String detailedDescription,
        String targetAudience,
        String phone,
        String email,
        String website,
        Goal goal,
        Sector sector,
        PrimaryCTA primaryCTA,
        String facebook,
        String instagram,
        String whatsapp,
        Instant createdAt,
        Instant updatedAt
) {

    public static BusinessProfileResponse fromEntity(BusinessProfile profile) {
        return new BusinessProfileResponse(
                profile.getId(),
                profile.getProject().getId(),
                profile.getBusinessName(),
                profile.getCity(),
                profile.getAddress(),
                profile.getDescription(),
                profile.getDetailedDescription(),
                profile.getTargetAudience(),
                profile.getPhone(),
                profile.getEmail(),
                profile.getWebsite(),
                profile.getGoal(),
                profile.getSector(),
                profile.getPrimaryCTA(),
                profile.getFacebook(),
                profile.getInstagram(),
                profile.getWhatsapp(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
