package com.amorebackend.service.impl;


import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.entity.User;
import com.amorebackend.entity.UserProfile;
import com.amorebackend.vo.UserInfo.ProfileVO;
import com.amorebackend.mapper.UserProfileMapper;
import com.amorebackend.service.UserInfoService;
import com.common.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserProfileMapper userProfileMapper;

    @Override
    public ApiResponse<ProfileVO> profile(String uid) {
        ProfileVO profile = userProfileMapper.findByUid(uid);

        return ApiResponse.success("获取用户信息成功", profile);
    }
}
