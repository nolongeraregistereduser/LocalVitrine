package com.localvitrine.service;

import com.localvitrine.dto.TemplateResponse;

import java.util.List;

public interface TemplateService {

    List<TemplateResponse> listActiveTemplates();

    TemplateResponse getActiveTemplateById(Long id);
}
