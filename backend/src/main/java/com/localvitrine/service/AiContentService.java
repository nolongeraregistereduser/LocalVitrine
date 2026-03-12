package com.localvitrine.service;

import com.localvitrine.dto.AiGeneratedContentResponse;

public interface AiContentService {

    AiGeneratedContentResponse generateForProject(Long projectId);
}
