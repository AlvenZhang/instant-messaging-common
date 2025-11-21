package com.im.common.cache.distribute.impl;
import com.alibaba.fastjson.JSON;
import com.im.common.cache.distribute.DistributedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的分布式缓存实现。
 * 使用 @ConditionalOnProperty 注解，根据配置决定是否加载该类。
 * 配置项：cache.distributed.type=redis 时启用此实现。
 */
@Component
@ConditionalOnProperty(name = "cache.distributed.type", havingValue = "redis")
public class RedisCache implements DistributedCache {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 逻辑过期时间的键后缀。
     */
    private static final String LOGICAL_EXPIRE_SUFFIX = ":logical_expire";

    @Override
    public void set(String key, Object value) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        stringRedisTemplate.opsForValue().set(key, this.getValue(value));
    }

    @Override
    public void set(String key, Object value, Duration ttl) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            // 如果 TTL 无效，使用不带过期时间的 set
            set(key, value);
            return;
        }
        stringRedisTemplate.opsForValue().set(key, this.getValue(value), ttl.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean expire(String key, Duration ttl) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            return false;
        }
        Boolean result = stringRedisTemplate.expire(key, ttl.toMillis(), TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    @Override
    public void setWithLogicalExpire(String key, String value, Duration logicalExpire) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (logicalExpire == null || logicalExpire.isZero() || logicalExpire.isNegative()) {
            // 如果逻辑过期时间无效，直接设置值
            set(key, value);
            return;
        }

        // 计算逻辑过期时间戳（当前时间 + 逻辑过期时长）
        long logicalExpireTime = Instant.now().toEpochMilli() + logicalExpire.toMillis();

        // 将值和逻辑过期时间一起存储
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.opsForValue().set(key + LOGICAL_EXPIRE_SUFFIX, String.valueOf(logicalExpireTime));
    }

    @Override
    public String getString(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        String value = getString(key);
        if (value == null) {
            return null;
        }

        // 如果目标类型是 String，直接返回
        if (type == String.class) {
            return type.cast(value);
        }

        // 使用 fastjson 反序列化
        try {
            return JSON.parseObject(value, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize value for key: " + key, e);
        }
    }

    @Override
    public Map<String, String> batchGet(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyMap();
        }

        // 过滤掉空键
        List<String> validKeys = new ArrayList<>();
        for (String key : keys) {
            if (StringUtils.hasText(key)) {
                validKeys.add(key);
            }
        }

        if (validKeys.isEmpty()) {
            return Collections.emptyMap();
        }

        // 批量获取值
        List<String> values = stringRedisTemplate.opsForValue().multiGet(validKeys);
        if (values == null) {
            return Collections.emptyMap();
        }

        // 构建结果映射（只包含非空值）
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < validKeys.size(); i++) {
            String value = values.get(i);
            if (value != null) {
                result.put(validKeys.get(i), value);
            }
        }

        return result;
    }

    @Override
    public Set<String> getKeysByPattern(String regex) {
        if (!StringUtils.hasText(regex)) {
            throw new IllegalArgumentException("Regex pattern cannot be null or empty");
        }

        Set<String> keys = stringRedisTemplate.keys(regex);
        return keys != null ? keys : Collections.emptySet();
    }

    @Override
    public boolean delete(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }

        Boolean result = stringRedisTemplate.delete(key);

        // 同时删除逻辑过期时间键（如果存在）
        String logicalExpireKey = key + LOGICAL_EXPIRE_SUFFIX;
        stringRedisTemplate.delete(logicalExpireKey);

        return result != null && result;
    }

    /**
     * 检查逻辑过期时间是否已过期。
     *
     * @param key 缓存键
     * @return 如果已过期返回 true，否则返回 false
     */
    public boolean isLogicalExpired(String key) {
        if (!StringUtils.hasText(key)) {
            return true;
        }

        String logicalExpireKey = key + LOGICAL_EXPIRE_SUFFIX;
        String expireTimeStr = stringRedisTemplate.opsForValue().get(logicalExpireKey);

        if (expireTimeStr == null) {
            // 没有设置逻辑过期时间，认为未过期
            return false;
        }

        try {
            long expireTime = Long.parseLong(expireTimeStr);
            return Instant.now().toEpochMilli() > expireTime;
        } catch (NumberFormatException e) {
            // 解析失败，认为已过期
            return true;
        }
    }

    /**
     * 获取逻辑过期时间戳。
     *
     * @param key 缓存键
     * @return 逻辑过期时间戳（毫秒），如果不存在返回 null
     */
    public Long getLogicalExpireTime(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }

        String logicalExpireKey = key + LOGICAL_EXPIRE_SUFFIX;
        String expireTimeStr = stringRedisTemplate.opsForValue().get(logicalExpireKey);

        if (expireTimeStr == null) {
            return null;
        }

        try {
            return Long.parseLong(expireTimeStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
