package com.example.a21516.ceshi_jiguang.famework.greendao.model;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 描述：基础类
 */
public class User {
    //autoincrement = true 主键会自增
    @Id(autoincrement = true)
    private Long id;

    //@Generated   编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
    @Generated(hash = 1248599927)
    public User(Long id){
        this.id=id;
    }

    @Generated(hash = 596692638)
    public User(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
