package com.example.a21516.ceshi_jiguang.famework.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.example.a21516.ceshi_jiguang.famework.greendao.DaoMaster;
import com.example.a21516.ceshi_jiguang.famework.helper.MySQLiteOpenHelper;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

public class BaseApplication extends Application{
    public static BaseApplication baseApplication;
    private SharedPrefHelper sharedPrefHelper;
    private Context mContext;
    public MySQLiteOpenHelper helper;
    private DaoMaster master;
    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication=this;
        //install里主要做了一些判断，判断VM是否支持多dex  需要导入multidex包
        MultiDex.install(this);
        sharedPrefHelper=SharedPrefHelper.getInstance();
        //设置漫游开启
        sharedPrefHelper.setRoaming(true);
        //开启极光调试
        JPushInterface.setDebugMode(true);
        mContext=BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM，并自动同步聊天记录
        JMessageClient.init(getApplicationContext(),true);
        JMessageClient.setDebugMode(true);
        //初始化极光sms
//       SMSSDK.getInstance().initSdk(mContext);
        //初始化数据库
        setupDatabase();
        //通知管理，通知栏开启，其他关闭
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
        //初始化utils 工具类 需要引包
        Utils.init(this);
        //推送状态
        initJPush2();
        //初始化统计
        JAnalyticsInterface.init(mContext);
        //设置调试模式为true
        JAnalyticsInterface.setDebugMode(true);

    }

    private void initJPush2() {
        sharedPrefHelper.setMusic(false);
        sharedPrefHelper.setVib(false);
        sharedPrefHelper.setAppKey("b26196a33a5077d0469a6187");
    }

    //初始化数据库
    private void setupDatabase() {
        //是否开启调试
        MigrationHelper.DEBUG=true;
        QueryBuilder.LOG_SQL=true;
        QueryBuilder.LOG_VALUES=true;
        //数据库升级
        helper=new MySQLiteOpenHelper(mContext,"text");
        master=new DaoMaster(helper.getWritableDb());
    }
}
