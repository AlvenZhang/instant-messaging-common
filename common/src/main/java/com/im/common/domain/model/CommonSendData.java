package com.im.common.domain.model;

import com.im.common.domain.enums.SendMessageType;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用发送数据模型。
 * 用于即时通讯服务向客户端响应登录结果、发送心跳数据、单聊消息、群聊消息等。
 *
 * @param <T> 具体的数据载体类型
 */
public class CommonSendData<T> implements Serializable {
    /** 发送消息指令类型 */
    private SendMessageType commandType;
    /** 具体的数据载体 */
    private T data;

    public CommonSendData() {
    }

    public CommonSendData(SendMessageType commandType, T data) {
        this.commandType = commandType;
        this.data = data;
    }

    public SendMessageType getCommandType() {
        return commandType;
    }

    public void setCommandType(SendMessageType commandType) {
        this.commandType = commandType;
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
        CommonSendData<?> that = (CommonSendData<?>) o;
        return commandType == that.commandType && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, data);
    }

    @Override
    public String toString() {
        return "CommonSendData{" +
                "commandType=" + commandType +
                ", data=" + data +
                '}';
    }
}