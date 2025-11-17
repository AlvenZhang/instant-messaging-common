package com.im.common.cache.lock;
/**
 * 分布式锁工厂接口。
 * 定义获取分布式锁实例的方法，通过调用该方法可以获取一个分布式锁实例。
 * 支持多种分布式锁实现（如 Redisson、Redis 等）。
 */
public interface DistributedLockFactory {

    /**
     * 获取分布式锁实例。
     * 根据指定的锁名称获取一个分布式锁实例。
     *
     * @param lockName 锁的名称，用于唯一标识一个锁
     * @return DistributedLock 实例
     * @throws IllegalArgumentException 如果 lockName 为 null 或空字符串
     */
    DistributedLock getLock(String lockName);

    /**
     * 获取分布式锁实例（带前缀）。
     * 根据指定的前缀和锁名称获取一个分布式锁实例。
     * 实际的锁名称为：prefix + lockName
     *
     * @param prefix   锁名称前缀
     * @param lockName 锁的名称
     * @return DistributedLock 实例
     * @throws IllegalArgumentException 如果 prefix 或 lockName 为 null 或空字符串
     */
    DistributedLock getLock(String prefix, String lockName);

    /**
     * 获取工厂的名称。
     * 用于标识该工厂的实现类型（如 "redisson"、"redis" 等）。
     *
     * @return 工厂名称
     */
    String getFactoryName();
}
