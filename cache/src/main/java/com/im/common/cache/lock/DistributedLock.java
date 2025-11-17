package com.im.common.cache.lock;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口。
 * 定义分布式锁的常规操作方法，包含加锁、释放锁、判断是否已加锁等操作。
 * 适用于多进程、多服务器环境下的资源竞争控制。
 */
public interface DistributedLock {

    /**
     * 尝试加锁（非阻塞）。
     * 立即返回加锁结果，不会等待。
     *
     * @return 加锁成功返回 true，失败返回 false
     */
    boolean tryLock();

    /**
     * 尝试加锁，指定等待时间（阻塞）。
     * 在指定时间内尝试加锁，如果在此期间获得锁则立即返回 true，
     * 否则等待到超时时间后返回 false。
     *
     * @param waitTime 等待时间
     * @param unit     时间单位
     * @return 加锁成功返回 true，超时返回 false
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException;

    /**
     * 尝试加锁，指定等待时间和锁的过期时间（阻塞）。
     * 在指定等待时间内尝试加锁，如果成功则设置锁的过期时间。
     *
     * @param waitTime  等待时间
     * @param leaseTime 锁的过期时间（自动释放）
     * @param unit      时间单位
     * @return 加锁成功返回 true，超时返回 false
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 加锁（阻塞）。
     * 一直等待直到获得锁为止。
     */
    void lock();

    /**
     * 加锁，指定锁的过期时间（阻塞）。
     * 一直等待直到获得锁为止，获得锁后设置锁的过期时间。
     *
     * @param leaseTime 锁的过期时间（自动释放）
     * @param unit      时间单位
     */
    void lock(long leaseTime, TimeUnit unit);

    /**
     * 释放锁。
     * 只有持有该锁的线程才能释放锁。
     *
     * @return 释放成功返回 true，失败返回 false
     */
    boolean unlock();

    /**
     * 判断是否已加锁。
     * 检查当前锁是否被某个线程持有。
     *
     * @return 已加锁返回 true，未加锁返回 false
     */
    boolean isLocked();

    /**
     * 判断当前线程是否持有该锁。
     * 检查当前执行线程是否是该锁的持有者。
     *
     * @return 当前线程持有该锁返回 true，否则返回 false
     */
    boolean isHeldByCurrentThread();

    /**
     * 获取锁的名称。
     *
     * @return 锁的名称
     */
    String getLockName();
}
