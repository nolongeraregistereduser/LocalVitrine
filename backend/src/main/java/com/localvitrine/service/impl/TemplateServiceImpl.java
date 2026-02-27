package com.localvitrine.service.impl;

import com.localvitrine.dto.AdminTemplateRequest;
import com.localvitrine.dto.AdminTemplateResponse;
import com.localvitrine.dto.TemplateResponse;
import com.localvitrine.entity.Template;
import com.localvitrine.repository.TemplateRepository;
import com.localvitrine.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemplateResponse> listActiveTemplates() {
        return templateRepository.findByIsActiveTrueOrderByNameAsc().stream()
                .map(TemplateResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateResponse getActiveTemplateById(Long id) {
        Template template = templateRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Template not found"));
        return TemplateResponse.fromEntity(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTemplateResponse> listAllTemplatesForAdmin() {
        return templateRepository.findAllByOrderByNameAsc().stream()
                .map(AdminTemplateResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public AdminTemplateResponse createTemplate(AdminTemplateRequest request) {
        String normalizedCode = normalizeCode(request.code());
        if (templateRepository.existsByCode(normalizedCode)) {
            throw new ResponseStatusException(CONFLICT, "Template code already exists");
        }
        Template template = Template.builder()
                .name(request.name().trim())
                .code(normalizedCode)
                .description(request.description().trim())
                .activityType(request.activityType())
                .previewUrl(request.previewUrl().trim())
                .isActive(true)
                .build();
        templateRepository.save(template);
        return AdminTemplateResponse.fromEntity(template);
    }

    @Override
    @Transactional
    public AdminTemplateResponse updateTemplate(Long id, AdminTemplateRequest request) {
        Template template = requireTemplate(id);
        String normalizedCode = normalizeCode(request.code());
        if (templateRepository.existsByCodeAndIdNot(normalizedCode, id)) {
            throw new ResponseStatusException(CONFLICT, "Template code already exists");
        }
        template.setName(request.name().trim());
        template.setCode(normalizedCode);
        template.setDescription(request.description().trim());
        template.setActivityType(request.activityType());
        template.setPreviewUrl(request.previewUrl().trim());
        templateRepository.save(template);
        return AdminTemplateResponse.fromEntity(template);
    }

    @Override
    @Transactional
    public AdminTemplateResponse activateTemplate(Long id) {
        Template template = requireTemplate(id);
        template.setIsActive(true);
        templateRepository.save(template);
        return AdminTemplateResponse.fromEntity(template);
    }

    @Override
    @Transactional
    public AdminTemplateResponse deactivateTemplate(Long id) {
        Template template = requireTemplate(id);
        template.setIsActive(false);
        templateRepository.save(template);
        return AdminTemplateResponse.fromEntity(template);
    }

    private Template requireTemplate(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Template not found"));
    }

    private static String normalizeCode(String code) {
        return code.trim().toLowerCase(Locale.ROOT);
    }
}
