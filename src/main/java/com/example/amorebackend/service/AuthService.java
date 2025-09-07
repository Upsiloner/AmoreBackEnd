package com.example.amorebackend.service;

import com.example.amorebackend.dto.Auth.RegisterDTO;
import com.example.amorebackend.entity.User;
import com.example.amorebackend.mapper.UserMapper;
import com.example.amorebackend.common.ApiResponse;
import com.example.amorebackend.util.CodeStore;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.annotation.Resource;

@Service
public class AuthService {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ApiResponse<?> register(RegisterDTO request) {

        boolean valid = CodeStore.validateCode(request.getEmail(), request.getCode());

        if (!valid) {
            return ApiResponse.error(400, "验证码错误或已过期");
        }

        if (userMapper.findByEmail(request.getEmail()) != null) {
            return ApiResponse.error(400, "邮箱已注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 密码加密
        userMapper.insert(user);

        return ApiResponse.success("注册成功", null);
    }
}
