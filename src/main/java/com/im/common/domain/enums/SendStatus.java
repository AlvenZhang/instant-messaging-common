package com.im.common.domain.enums;

/**
 * 发送消息的状态枚举。
 * 包含：发送成功、对方当前不在线、未找到对方的Channel、未知异常
 */
public enum SendStatus {
    /** 发送成功 */
    SUCCESS(0, "发送成功"),
    /** 对方当前不在线 */
    PEER_OFFLINE(1, "对方当前不在线"),
    /** 未找到对方的 Channel */
    CHANNEL_NOT_FOUND(2, "未找到对方的Channel"),
    /** 未知异常 */
    UNKNOWN_ERROR(3, "未知异常");

    private final int code;
    private final String desc;

    SendStatus(int code, String desc) {
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