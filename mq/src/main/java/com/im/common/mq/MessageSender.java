package com.im.common.mq;
import com.im.common.domain.model.RocketMQData;

/**
 * 通用消息发送接口。
 * 定义消息发送的基本操作，支持普通消息和事务消息的发送。
 * 适用于基于 RocketMQ 的消息队列系统。
 *
 * @param <T> 消息数据的泛型类型
 */
public interface MessageSender<T> {

    /**
     * 发送普通消息。
     * 异步发送消息到 RocketMQ，不保证消息一定被消费。
     *
     * @param rocketMQData 包含 Topic 和消息数据的 RocketMQData 对象
     * @return 发送是否成功，成功返回 true，失败返回 false
     * @throws IllegalArgumentException 如果 rocketMQData 或其属性为 null
     */
    boolean send(RocketMQData<T> rocketMQData);

    /**
     * 发送事务消息。
     * 发送事务消息到 RocketMQ，确保消息与本地事务的一致性。
     * 事务消息需要实现本地事务执行和回查逻辑。
     *
     * @param rocketMQData 包含 Topic 和消息数据的 RocketMQData 对象
     * @return 发送是否成功，成功返回 true，失败返回 false
     * @throws IllegalArgumentException 如果 rocketMQData 或其属性为 null
     */
    boolean sendMessageInTransaction(RocketMQData<T> rocketMQData);
}
