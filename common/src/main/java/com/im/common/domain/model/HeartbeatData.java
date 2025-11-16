package com.im.common.domain.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 心跳数据模型。
 * 客户端与即时通讯系统之间的心跳机制载体，用于维持连接与在线状态。
 */
public class HeartbeatData implements Serializable {
    /** 发送心跳的用户唯一标识 */
    private String userId;
    /** 心跳时间戳（毫秒） */
    private long timestamp;

    public HeartbeatData() {
    }

    public HeartbeatData(String userId, long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeartbeatData that = (HeartbeatData) o;
        return timestamp == that.timestamp && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, timestamp);
    }

    @Override
    public String toString() {
        return "HeartbeatData{" +
                "userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}