package com.ycychat.model;

import java.io.Serializable;

/**
 * 用户实体，用于封装登录时提交的用户名与密码。
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}