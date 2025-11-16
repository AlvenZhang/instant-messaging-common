package com.im.common.domain.model;

import com.im.common.domain.enums.TerminalType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 群聊数据模型。
 * 用户在系统中发送的群聊消息。
 * 包含发送者信息、接收者 id 列表、接收者终端类型、是否将消息发送给自己的其他终端、是否回推发送结果数据、消息内容。
 */
public class GroupChatData implements Serializable {
    /** 消息发送者信息 */
    private UserInfo sender;
    /** 消息接收者唯一标识列表 */
    private List<String> receiverIds;
    /** 接收者的终端类型 */
    private TerminalType receiverTerminalType;
    /** 是否发送到自己其他终端 */
    private boolean sendToSelfOtherTerminals;
    /** 是否回推发送结果数据 */
    private boolean pushSendResult;
    /** 消息内容 */
    private String content;

    public GroupChatData() {
    }

    public GroupChatData(UserInfo sender, List<String> receiverIds, TerminalType receiverTerminalType, boolean sendToSelfOtherTerminals, boolean pushSendResult, String content) {
        this.sender = sender;
        this.receiverIds = receiverIds;
        this.receiverTerminalType = receiverTerminalType;
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

    public List<String> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<String> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public TerminalType getReceiverTerminalType() {
        return receiverTerminalType;
    }

    public void setReceiverTerminalType(TerminalType receiverTerminalType) {
        this.receiverTerminalType = receiverTerminalType;
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
        GroupChatData that = (GroupChatData) o;
        return sendToSelfOtherTerminals == that.sendToSelfOtherTerminals && pushSendResult == that.pushSendResult && Objects.equals(sender, that.sender) && Objects.equals(receiverIds, that.receiverIds) && receiverTerminalType == that.receiverTerminalType && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiverIds, receiverTerminalType, sendToSelfOtherTerminals, pushSendResult, content);
    }

    @Override
    public String toString() {
        return "GroupChatData{" +
                "sender=" + sender +
                ", receiverIds=" + receiverIds +
                ", receiverTerminalType=" + receiverTerminalType +
                ", sendToSelfOtherTerminals=" + sendToSelfOtherTerminals +
                ", pushSendResult=" + pushSendResult +
                ", content='" + content + '\'' +
                '}';
    }
}