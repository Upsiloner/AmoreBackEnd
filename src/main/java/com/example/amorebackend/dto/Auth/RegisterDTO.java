package com.example.amorebackend.dto.Auth;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    private String code;
}
