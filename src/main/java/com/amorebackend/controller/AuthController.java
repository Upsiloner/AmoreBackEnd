package com.amorebackend.controller;

import com.common.ApiResponse;
import com.amorebackend.service.AuthService;
import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.dto.Auth.ForgetDTO;
import com.amorebackend.dto.Auth.LoginDTO;
import com.amorebackend.vo.Auth.LoginVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Auth")
public class AuthController {
    @Resource
    private AuthService authService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterDTO request) {
        return authService.register(request);
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forget")
    public ApiResponse<?> forget(@RequestBody ForgetDTO request) {
        return authService.forget(request);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendCode")
    public ApiResponse<String> sendCode(@RequestBody Map<String, String> request) {
        return authService.sendCode(request.get("email"));
    }

    /**
     * 校验验证码
     */
    @GetMapping("/verifyCode")
    public ApiResponse<Boolean> verifyCode(@RequestParam String email, @RequestParam String code) {
        return authService.verifyCode(email, code);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@RequestBody LoginDTO request) {
        return authService.login(request);
    }
}
