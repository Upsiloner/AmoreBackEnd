package com.amorebackend.service.impl;

import com.amorebackend.dto.Auth.RegisterDTO;
import com.amorebackend.dto.Auth.ForgetDTO;
import com.amorebackend.dto.Auth.LoginDTO;
import com.amorebackend.vo.Auth.LoginVO;
import com.amorebackend.entity.User;
import com.amorebackend.entity.UserProfile;
import com.amorebackend.mapper.UserMapper;
import com.amorebackend.mapper.UserProfileMapper;
import com.amorebackend.service.AuthService;
import com.common.ApiResponse;
import com.common.TinyRedis;
import com.util.CodeUtil;
import com.util.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private TinyRedis tinyRedis;
    @Resource
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String fromEmail = "zhangyutongxue@163.com"; // 发件人

    @Override
    public ApiResponse<?> register(RegisterDTO request) {
        String storedCode = tinyRedis.get("email:code:" + request.getEmail());

        if (storedCode == null || !storedCode.equals(request.getCode())) {
            return ApiResponse.error(400, "验证码错误或已过期");
        }

        if (userMapper.findByEmail(request.getEmail()) != null) {
            return ApiResponse.error(400, "邮箱已注册");
        }

        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
        UserProfile userProfile = new UserProfile(user.getUid());
        if (!userMapper.insert(user) || !userProfileMapper.insert(userProfile)) {
            return ApiResponse.error(400, "新建用户失败");
        }

        return ApiResponse.success("注册成功", null);
    }

    @Override
    public ApiResponse<?> forget(ForgetDTO request) {
        String storedCode = tinyRedis.get("email:code:" + request.getEmail());

        if (storedCode == null || !storedCode.equals(request.getCode())) {
            return ApiResponse.error(400, "验证码错误或已过期");
        }
        if (userMapper.findByEmail(request.getEmail()) == null) {
            return ApiResponse.error(400, "邮箱未注册");
        }
        if (!userMapper.updatePasswordByUsername(request.getEmail(), passwordEncoder.encode(request.getPassword()))) {
            return ApiResponse.error(400, "修改密码失败");
        }

        return ApiResponse.success("修改密码成功", null);
    }

    @Override
    public ApiResponse<String> sendCode(String email) {
        String code = CodeUtil.generateCode();
        tinyRedis.set("email:code:" + email, code, 60 * 5 * 1000);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("您的验证码");

            String htmlContent = String.format("""
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <div style="border: 1px solid #ccc; padding: 20px; max-width: 500px; margin: 0 auto;">
                            <h2 style="color: #8062bc; text-align: center;">青梅博客 验证码</h2>
                            <hr style="border: none; border-top: 2px solid #8062bc;">
                            <p style="font-size: 16px;">您好，</p>
                            <p style="font-size: 16px;">您的验证码为：</p>
                            <p style="font-size: 28px; font-weight: bold; color: #8062bc; text-align: center;">%s</p>
                            <p style="font-size: 16px; text-align: center;">验证码 5 分钟内有效，请勿泄露。</p>
                            <hr style="border: none; border-top: 1px dashed #aaa; margin-top: 30px;">
                            <p style="font-size: 12px; color: #999; text-align: center;">来自 AmoreBackend 安全验证系统</p>
                        </div>
                    </body>
                </html>
            """, code);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            return ApiResponse.success("验证码已发送", null);
        } catch (Exception e) {
            return ApiResponse.error(400, "验证码发送错误: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Boolean> verifyCode(String email, String code) {
        String storedCode = tinyRedis.get("email:code:" + email);
        if (storedCode != null && storedCode.equals(code)) {
            return ApiResponse.success("验证码校验成功", true);
        } else {
            return ApiResponse.error(400, "验证码校验错误");
        }
    }

    @Override
    public ApiResponse<LoginVO> login(LoginDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userMapper.findByEmail(email);
        if (user == null) {
            return ApiResponse.error(400, "该账号不存在，请注册账号");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ApiResponse.error(400, "密码错误");
        }

        String token = JwtUtil.generateToken(user.getUid());
        tinyRedis.set("uid:jwt:" + email, token, 30 * 60 * 1000);

        LoginVO loginVO = new LoginVO(token, user.getUid(), user.getUsername(), user.getEmail());

        return ApiResponse.success("登录成功", loginVO);
    }
}
