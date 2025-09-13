package com.amorebackend.vo.UserInfo;

import lombok.Data;

@Data
public class ProfileVO {
    private String uid;
    private String username;
    private String email;
    private Integer gender;
    private String bio;
    private Integer points;
    private byte[] avatar;
}
