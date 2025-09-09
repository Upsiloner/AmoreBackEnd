package com.amorebackend.service;

import com.amorebackend.dto.Auth.RegisterDTO;
import com.common.ApiResponse;

public interface AuthService {
    ApiResponse<?> register(RegisterDTO request);

    ApiResponse<String> sendCode(String email);

    ApiResponse<Boolean> verifyCode(String email, String code);
}
