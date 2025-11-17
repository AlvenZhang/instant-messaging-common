package com.im.common.cache.distribute;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 分布式缓存接口。
 * 定义常用的读写、过期与键匹配等操作抽象，便于不同缓存实现（如 Redis、Memcached）统一接入。
 */
public interface DistributedCache {

    /**
     * 根据 key 设置缓存（不设置过期时间）。
     * @param key 缓存键
     * @param value 缓存值（建议为 JSON 字符串或可序列化文本）
     */
    void set(String key, String value);

    /**
     * 根据 key 设置缓存并设置过期时间（TTL）。
     * @param key 缓存键
     * @param value 缓存值（建议为 JSON 字符串或可序列化文本）
     * @param ttl 过期时间（TTL），到期后缓存将自动失效
     */
    void set(String key, String value, Duration ttl);

    /**
     * 设置指定 key 的过期时间（TTL）。
     * @param key 缓存键
     * @param ttl 过期时间（TTL）
     * @return 设置是否成功
     */
    boolean expire(String key, Duration ttl);

    /**
     * 设置缓存并同时设置逻辑过期时间。
     * 逻辑过期指在值中携带一个“逻辑失效”的时间戳（如用于缓存击穿保护与异步刷新），
     * 到达逻辑过期后可返回旧值同时触发后台刷新。
     * @param key 缓存键
     * @param value 缓存值（内部可按约定携带逻辑过期标记）
     * @param logicalExpire 逻辑过期时间（非物理 TTL）
     */
    void setWithLogicalExpire(String key, String value, Duration logicalExpire);

    /**
     * 根据 key 获取字符串值。
     * @param key 缓存键
     * @return 字符串值，若不存在返回 null
     */
    String getString(String key);

    /**
     * 根据 key 获取指定类型的缓存值。
     * @param key 缓存键
     * @param type 目标类型（用于反序列化）
     * @param <T> 目标泛型类型
     * @return 指定类型的值，若不存在返回 null
     */
    <T> T get(String key, Class<T> type);

    /**
     * 根据 key 列表批量获取缓存值。
     * @param keys 缓存键列表
     * @return 键到值的映射，未命中键不包含在返回映射中
     */
    Map<String, String> batchGet(List<String> keys);

    /**
     * 根据正则表达式匹配并获取所有键。
     * @param regex 正则表达式（如 "user:.*"）
     * @return 匹配到的键集合
     */
    Set<String> getKeysByPattern(String regex);

    /**
     * 删除指定的 key。
     * @param key 缓存键
     * @return 删除是否成功
     */
    boolean delete(String key);
}