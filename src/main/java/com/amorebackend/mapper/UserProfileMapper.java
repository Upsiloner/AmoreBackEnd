package com.amorebackend.mapper;

import com.amorebackend.entity.UserProfile;
import com.amorebackend.vo.UserInfo.ProfileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {

    ProfileVO findByUid(@Param("uid") String uid);

    boolean insert(UserProfile userProfile);
}
