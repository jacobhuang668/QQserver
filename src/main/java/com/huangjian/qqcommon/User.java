package com.huangjian.qqcommon;

import java.io.Serializable;

public class User implements Serializable {


    private static final long serialVersionUID = 7532403726078192567L;
    private String password;
    private String userId;
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
