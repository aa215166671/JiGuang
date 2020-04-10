package com.example.a21516.ceshi_jiguang.famework.helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.a21516.ceshi_jiguang.famework.greendao.ChatLogDao;
import com.example.a21516.ceshi_jiguang.famework.greendao.DaoMaster;
import com.example.a21516.ceshi_jiguang.famework.greendao.RequestListDao;
import com.example.a21516.ceshi_jiguang.famework.greendao.UserDao;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.RequestList;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper{
    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
       //设置需要升级的表
        MigrationHelper.migrate(db, ChatLogDao.class, UserDao.class, RequestListDao.class);
    }
}
