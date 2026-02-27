package com.localvitrine.service;

import com.localvitrine.dto.AdminUserResponse;

import java.util.List;

public interface AdminUserService {

    List<AdminUserResponse> listUsers();

    AdminUserResponse getUserById(Long id);

    AdminUserResponse enableUser(Long id);

    AdminUserResponse disableUser(Long id);

    void softDeleteUser(Long id);
}
