package com.im.common.cache.id;
/**
 * 雪花算法（Snowflake）ID 生成器。
 * 
 * <p>雪花算法是 Twitter 开源的分布式 ID 生成算法，生成的 ID 是一个 64 位的 long 型数字。</p>
 * 
 * <p>ID 结构（64 位）：</p>
 * <pre>
 * +--------------------------------------------------------------------------+
 * | 1 Bit 未使用 | 41 Bit 时间戳 | 10 Bit 工作机器ID | 12 Bit 序列号 |
 * +--------------------------------------------------------------------------+
 * </pre>
 * 
 * <ul>
 *   <li>1 位：符号位，始终为 0，保证生成的 ID 为正数</li>
 *   <li>41 位：时间戳（毫秒级），可以使用约 69 年</li>
 *   <li>10 位：工作机器 ID（5 位数据中心 ID + 5 位机器 ID），支持 1024 个节点</li>
 *   <li>12 位：序列号，同一毫秒内可生成 4096 个不同 ID</li>
 * </ul>
 * 
 * <p>特点：</p>
 * <ul>
 *   <li>全局唯一：在分布式环境下保证 ID 唯一性</li>
 *   <li>趋势递增：ID 按时间递增，有利于数据库索引</li>
 *   <li>高性能：本地生成，无需网络调用</li>
 *   <li>高可用：不依赖第三方系统</li>
 * </ul>
 * 
 * <p>线程安全：使用 synchronized 关键字保证线程安全。</p>
 * 
 * @author IM Team
 * @since 1.0
 */
public class SnowflakeIdGenerator {

    /**
     * 起始时间戳（2024-01-01 00:00:00）。
     * 可以根据实际业务调整，建议设置为项目开始时间。
     */
    private static final long START_TIMESTAMP = 1704067200000L;

    /**
     * 数据中心 ID 所占的位数。
     */
    private static final long DATACENTER_ID_BITS = 5L;

    /**
     * 机器 ID 所占的位数。
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 序列号所占的位数。
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 数据中心 ID 的最大值（31）。
     */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    /**
     * 机器 ID 的最大值（31）。
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 序列号的最大值（4095）。
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器 ID 左移位数（12 位）。
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心 ID 左移位数（17 位）。
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数（22 位）。
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    /**
     * 数据中心 ID（0-31）。
     */
    private final long datacenterId;

    /**
     * 机器 ID（0-31）。
     */
    private final long workerId;

    /**
     * 序列号（0-4095）。
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间戳。
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数。
     * 
     * @param datacenterId 数据中心 ID（0-31）
     * @param workerId     机器 ID（0-31）
     * @throws IllegalArgumentException 如果数据中心 ID 或机器 ID 超出范围
     */
    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("Datacenter ID can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * 生成下一个 ID（线程安全）。
     * 
     * @return 生成的唯一 ID
     * @throws RuntimeException 如果时钟回拨
     */
    public synchronized long nextId() {
        long timestamp = getCurrentTimestamp();

        // 检查时钟回拨
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        // 同一毫秒内生成多个 ID
        if (timestamp == lastTimestamp) {
            // 序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出，等待下一毫秒
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置为 0
            sequence = 0L;
        }

        // 更新上次生成 ID 的时间戳
        lastTimestamp = timestamp;

        // 组装 ID
        // 时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 等待下一毫秒。
     * 
     * @param lastTimestamp 上次生成 ID 的时间戳
     * @return 下一毫秒的时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳（毫秒）。
     * 
     * @return 当前时间戳
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 解析 ID，获取 ID 的各个组成部分。
     * 
     * @param id 雪花算法生成的 ID
     * @return ID 信息对象
     */
    public static IdInfo parseId(long id) {
        long timestamp = (id >> TIMESTAMP_SHIFT) + START_TIMESTAMP;
        long datacenterId = (id >> DATACENTER_ID_SHIFT) & MAX_DATACENTER_ID;
        long workerId = (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;
        long sequence = id & MAX_SEQUENCE;

        return new IdInfo(timestamp, datacenterId, workerId, sequence);
    }

    /**
     * ID 信息类。
     * 用于存储解析后的 ID 各个组成部分。
     */
    public static class IdInfo {
        /**
         * 时间戳（毫秒）。
         */
        private final long timestamp;

        /**
         * 数据中心 ID。
         */
        private final long datacenterId;

        /**
         * 机器 ID。
         */
        private final long workerId;

        /**
         * 序列号。
         */
        private final long sequence;

        public IdInfo(long timestamp, long datacenterId, long workerId, long sequence) {
            this.timestamp = timestamp;
            this.datacenterId = datacenterId;
            this.workerId = workerId;
            this.sequence = sequence;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getDatacenterId() {
            return datacenterId;
        }

        public long getWorkerId() {
            return workerId;
        }

        public long getSequence() {
            return sequence;
        }

        @Override
        public String toString() {
            return "IdInfo{" +
                    "timestamp=" + timestamp +
                    ", datacenterId=" + datacenterId +
                    ", workerId=" + workerId +
                    ", sequence=" + sequence +
                    '}';
        }
    }

    /**
     * 获取数据中心 ID。
     * 
     * @return 数据中心 ID
     */
    public long getDatacenterId() {
        return datacenterId;
    }

    /**
     * 获取机器 ID。
     * 
     * @return 机器 ID
     */
    public long getWorkerId() {
        return workerId;
    }
}
