package com.util;

import java.util.Random;

// 生成 6 位数随机验证码
public class CodeUtil {
    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10)); // 0-9 随机
        }
        return sb.toString();
    }
}
