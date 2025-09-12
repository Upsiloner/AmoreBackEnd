package com.amorebackend.mapper;

import com.amorebackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);
    User findByUsername(@Param("username") String username);
    boolean insert(User user);

    boolean updatePasswordByUsername(@Param("email") String email, @Param("password") String password);
}
