package com.amorebackend.dto.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
