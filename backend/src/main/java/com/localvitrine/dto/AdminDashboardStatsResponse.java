package com.localvitrine.dto;

public record AdminDashboardStatsResponse(
        long totalUsers,
        long totalProjects,
        long activeTemplates
) {
}
