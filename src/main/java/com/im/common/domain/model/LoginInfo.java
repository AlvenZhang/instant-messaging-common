package com.im.common.domain.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 登录信息模型。
 * 包含用户登录的 Token 信息，登录系统时会根据此 Token 进行校验。
 */
public class LoginInfo implements Serializable {
    /** 用户登录的 Token */
    private String token;

    public LoginInfo() {
    }

    public LoginInfo(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginInfo loginInfo = (LoginInfo) o;
        return Objects.equals(token, loginInfo.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "token='" + token + '\'' +
                '}';
    }
}