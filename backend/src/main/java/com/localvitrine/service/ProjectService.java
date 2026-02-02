package com.localvitrine.service;

import com.localvitrine.dto.ProjectRequest;
import com.localvitrine.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request);

    List<ProjectResponse> getMyProjects();

    ProjectResponse getMyProjectById(Long id);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);
}
