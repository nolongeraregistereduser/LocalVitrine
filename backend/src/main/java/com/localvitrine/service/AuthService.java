package com.localvitrine.service;

import com.localvitrine.dto.AuthResponse;
import com.localvitrine.dto.LoginRequest;
import com.localvitrine.dto.RegisterRequest;
import com.localvitrine.dto.UserProfileResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileResponse getCurrentUser();
}
