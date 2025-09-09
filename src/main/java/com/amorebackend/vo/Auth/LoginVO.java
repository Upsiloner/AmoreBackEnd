package com.amorebackend.vo.Auth;

import lombok.Data;

@Data
public class LoginVO {

    private String token;
    private Long uid;
    private String username;
    private String email;

    public LoginVO(String token, Long uid, String username, String email) {
        this.token = token;
        this.uid = uid;
        this.username = username;
        this.email = email;
    }
}
