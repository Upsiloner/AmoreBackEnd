package com.example.amorebackend.util;

import java.util.concurrent.ConcurrentHashMap;

public class CodeStore {
    // key = email, value = CodeEntry(code, expireTime)
    private static final ConcurrentHashMap<String, CodeEntry> store = new ConcurrentHashMap<>();

    public static void saveCode(String email, String code, long expireMillis) {
        store.put(email, new CodeEntry(code, System.currentTimeMillis() + expireMillis));
    }

    public static boolean validateCode(String email, String code) {
        CodeEntry entry = store.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireTime) {
            store.remove(email);
            return false; // 过期
        }
        return entry.code.equals(code);
    }

    private static class CodeEntry {
        String code;
        long expireTime;
        CodeEntry(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }
}
