package com.localvitrine.service;

import com.localvitrine.dto.AdminTemplateRequest;
import com.localvitrine.dto.AdminTemplateResponse;
import com.localvitrine.dto.TemplateResponse;

import java.util.List;

public interface TemplateService {

    List<TemplateResponse> listActiveTemplates();

    TemplateResponse getActiveTemplateById(Long id);

    List<AdminTemplateResponse> listAllTemplatesForAdmin();

    AdminTemplateResponse createTemplate(AdminTemplateRequest request);

    AdminTemplateResponse updateTemplate(Long id, AdminTemplateRequest request);

    AdminTemplateResponse activateTemplate(Long id);

    AdminTemplateResponse deactivateTemplate(Long id);
}
