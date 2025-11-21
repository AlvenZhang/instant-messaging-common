package com.im.common.domain.model;

import com.im.common.domain.enums.TerminalType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Session 信息模型。
 * 用户登录系统之后保存的会话信息，主要包含用户唯一标识与终端类型。
 */
public class SessionInfo implements Serializable {
    /** 用户唯一标识 */
    private Long userId;
    /** 用户终端类型 */
    private TerminalType terminal;

    public SessionInfo() {
    }

    public SessionInfo(Long userId, TerminalType terminal) {
        this.userId = userId;
        this.terminal = terminal;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
        SessionInfo that = (SessionInfo) o;
        return Objects.equals(userId, that.userId) && terminal == that.terminal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, terminal);
    }

    @Override
    public String toString() {
        return "SessionInfo{" +
                "userId='" + userId + '\'' +
                ", terminal=" + terminal +
                '}';
    }
}