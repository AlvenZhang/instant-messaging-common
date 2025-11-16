package com.im.common.domain.enums;

/**
 * 监听消息的类型枚举。
 * 包含：全部消息、私聊消息和群聊消息
 */
public enum ListenMessageType {
    /** 全部消息 */
    ALL(0, "全部消息"),
    /** 私聊消息 */
    PRIVATE_CHAT(1, "私聊消息"),
    /** 群聊消息 */
    GROUP_CHAT(2, "群聊消息");

    private final int code;
    private final String desc;

    ListenMessageType(int code, String desc) {
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