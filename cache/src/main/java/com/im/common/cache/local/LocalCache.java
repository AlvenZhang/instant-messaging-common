package com.im.common.cache.local;

import java.time.Duration;

/**
 * 本地缓存接口。
 * 定义本地缓存的基本操作，适用于进程内缓存实现（如 Caffeine、Guava Cache 等）。
 * 相比分布式缓存，本地缓存具有更快的访问速度，但不支持跨节点共享。
 */
public interface LocalCache {

    /**
     * 向缓存中添加数据（不设置过期时间）。
     * @param key 缓存键
     * @param value 缓存值
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    <K, V> void put(K key, V value);

    /**
     * 向缓存中添加数据并设置过期时间（TTL）。
     * @param key 缓存键
     * @param value 缓存值
     * @param ttl 过期时间（TTL），到期后缓存将自动失效
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    <K, V> void put(K key, V value, Duration ttl);

    /**
     * 根据 key 从缓存中获取数据。
     * @param key 缓存键
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 缓存值，若不存在或已过期返回 null
     */
    <K, V> V get(K key);

    /**
     * 根据 key 从缓存中获取数据，若不存在则返回默认值。
     * @param key 缓存键
     * @param defaultValue 默认值
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 缓存值，若不存在或已过期返回默认值
     */
    <K, V> V getOrDefault(K key, V defaultValue);

    /**
     * 移除缓存中的数据。
     * @param key 缓存键
     * @param <K> 键的类型
     * @return 移除是否成功，若 key 不存在返回 false
     */
    <K> boolean remove(K key);

    /**
     * 清空所有缓存数据。
     */
    void clear();

    /**
     * 获取缓存中的数据条目数量。
     * @return 缓存条目数量
     */
    long size();

    /**
     * 判断缓存中是否包含指定的 key。
     * @param key 缓存键
     * @param <K> 键的类型
     * @return 若存在返回 true，否则返回 false
     */
    <K> boolean containsKey(K key);
}
