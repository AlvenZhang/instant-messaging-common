package com.im.common.cache.local.impl;
import com.google.common.cache.Cache;
import com.im.common.cache.local.factory.GuavaCacheFactory;
import com.im.common.cache.local.LocalCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 基于 Guava Cache 的本地缓存实现。
 * 使用 @ConditionalOnProperty 注解，根据配置决定是否加载该类。
 * 配置项：cache.local.type=guava 时启用此实现。
 */
@Component
@ConditionalOnProperty(name = "cache.local.type", havingValue = "guava", matchIfMissing = false)
public class GuavaLocalCache implements LocalCache {

    /**
     * 默认的 Guava Cache 实例，用于不设置过期时间的缓存。
     */
    private final Cache<Object, Object> defaultCache;

    /**
     * 存储带有自定义 TTL 的缓存实例。
     * Key 为 TTL 的毫秒数，Value 为对应的 Cache 实例。
     */
    private final ConcurrentMap<Long, Cache<Object, Object>> ttlCacheMap;

    /**
     * 构造函数，初始化默认缓存和 TTL 缓存映射。
     */
    public GuavaLocalCache() {
        // 创建默认缓存实例（最大容量 10000，不设置过期时间）
        this.defaultCache = GuavaCacheFactory.createWithMaxSize(10000);
        this.ttlCacheMap = new ConcurrentHashMap<>();
    }

    @Override
    public <K, V> void put(K key, V value) {
        defaultCache.put(key, value);
    }

    @Override
    public <K, V> void put(K key, V value, Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            // 如果 TTL 无效，使用默认缓存
            put(key, value);
            return;
        }

        // 获取或创建对应 TTL 的缓存实例
        long ttlMillis = ttl.toMillis();
        Cache<Object, Object> cache = ttlCacheMap.computeIfAbsent(ttlMillis, k ->
                GuavaCacheFactory.createWithExpireAfterWrite(Duration.ofMillis(k))
        );

        cache.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V get(K key) {
        // 先从默认缓存中查找
        V value = (V) defaultCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        // 从所有 TTL 缓存中查找
        for (Cache<Object, Object> cache : ttlCacheMap.values()) {
            value = (V) cache.getIfPresent(key);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    @Override
    public <K, V> V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public <K> boolean remove(K key) {
        boolean removed = false;

        // 从默认缓存中移除
        if (defaultCache.getIfPresent(key) != null) {
            defaultCache.invalidate(key);
            removed = true;
        }

        // 从所有 TTL 缓存中移除
        for (Cache<Object, Object> cache : ttlCacheMap.values()) {
            if (cache.getIfPresent(key) != null) {
                cache.invalidate(key);
                removed = true;
            }
        }

        return removed;
    }

    @Override
    public void clear() {
        // 清空默认缓存
        defaultCache.invalidateAll();

        // 清空所有 TTL 缓存
        ttlCacheMap.values().forEach(Cache::invalidateAll);
    }

    @Override
    public long size() {
        // 计算默认缓存的大小
        long totalSize = defaultCache.size();

        // 累加所有 TTL 缓存的大小
        for (Cache<Object, Object> cache : ttlCacheMap.values()) {
            totalSize += cache.size();
        }

        return totalSize;
    }

    @Override
    public <K> boolean containsKey(K key) {
        // 检查默认缓存
        if (defaultCache.getIfPresent(key) != null) {
            return true;
        }

        // 检查所有 TTL 缓存
        for (Cache<Object, Object> cache : ttlCacheMap.values()) {
            if (cache.getIfPresent(key) != null) {
                return true;
            }
        }

        return false;
    }
}
