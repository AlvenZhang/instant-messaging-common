package com.im.common.domain.model;

import com.im.common.domain.enums.SendMessageType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 通用接收数据模型。
 * 大后端平台与即时通讯服务之间的数据交互载体。
 * 包含消息指令类型、消息发送者信息、消息接受者列表、是否需要回调发送结果、具体的数据。
 *
 * @param <T> 具体的数据载体类型
 */
public class CommonReceiveData<T> implements Serializable {
    /** 消息指令类型（与发送类型一致） */
    private SendMessageType commandType;
    /** 消息发送者信息 */
    private UserInfo sender;
    /** 消息接收者唯一标识列表 */
    private List<String> receiverIds;
    /** 是否需要回调发送结果 */
    private boolean needCallback;
    /** 具体的数据载体 */
    private T data;

    public CommonReceiveData() {
    }

    public CommonReceiveData(SendMessageType commandType, UserInfo sender, List<String> receiverIds, boolean needCallback, T data) {
        this.commandType = commandType;
        this.sender = sender;
        this.receiverIds = receiverIds;
        this.needCallback = needCallback;
        this.data = data;
    }

    public SendMessageType getCommandType() {
        return commandType;
    }

    public void setCommandType(SendMessageType commandType) {
        this.commandType = commandType;
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

    public boolean isNeedCallback() {
        return needCallback;
    }

    public void setNeedCallback(boolean needCallback) {
        this.needCallback = needCallback;
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
        CommonReceiveData<?> that = (CommonReceiveData<?>) o;
        return needCallback == that.needCallback && commandType == that.commandType && Objects.equals(sender, that.sender) && Objects.equals(receiverIds, that.receiverIds) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, sender, receiverIds, needCallback, data);
    }

    @Override
    public String toString() {
        return "CommonReceiveData{" +
                "commandType=" + commandType +
                ", sender=" + sender +
                ", receiverIds=" + receiverIds +
                ", needCallback=" + needCallback +
                ", data=" + data +
                '}';
    }
}