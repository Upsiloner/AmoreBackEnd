package com.amorebackend.service;

import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.dto.Auth.LoginDTO;
import com.amorebackend.vo.Auth.LoginVO;
import com.common.ApiResponse;

public interface AuthService {
    ApiResponse<?> register(RegisterDTO request);

    ApiResponse<String> sendCode(String email);

    ApiResponse<Boolean> verifyCode(String email, String code);

    ApiResponse<LoginVO> login(LoginDTO request);
}
