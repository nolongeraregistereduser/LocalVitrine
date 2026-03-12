package com.localvitrine.service;

import com.localvitrine.dto.ProjectContentRequest;
import com.localvitrine.dto.ProjectContentResponse;
import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;
import com.localvitrine.dto.PublicLandingPageResponse;
import com.localvitrine.dto.PublishedProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request);

    List<ProjectResponse> getMyProjects();

    ProjectResponse getMyProjectById(Long id);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);

    ProjectResponse assignTemplateToProject(Long projectId, Long templateId);

    ProjectContentResponse getProjectContent(Long id);

    ProjectContentResponse updateProjectContent(Long id, ProjectContentRequest request);

    PublishedProjectResponse publishProject(Long id, String requestedSlug);

    PublicLandingPageResponse getPublicLandingPage(String slug);
}
