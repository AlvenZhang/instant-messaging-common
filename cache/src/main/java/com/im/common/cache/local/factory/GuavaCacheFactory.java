package com.im.common.cache.local.factory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Guava 缓存工厂类。
 * 提供多种方式创建 Guava Cache 实例，支持不同的配置参数。
 */
public class GuavaCacheFactory {

    /**
     * 创建默认配置的 Guava Cache 实例。
     * 默认配置：最大容量 1000，写入后 10 分钟过期。
     *
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createDefault() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 创建指定最大容量的 Guava Cache 实例。
     *
     * @param maximumSize 最大容量
     * @param <K>         键的类型
     * @param <V>         值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithMaxSize(long maximumSize) {
        return CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .build();
    }

    /**
     * 创建指定过期时间的 Guava Cache 实例（写入后过期）。
     *
     * @param expireAfterWrite 写入后过期时间
     * @param <K>              键的类型
     * @param <V>              值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithExpireAfterWrite(Duration expireAfterWrite) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expireAfterWrite.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 创建指定过期时间的 Guava Cache 实例（访问后过期）。
     *
     * @param expireAfterAccess 访问后过期时间
     * @param <K>               键的类型
     * @param <V>               值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithExpireAfterAccess(Duration expireAfterAccess) {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(expireAfterAccess.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 创建指定最大容量和写入后过期时间的 Guava Cache 实例。
     *
     * @param maximumSize      最大容量
     * @param expireAfterWrite 写入后过期时间
     * @param <K>              键的类型
     * @param <V>              值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithMaxSizeAndExpireAfterWrite(long maximumSize, Duration expireAfterWrite) {
        return CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWrite.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 创建指定最大容量和访问后过期时间的 Guava Cache 实例。
     *
     * @param maximumSize       最大容量
     * @param expireAfterAccess 访问后过期时间
     * @param <K>               键的类型
     * @param <V>               值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithMaxSizeAndExpireAfterAccess(long maximumSize, Duration expireAfterAccess) {
        return CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccess.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 创建完全自定义配置的 Guava Cache 实例。
     *
     * @param maximumSize       最大容量
     * @param expireAfterWrite  写入后过期时间（可为 null）
     * @param expireAfterAccess 访问后过期时间（可为 null）
     * @param initialCapacity   初始容量（可为 null）
     * @param concurrencyLevel  并发级别（可为 null）
     * @param <K>               键的类型
     * @param <V>               值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createCustom(Long maximumSize,
                                                   Duration expireAfterWrite,
                                                   Duration expireAfterAccess,
                                                   Integer initialCapacity,
                                                   Integer concurrencyLevel) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

        if (maximumSize != null) {
            builder.maximumSize(maximumSize);
        }

        if (expireAfterWrite != null) {
            builder.expireAfterWrite(expireAfterWrite.toMillis(), TimeUnit.MILLISECONDS);
        }

        if (expireAfterAccess != null) {
            builder.expireAfterAccess(expireAfterAccess.toMillis(), TimeUnit.MILLISECONDS);
        }

        if (initialCapacity != null) {
            builder.initialCapacity(initialCapacity);
        }

        if (concurrencyLevel != null) {
            builder.concurrencyLevel(concurrencyLevel);
        }

        return builder.build();
    }

    /**
     * 创建支持软引用的 Guava Cache 实例。
     * 软引用允许在内存不足时回收缓存对象。
     *
     * @param maximumSize 最大容量
     * @param <K>         键的类型
     * @param <V>         值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithSoftValues(long maximumSize) {
        return CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .softValues()
                .build();
    }

    /**
     * 创建支持弱引用的 Guava Cache 实例。
     * 弱引用允许在 GC 时回收缓存对象。
     *
     * @param maximumSize 最大容量
     * @param <K>         键的类型
     * @param <V>         值的类型
     * @return Guava Cache 实例
     */
    public static <K, V> Cache<K, V> createWithWeakValues(long maximumSize) {
        return CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .weakValues()
                .build();
    }
}
