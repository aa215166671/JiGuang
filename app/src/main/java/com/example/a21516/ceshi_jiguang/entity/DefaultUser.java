package com.example.a21516.ceshi_jiguang.entity;

import cn.jiguang.imui.commons.models.IUser;

public class DefaultUser implements IUser {
    public String id;
    private String displayName;
    private  String avatar;

    public DefaultUser(String id,String displayName,String avatar){
        this.id=id;
        this.displayName=displayName;
        this.avatar=avatar;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }
}
