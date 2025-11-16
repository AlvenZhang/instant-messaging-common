package com.im.common.domain.model;

import com.im.common.domain.enums.TerminalType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 私聊数据模型。
 * 用户在系统中发送的单聊消息。
 * 包含消息发送者信息、消息接收者 id、接收者终端列表、是否将消息发送给自己的其他终端、是否回推发送结果数据、消息具体内容。
 */
public class PrivateChatData implements Serializable {
    /** 消息发送者信息 */
    private UserInfo sender;
    /** 消息接收者唯一标识 */
    private String receiverId;
    /** 接收者的终端列表 */
    private List<TerminalType> receiverTerminals;
    /** 是否发送到自己其他终端 */
    private boolean sendToSelfOtherTerminals;
    /** 是否回推发送结果数据 */
    private boolean pushSendResult;
    /** 消息内容 */
    private String content;

    public PrivateChatData() {
    }

    public PrivateChatData(UserInfo sender, String receiverId, List<TerminalType> receiverTerminals, boolean sendToSelfOtherTerminals, boolean pushSendResult, String content) {
        this.sender = sender;
        this.receiverId = receiverId;
        this.receiverTerminals = receiverTerminals;
        this.sendToSelfOtherTerminals = sendToSelfOtherTerminals;
        this.pushSendResult = pushSendResult;
        this.content = content;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public List<TerminalType> getReceiverTerminals() {
        return receiverTerminals;
    }

    public void setReceiverTerminals(List<TerminalType> receiverTerminals) {
        this.receiverTerminals = receiverTerminals;
    }

    public boolean isSendToSelfOtherTerminals() {
        return sendToSelfOtherTerminals;
    }

    public void setSendToSelfOtherTerminals(boolean sendToSelfOtherTerminals) {
        this.sendToSelfOtherTerminals = sendToSelfOtherTerminals;
    }

    public boolean isPushSendResult() {
        return pushSendResult;
    }

    public void setPushSendResult(boolean pushSendResult) {
        this.pushSendResult = pushSendResult;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateChatData that = (PrivateChatData) o;
        return sendToSelfOtherTerminals == that.sendToSelfOtherTerminals && pushSendResult == that.pushSendResult && Objects.equals(sender, that.sender) && Objects.equals(receiverId, that.receiverId) && Objects.equals(receiverTerminals, that.receiverTerminals) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiverId, receiverTerminals, sendToSelfOtherTerminals, pushSendResult, content);
    }

    @Override
    public String toString() {
        return "PrivateChatData{" +
                "sender=" + sender +
                ", receiverId='" + receiverId + '\'' +
                ", receiverTerminals=" + receiverTerminals +
                ", sendToSelfOtherTerminals=" + sendToSelfOtherTerminals +
                ", pushSendResult=" + pushSendResult +
                ", content='" + content + '\'' +
                '}';
    }
}