package com.localvitrine.service;

import com.localvitrine.dto.BusinessProfileRequest;
import com.localvitrine.dto.BusinessProfileResponse;

public interface BusinessProfileService {

    BusinessProfileResponse createBusinessProfile(Long projectId, BusinessProfileRequest request);

    BusinessProfileResponse getBusinessProfile(Long projectId);

    BusinessProfileResponse updateBusinessProfile(Long projectId, BusinessProfileRequest request);
}
