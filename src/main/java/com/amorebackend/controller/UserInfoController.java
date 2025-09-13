package com.amorebackend.controller;

import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.service.AuthService;
import com.common.ApiResponse;
import com.amorebackend.vo.UserInfo.ProfileVO;
import com.amorebackend.service.UserInfoService;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/UserInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public ApiResponse<ProfileVO> profile(HttpServletRequest requestFilter) {
        String uid = (String) requestFilter.getAttribute("uid");
        return userInfoService.profile(uid);
    }

}
