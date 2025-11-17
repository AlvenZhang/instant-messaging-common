package com.im.common.cache.lock.impl;
import com.im.common.cache.lock.DistributedLock;
import com.im.common.cache.lock.DistributedLockFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redisson 的分布式锁工厂实现类。
 * 通过 Redisson 客户端创建分布式锁实例，支持多种锁操作。
 * 使用 @ConditionalOnProperty 注解，根据配置决定是否加载该类。
 * 配置项：cache.distributed.redisson.enabled=true 时启用此实现。
 */
@Component
@ConditionalOnProperty(name = "cache.distributed.redisson.enabled", havingValue = "true")
public class RedissonLockFactory implements DistributedLockFactory {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 默认的锁名称前缀。
     */
    private static final String DEFAULT_LOCK_PREFIX = "lock:";

    @Override
    public DistributedLock getLock(String lockName) {
        return getLock(DEFAULT_LOCK_PREFIX, lockName);
    }

    @Override
    public DistributedLock getLock(String prefix, String lockName) {
        if (!StringUtils.hasText(prefix)) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        if (!StringUtils.hasText(lockName)) {
            throw new IllegalArgumentException("Lock name cannot be null or empty");
        }

        // 组合完整的锁名称
        String fullLockName = prefix + lockName;

        // 从 Redisson 客户端获取 RLock 实例
        RLock rLock = redissonClient.getLock(fullLockName);

        // 返回匿名内部类实现的 DistributedLock
        return new DistributedLock() {
            @Override
            public boolean tryLock() {
                return rLock.tryLock();
            }

            @Override
            public boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, unit);
            }

            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, leaseTime, unit);
            }

            @Override
            public void lock() {
                rLock.lock();
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                rLock.lock(leaseTime, unit);
            }

            @Override
            public boolean unlock() {
                try {
                    rLock.unlock();
                    return true;
                } catch (IllegalMonitorStateException e) {
                    // 当前线程不持有该锁
                    return false;
                }
            }

            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }

            @Override
            public String getLockName() {
                return fullLockName;
            }
        };
    }

    @Override
    public String getFactoryName() {
        return "redisson";
    }
}
