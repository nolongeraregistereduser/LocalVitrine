package com.localvitrine.controller;

import com.localvitrine.dto.AdminUserResponse;
import com.localvitrine.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(adminUserService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<AdminUserResponse> enable(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.enableUser(id));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<AdminUserResponse> disable(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.disableUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        adminUserService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
