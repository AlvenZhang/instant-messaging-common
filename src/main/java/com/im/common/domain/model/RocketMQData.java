package com.im.common.domain.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * RocketMQ 数据模型。
 * 用于向 RocketMQ 发送数据和接收 RocketMQ 中的数据，包含消息的 Topic 与数据载体。
 *
 * @param <T> 具体的数据载体类型
 */
public class RocketMQData<T> implements Serializable {
    /** RocketMQ Topic */
    private String topic;
    /** 具体的数据载体 */
    private T data;

    public RocketMQData() {
    }

    public RocketMQData(String topic, T data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketMQData<?> that = (RocketMQData<?>) o;
        return Objects.equals(topic, that.topic) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, data);
    }

    @Override
    public String toString() {
        return "RocketMQData{" +
                "topic='" + topic + '\'' +
                ", data=" + data +
                '}';
    }
}