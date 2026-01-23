package com.localvitrine.service.impl;

import com.localvitrine.dto.HealthResponse;
import com.localvitrine.service.HealthService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public HealthResponse getHealth() {
        return new HealthResponse("ok", Instant.now());
    }
}

