package com.amorebackend.service;

import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.entity.User;
import com.amorebackend.mapper.UserMapper;
import com.common.ApiResponse;
import com.common.TinyRedis;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.annotation.Resource;

@Service
public class AuthService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private TinyRedis tinyRedis;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ApiResponse<?> register(RegisterDTO request) {

        String storedCode = tinyRedis.get("email:code:" + request.getEmail());

        if (storedCode == null || !storedCode.equals(request.getCode())) {
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
