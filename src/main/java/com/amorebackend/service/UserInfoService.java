package com.amorebackend.service;

import com.amorebackend.vo.UserInfo.ProfileVO;
import com.common.ApiResponse;

public interface UserInfoService {
    ApiResponse<ProfileVO> profile(String uid);
}
