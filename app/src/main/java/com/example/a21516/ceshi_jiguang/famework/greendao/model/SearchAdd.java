package com.example.a21516.ceshi_jiguang.famework.greendao.model;

import com.example.a21516.ceshi_jiguang.famework.greendao.SearchAddDao;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/*
* 搜索好友历史
* */
public class SearchAdd {


    @Id(autoincrement = true)
    private Long id;
    private String content;

    @Generated(hash = 129942465)
    public SearchAdd(Long id, String content){
        this.id=id;
        this.content=content;
    }
    public SearchAdd(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
