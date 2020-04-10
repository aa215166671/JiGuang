package com.example.a21516.ceshi_jiguang.famework.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.a21516.ceshi_jiguang.famework.greendao.DaoMaster;
import com.example.a21516.ceshi_jiguang.famework.greendao.DaoSession;

/**
 *
 * 数据库初始化的辅助类
 */
public class GreenDaoHelper {
    Context context;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    public GreenDaoHelper(Context context){
        this.context=context;
    }

    public DaoSession initDao() {
        helper=new DaoMaster.DevOpenHelper(context,"text",null);
        db=helper.getWritableDatabase();
        daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
        return daoSession;
    }
}
