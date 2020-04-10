package com.example.a21516.ceshi_jiguang.entity;

import java.util.List;

public class UserStateListBean {

    /**
     * devices : [{"login":false,"online":false,"platform":"a"}]
     * username : Rauly
     */

    public String username;
    public List<DevicesBean> devices;

    public static class DevicesBean {
        /**
         * login : false
         * online : false
         * platform : a
         */

        public boolean login;
        public boolean online;
        public String platform;
    }
}
