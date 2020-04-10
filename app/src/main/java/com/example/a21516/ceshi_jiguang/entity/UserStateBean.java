package com.example.a21516.ceshi_jiguang.entity;

public class UserStateBean {
    //判断是否在线

    /**
     * login : true
     * online : false
     */

    public boolean login;
    public boolean online;

    @Override
    public String toString() {
        return "UserStateBean{" +
                "login=" + login +
                ", online=" + online +
                '}';
    }
}
