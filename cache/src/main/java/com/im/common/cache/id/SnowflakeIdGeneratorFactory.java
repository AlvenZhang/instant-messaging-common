package com.im.common.cache.id;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法 ID 生成器工厂配置类。
 * 根据配置文件创建 SnowflakeIdGenerator 实例。
 * 配置项：id.generator.type=snowflake 时启用此配置。
 */
@Configuration
@ConditionalOnProperty(name = "id.generator.type", havingValue = "snowflake", matchIfMissing = false)
public class SnowflakeIdGeneratorFactory {

    @Value("${id.generator.snowflake.datacenter-id:0}")
    private long datacenterId;

    @Value("${id.generator.snowflake.worker-id:0}")
    private long workerId;

    /**
     * 创建雪花算法 ID 生成器实例。
     * 
     * @return SnowflakeIdGenerator 实例
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(datacenterId, workerId);
    }
}
