package com.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class TinyRedis {

    private static class ValueEntry {
        String value;
        long expireTime;  // 毫秒时间戳

        ValueEntry(String value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
    }

    private final ConcurrentHashMap<String, ValueEntry> store = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public TinyRedis() {
        // 定期清理过期 key，每分钟一次
        cleaner.scheduleAtFixedRate(this::cleanExpired, 1, 1, TimeUnit.MINUTES);
    }

    // 设置 key-value，带过期时间
    public void set(String key, String value, long expireMillis) {
        long expireAt = System.currentTimeMillis() + expireMillis;
        store.put(key, new ValueEntry(value, expireAt));
    }

    public void set(String key, String value) {
        store.put(key, new ValueEntry(value, -1));
    }

    public String get(String key) {
        ValueEntry entry = store.get(key);
        if (entry == null) return null;
        if (entry.expireTime != -1 && System.currentTimeMillis() > entry.expireTime) {
            store.remove(key);
            return null;
        }
        return entry.value;
    }

    public void delete(String key) {
        store.remove(key);
    }

    public boolean hasKey(String key) {
        return get(key) != null;
    }

    private void cleanExpired() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, ValueEntry> e : store.entrySet()) {
            ValueEntry entry = e.getValue();
            if (entry.expireTime != -1 && now > entry.expireTime) {
                store.remove(e.getKey());
            }
        }
    }
}
