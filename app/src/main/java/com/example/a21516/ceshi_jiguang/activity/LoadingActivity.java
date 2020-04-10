package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.a21516.ceshi_jiguang.MainActivity;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class LoadingActivity extends AppCompatActivity  {
    private SharedPrefHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //取消状态栏还有导航栏的显示
        BarUtils.setNotificationBarVisibility(false);
        BarUtils.setStatusBarVisibility(this,false);
        //实现自动登录
        initView();
    }

    private void initView() {
        //初始化数据库
        helper=SharedPrefHelper.getInstance();
        final Handler handler=new Handler();

        //记录多少人在线
        /**
         * getCanonicalName() 是获取所传类从java语言规范定义的格式输出。
         * getName() 是返回实体类型名称
         * getSimpleName() 返回从源代码中返回实例的名称。
         * */
        JAnalyticsInterface.onPageStart(this,this.getClass().getCanonicalName());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (helper.getUserPW().equals("")){
                    Intent intent=new Intent(LoadingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    JMessageClient.login(helper.getUserId(), helper.getUserPW(), new BasicCallback() {
                        @Override

                        public void gotResult(int i, String s) {
                            Log.i("账号", "gotResult: "+ helper.getUserId());
                            if (i==0){
                                //获取用户信息
                                initUserInfo();
                                ToastUtils.showShort("登录成功");
                                Intent intent=new Intent(getApplication(), MainActivity.class);
                                //意思应该是把主页面放在最前面的类栈里面
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                LoadingActivity.this.finish();
                            }else {
                                startActivity(new Intent(LoadingActivity.this,LoginActivity.class));
                                ToastUtils.showShort("登陆失败:"+s);
                            }
                        }
                    });
                }
            }
        },3000);
    }

    //关闭Activity 释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭页面结束记录
        JAnalyticsInterface.onPageEnd(this,this.getClass().getCanonicalName());
    }

    public void initUserInfo() {
        JMessageClient.getUserInfo(helper.getUserId(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.i("用户名", "gotResult: "+userInfo.getNickname()+"//////"+userInfo.getUserName());
                if (i==0) {
                    helper.setNakeName(userInfo.getNickname());
                    helper.setUserId(userInfo.getUserName());
                }
            }
        });
    }
}
