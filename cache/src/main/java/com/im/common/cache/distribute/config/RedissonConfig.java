package com.im.common.cache.distribute.config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Redisson 配置类。
 * 提供 Redisson 客户端配置，支持单机、集群、哨兵等多种部署模式。
 * 配置项：cache.distributed.redisson.enabled=true 时启用此配置。
 */
@Configuration
@ConditionalOnProperty(name = "cache.distributed.redisson.enabled", havingValue = "true", matchIfMissing = false)
public class RedissonConfig {

    @Value("${spring.redis.host:localhost}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    @Value("${spring.redis.timeout:3000}")
    private int timeout;

    @Value("${spring.redis.redisson.connection-pool-size:64}")
    private int connectionPoolSize;

    @Value("${spring.redis.redisson.connection-minimum-idle-size:10}")
    private int connectionMinimumIdleSize;

    /**
     * 配置 Redisson 客户端（单机模式）。
     *
     * @return RedissonClient 实例
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 单机模式配置
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);

        // 如果配置了密码，则设置密码
        if (StringUtils.hasText(password)) {
            singleServerConfig.setPassword(password);
        }

        return Redisson.create(config);
    }

    /**
     * 配置 Redisson 客户端（集群模式）。
     * 需要配置 spring.redis.cluster.nodes 属性。
     *
     * @param clusterNodes 集群节点列表，格式：host1:port1,host2:port2,...
     * @return RedissonClient 实例
     */
    // @Bean(destroyMethod = "shutdown")
    // @ConditionalOnProperty(name = "spring.redis.cluster.nodes")
    public RedissonClient redissonClusterClient(
            @Value("${spring.redis.cluster.nodes:}") String clusterNodes) {
        Config config = new Config();

        String[] nodes = clusterNodes.split(",");
        String[] addresses = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            addresses[i] = "redis://" + nodes[i].trim();
        }

        config.useClusterServers()
                .addNodeAddress(addresses)
                .setTimeout(timeout)
                .setMasterConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize);

        // 如果配置了密码，则设置密码
        if (StringUtils.hasText(password)) {
            config.useClusterServers().setPassword(password);
        }

        return Redisson.create(config);
    }

    /**
     * 配置 Redisson 客户端（哨兵模式）。
     * 需要配置 spring.redis.sentinel.master 和 spring.redis.sentinel.nodes 属性。
     *
     * @param masterName   哨兵主节点名称
     * @param sentinelNodes 哨兵节点列表，格式：host1:port1,host2:port2,...
     * @return RedissonClient 实例
     */
    // @Bean(destroyMethod = "shutdown")
    // @ConditionalOnProperty(name = "spring.redis.sentinel.master")
    public RedissonClient redissonSentinelClient(
            @Value("${spring.redis.sentinel.master:}") String masterName,
            @Value("${spring.redis.sentinel.nodes:}") String sentinelNodes) {
        Config config = new Config();

        String[] nodes = sentinelNodes.split(",");
        String[] addresses = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            addresses[i] = "redis://" + nodes[i].trim();
        }

        config.useSentinelServers()
                .setMasterName(masterName)
                .addSentinelAddress(addresses)
                .setDatabase(database)
                .setTimeout(timeout)
                .setMasterConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize);

        // 如果配置了密码，则设置密码
        if (StringUtils.hasText(password)) {
            config.useSentinelServers().setPassword(password);
        }

        return Redisson.create(config);
    }
}
