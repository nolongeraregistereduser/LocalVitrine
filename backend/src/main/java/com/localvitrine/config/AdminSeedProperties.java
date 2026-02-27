package com.localvitrine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.seed.admin")
public record AdminSeedProperties(
        boolean enabled,
        String fullName,
        String email,
        String password
) {
}
