package com.im.common.domain.model;

import com.im.common.domain.enums.TerminalType;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户信息模型。
 * 发送消息的用户（或消息发送者），包含用户唯一标识与终端类型。
 */
public class UserInfo implements Serializable {
    /** 用户唯一标识 */
    private String userId;
    /** 用户终端类型 */
    private TerminalType terminal;

    public UserInfo() {
    }

    public UserInfo(String userId, TerminalType terminal) {
        this.userId = userId;
        this.terminal = terminal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TerminalType getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalType terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(userId, userInfo.userId) && terminal == userInfo.terminal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, terminal);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", terminal=" + terminal +
                '}';
    }
}