package com.im.common.mq.impl;
import com.alibaba.fastjson.JSON;
import com.im.common.domain.model.RocketMQData;
import com.im.common.mq.MessageSender;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 基于 RocketMQ 的消息发送实现类。
 * 实现 MessageSender 接口，提供普通消息和事务消息的发送功能。
 * 使用 RocketMQTemplate 与 RocketMQ 进行交互。
 */
@Component
public class RocketMQMessageSender implements MessageSender<Object> {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送普通消息。
     * 异步发送消息到 RocketMQ，消息会被立即投递到消息队列。
     *
     * @param rocketMQData 包含 Topic 和消息数据的 RocketMQData 对象
     * @return 发送是否成功，成功返回 true，失败返回 false
     * @throws IllegalArgumentException 如果 rocketMQData 或其属性为 null
     */
    @Override
    public boolean send(RocketMQData<Object> rocketMQData) {
        // 参数校验
        if (rocketMQData == null) {
            throw new IllegalArgumentException("RocketMQData cannot be null");
        }
        if (!StringUtils.hasText(rocketMQData.getTopic())) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        if (rocketMQData.getData() == null) {
            throw new IllegalArgumentException("Message data cannot be null");
        }

        try {
            // 构造消息
            Message<String> message = getMessage(rocketMQData);

            // 发送消息到 RocketMQ
            rocketMQTemplate.convertAndSend(rocketMQData.getTopic(), message);

            return true;
        } catch (Exception e) {
            // 记录异常日志
            System.err.println("Failed to send message to topic: " + rocketMQData.getTopic() + ", error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送事务消息。
     * 发送事务消息到 RocketMQ，确保消息与本地事务的一致性。
     * 事务消息需要实现本地事务执行和回查逻辑。
     *
     * @param rocketMQData 包含 Topic 和消息数据的 RocketMQData 对象
     * @return 发送是否成功，成功返回 true，失败返回 false
     * @throws IllegalArgumentException 如果 rocketMQData 或其属性为 null
     */
    @Override
    public boolean sendMessageInTransaction(RocketMQData<Object> rocketMQData) {
        // 参数校验
        if (rocketMQData == null) {
            throw new IllegalArgumentException("RocketMQData cannot be null");
        }
        if (!StringUtils.hasText(rocketMQData.getTopic())) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        if (rocketMQData.getData() == null) {
            throw new IllegalArgumentException("Message data cannot be null");
        }

        try {
            // 构造消息
            Message<String> message = getMessage(rocketMQData);

            // 发送事务消息到 RocketMQ
            // 注意：事务消息需要在应用中实现 RocketMQLocalTransactionListener 接口
            // 来处理本地事务执行和回查逻辑
            rocketMQTemplate.sendMessageInTransaction(rocketMQData.getTopic(), message, null);

            return true;
        } catch (Exception e) {
            // 记录异常日志
            System.err.println("Failed to send transaction message to topic: " + rocketMQData.getTopic() + ", error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 构造向 RocketMQ 中真正发送的消息数据。
     * 将 RocketMQData 对象转换为 Spring Message 对象，用于发送到 RocketMQ。
     *
     * @param rocketMQData 包含 Topic 和消息数据的 RocketMQData 对象
     * @return Spring Message 对象，包含序列化后的消息内容
     */
    private Message<String> getMessage(RocketMQData<Object> rocketMQData) {
        // 将消息数据序列化为 JSON 字符串
        String messageContent = JSON.toJSONString(rocketMQData.getData());

        // 使用 MessageBuilder 构造 Spring Message 对象
        // 可以在这里添加消息头、标签等信息
        Message<String> message = MessageBuilder
                .withPayload(messageContent)
                // 可选：添加消息头信息
                .setHeader("topic", rocketMQData.getTopic())
                .setHeader("timestamp", System.currentTimeMillis())
                .build();

        return message;
    }
}
