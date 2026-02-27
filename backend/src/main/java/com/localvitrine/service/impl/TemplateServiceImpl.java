package com.localvitrine.service.impl;

import com.localvitrine.dto.TemplateResponse;
import com.localvitrine.entity.Template;
import com.localvitrine.repository.TemplateRepository;
import com.localvitrine.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
}
