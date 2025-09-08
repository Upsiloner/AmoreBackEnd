package com.example.amorebackend.controller;

import com.example.amorebackend.util.CodeUtil;
import com.example.amorebackend.util.TinyRedisUtil;
import com.example.amorebackend.common.ApiResponse;
import com.example.amorebackend.service.AuthService;
import com.example.amorebackend.dto.Auth.RegisterDTO;
import jakarta.annotation.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Auth")
public class AuthController {
    @Resource
    private AuthService authService;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private TinyRedisUtil tinyRedis;

    private final String fromEmail = "zhangyutongxue@163.com"; // 发件人

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterDTO request) {
        return authService.register(request);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendCode")
    public ApiResponse<String> sendCode(@RequestBody Map<String, String> request) {
        String code = CodeUtil.generateCode();

        String email = request.get("email");
        tinyRedis.set("email:code:" + email, code, 60 * 5 *1000);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("您的验证码");

            // HTML 内容
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

    /**
     * 校验验证码
     */
    @GetMapping("/verifyCode")
    public ApiResponse<Boolean> verifyCode(@RequestParam String email, @RequestParam String code) {
        String storedCode = tinyRedis.get("email:code:" + email);
        if (storedCode != null && storedCode.equals(code)) {
            return ApiResponse.success("验证码校验成功", null);
        } else {
            return ApiResponse.error(400, "验证码校验错误");
        }
    }
}
