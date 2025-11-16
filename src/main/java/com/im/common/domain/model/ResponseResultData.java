package com.im.common.domain.model;

import com.im.common.domain.enums.SendMessageType;
import com.im.common.domain.enums.SendStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 响应结果数据模型。
 * 用于发送响应的结果数据，包含消息发送者、消息接收者、指令类型、发送状态、具体数据。
 *
 * @param <T> 具体的数据载体类型
 */
public class ResponseResultData<T> implements Serializable {
    /** 消息发送者信息 */
    private UserInfo sender;
    /** 消息接收者唯一标识列表 */
    private List<String> receiverIds;
    /** 指令类型 */
    private SendMessageType commandType;
    /** 发送状态 */
    private SendStatus status;
    /** 具体的数据载体 */
    private T data;

    public ResponseResultData() {
    }

    public ResponseResultData(UserInfo sender, List<String> receiverIds, SendMessageType commandType, SendStatus status, T data) {
        this.sender = sender;
        this.receiverIds = receiverIds;
        this.commandType = commandType;
        this.status = status;
        this.data = data;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public List<String> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<String> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public SendMessageType getCommandType() {
        return commandType;
    }

    public void setCommandType(SendMessageType commandType) {
        this.commandType = commandType;
    }

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
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
        ResponseResultData<?> that = (ResponseResultData<?>) o;
        return Objects.equals(sender, that.sender) && Objects.equals(receiverIds, that.receiverIds) && commandType == that.commandType && status == that.status && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiverIds, commandType, status, data);
    }

    @Override
    public String toString() {
        return "ResponseResultData{" +
                "sender=" + sender +
                ", receiverIds=" + receiverIds +
                ", commandType=" + commandType +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}