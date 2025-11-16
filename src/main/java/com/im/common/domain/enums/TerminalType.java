package com.im.common.domain.enums;

/**
 * 发送和接收消息的终端类型枚举。
 * 包含 Web 端与 App 端
 */
public enum TerminalType {
    /** Web 端 */
    WEB(0, "Web端"),
    /** App 端 */
    APP(1, "App端");

    private final int code;
    private final String desc;

    TerminalType(int code, String desc) {
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