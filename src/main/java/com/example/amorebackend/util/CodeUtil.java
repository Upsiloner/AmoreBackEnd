package com.example.amorebackend.util;

import java.util.Random;

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
