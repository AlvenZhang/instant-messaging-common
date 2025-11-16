package com.im.common.domain.enums;

/**
 * 发送消息的类型枚举。
 * 包含：登录、心跳、强制下线、私聊消息和群聊消息
 */
public enum SendMessageType {
    /** 登录 */
    LOGIN(0, "登录"),
    /** 心跳 */
    HEARTBEAT(1, "心跳"),
    /** 强制下线 */
    FORCE_LOGOUT(2, "强制下线"),
    /** 私聊消息 */
    PRIVATE_CHAT(3, "私聊消息"),
    /** 群聊消息 */
    GROUP_CHAT(4, "群聊消息");

    private final int code;
    private final String desc;

    SendMessageType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}