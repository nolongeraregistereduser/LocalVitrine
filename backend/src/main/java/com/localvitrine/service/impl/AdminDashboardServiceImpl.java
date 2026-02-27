package com.localvitrine.service.impl;

import com.localvitrine.dto.AdminDashboardStatsResponse;
import com.localvitrine.repository.ProjectRepository;
import com.localvitrine.repository.TemplateRepository;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.service.AdminDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TemplateRepository templateRepository;

    public AdminDashboardServiceImpl(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TemplateRepository templateRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardStatsResponse getStats() {
        return new AdminDashboardStatsResponse(
                userRepository.count(),
                projectRepository.count(),
                templateRepository.countByIsActiveTrue()
        );
    }
}
