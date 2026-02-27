package com.localvitrine.service.impl;

import com.localvitrine.dto.AdminUserResponse;
import com.localvitrine.entity.User;
import com.localvitrine.entity.UserStatus;
import com.localvitrine.repository.UserRepository;
import com.localvitrine.service.AdminUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponse> listUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AdminUserResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse getUserById(Long id) {
        return AdminUserResponse.fromEntity(requireUser(id));
    }

    @Override
    @Transactional
    public AdminUserResponse enableUser(Long id) {
        User user = requireUser(id);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return AdminUserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public AdminUserResponse disableUser(Long id) {
        User user = requireUser(id);
        ensureNotCurrentUser(user);
        user.setStatus(UserStatus.DISABLED);
        userRepository.save(user);
        return AdminUserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long id) {
        User user = requireUser(id);
        ensureNotCurrentUser(user);
        user.setStatus(UserStatus.DISABLED);
        userRepository.save(user);
    }

    private User requireUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    private void ensureNotCurrentUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return;
        }
        if (user.getEmail().equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(CONFLICT, "You cannot disable your own account");
        }
    }
}
