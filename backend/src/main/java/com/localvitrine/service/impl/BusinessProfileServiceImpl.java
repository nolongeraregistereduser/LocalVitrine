package com.localvitrine.service.impl;

import com.localvitrine.dto.BusinessProfileRequest;
import com.localvitrine.dto.BusinessProfileResponse;
import com.localvitrine.entity.BusinessProfile;
import com.localvitrine.entity.Project;
import com.localvitrine.entity.User;
import com.localvitrine.repository.BusinessProfileRepository;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.service.BusinessProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class BusinessProfileServiceImpl implements BusinessProfileService {

    private final BusinessProfileRepository businessProfileRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public BusinessProfileServiceImpl(
            BusinessProfileRepository businessProfileRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository) {
        this.businessProfileRepository = businessProfileRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BusinessProfileResponse createBusinessProfile(Long projectId, BusinessProfileRequest request) {
        Project project = requireOwnedProject(projectId);
        if (businessProfileRepository.findByProjectId(projectId).isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Business profile already exists for this project");
        }
        BusinessProfile profile = BusinessProfile.builder()
                .project(project)
                .businessName(request.businessName().trim())
                .city(request.city().trim())
                .address(request.address().trim())
                .description(request.description().trim())
                .detailedDescription(request.detailedDescription() != null ? request.detailedDescription().trim() : null)
                .targetAudience(request.targetAudience() != null ? request.targetAudience().trim() : null)
                .phone(request.phone().trim())
                .email(request.email().trim())
                .website(request.website() != null ? request.website().trim() : null)
                .goal(request.goal())
                .sector(request.sector())
                .primaryCTA(request.primaryCTA())
                .facebook(request.facebook() != null ? request.facebook().trim() : null)
                .instagram(request.instagram() != null ? request.instagram().trim() : null)
                .whatsapp(request.whatsapp() != null ? request.whatsapp().trim() : null)
                .build();
        businessProfileRepository.save(profile);
        return BusinessProfileResponse.fromEntity(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessProfileResponse getBusinessProfile(Long projectId) {
        requireOwnedProject(projectId);
        BusinessProfile profile = businessProfileRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Business profile not found"));
        return BusinessProfileResponse.fromEntity(profile);
    }

    @Override
    @Transactional
    public BusinessProfileResponse updateBusinessProfile(Long projectId, BusinessProfileRequest request) {
        requireOwnedProject(projectId);
        BusinessProfile profile = businessProfileRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Business profile not found"));
        profile.setBusinessName(request.businessName().trim());
        profile.setCity(request.city().trim());
        profile.setAddress(request.address().trim());
        profile.setDescription(request.description().trim());
        profile.setDetailedDescription(request.detailedDescription() != null ? request.detailedDescription().trim() : null);
        profile.setTargetAudience(request.targetAudience() != null ? request.targetAudience().trim() : null);
        profile.setPhone(request.phone().trim());
        profile.setEmail(request.email().trim());
        profile.setWebsite(request.website() != null ? request.website().trim() : null);
        profile.setGoal(request.goal());
        profile.setSector(request.sector());
        profile.setPrimaryCTA(request.primaryCTA());
        profile.setFacebook(request.facebook() != null ? request.facebook().trim() : null);
        profile.setInstagram(request.instagram() != null ? request.instagram().trim() : null);
        profile.setWhatsapp(request.whatsapp() != null ? request.whatsapp().trim() : null);
        businessProfileRepository.save(profile);
        return BusinessProfileResponse.fromEntity(profile);
    }

    private Project requireOwnedProject(Long projectId) {
        User owner = requireCurrentUser();
        return projectRepository.findByIdAndOwnerId(projectId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Project not found"));
    }

    private User requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
    }

}
