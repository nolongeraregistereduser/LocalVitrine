package com.localvitrine.controller;

import com.localvitrine.dto.AdminTemplateRequest;
import com.localvitrine.dto.AdminTemplateResponse;
import com.localvitrine.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/templates")
public class AdminTemplateController {

    private final TemplateService templateService;

    public AdminTemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<List<AdminTemplateResponse>> listAll() {
        return ResponseEntity.ok(templateService.listAllTemplatesForAdmin());
    }

    @PostMapping
    public ResponseEntity<AdminTemplateResponse> create(@Valid @RequestBody AdminTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.createTemplate(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminTemplateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminTemplateRequest request) {
        return ResponseEntity.ok(templateService.updateTemplate(id, request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AdminTemplateResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.activateTemplate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AdminTemplateResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.deactivateTemplate(id));
    }
}
