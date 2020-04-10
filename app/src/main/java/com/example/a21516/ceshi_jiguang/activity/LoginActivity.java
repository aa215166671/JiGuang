package com.example.a21516.ceshi_jiguang.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.MainActivity;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.LoginEvent;
import cn.jiguang.analytics.android.api.RegisterEvent;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_passWord)
    EditText loginPassWord;
    @BindView(R.id.login_code)
    EditText loginCode;
    @BindView(R.id.login_code_bt)
    Button loginCodeBt;
    @BindView(R.id.login_submit)
    Button loginSubmit;
    @BindView(R.id.login_ok)
    Button loginOk;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;

private int time =60;
SharedPrefHelper sharedPrefHelper;
private UserInfo userInfo;

Handler handler=new Handler();

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        sharedPrefHelper=SharedPrefHelper.getInstance();
        //如果有保存的用户id就显示在TextView上
        if (!sharedPrefHelper.getUserId().equals("")){
            loginUsername.setText(sharedPrefHelper.getUserId());
        }
        mTitleBarBack.setVisibility(View.GONE);
        mTitleOptionsImg.setVisibility(View.GONE);
        mTitleBarTitle.setText("注册登录");

    }


    @OnClick({R.id.login_code_bt,R.id.login_submit,R.id.login_ok})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.login_code_bt:
                break;
                //注册
            case R.id.login_submit:
                JMessageClient.register(loginUsername.getText().toString(), loginPassWord.getText().toString(), new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("ceshi", i + "，" + s);
                        switch (i){

                            case 0:
                                showToast(LoginActivity.this,"注册成功");
                                initLogin(loginUsername.getText().toString(),loginPassWord.getText().toString(),1);
                                //极光统计用户数
                                RegisterEvent event=new RegisterEvent("userName",true);
                                JAnalyticsInterface.onEvent(getContext(),event);
                                break;
                            case 898001:
                                showToast(LoginActivity.this, "用户名已存在");
                                break;
                            case 871301:
                                showToast(LoginActivity.this, "密码格式错误");
                                break;
                            case 871304:
                                showToast(LoginActivity.this, "密码错误");
                                break;
                            default:
                                showToast(LoginActivity.this, s);
                                break;
                        }
                    }
                });
                break;
                //直接登录
            case R.id.login_ok:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog("正在登录");
                        initLogin(loginUsername.getText().toString(),loginPassWord.getText().toString(),0);
                    }
                },500);
                break;
                default:
                    break;
        }
    }

    /**
     * 登录
     * @param userName
     * @param passWord
     */
    private void initLogin(String userName,String passWord,final  int type) {
        Log.i("type值", "initLogin: "+type);
        showProgressDialog();
        JMessageClient.login(userName, passWord, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                dismissProgressDialog();
                switch (i){
                    case 801003:
                        showToast(LoginActivity.this,"用户名不存在");
                        break;
                    case 871301 :
                        showToast(LoginActivity.this, "密码格式错误");
                        break;
                    case 801004:
                        showToast(LoginActivity.this, "密码错误");
                        handler.sendEmptyMessage(-1);
                        break;
                    case 0:
                        showToast(LoginActivity.this, "登陆成功");
                        //保存用户名还有密码
                        sharedPrefHelper.setUserId(loginUsername.getText().toString());
                        sharedPrefHelper.setUserPW(loginPassWord.getText().toString());
                        //初始化用户数据
                        initUserInfo(loginUsername.getText().toString(),type);
                        //登录成功计数
                        LoginEvent event = new LoginEvent("userName", true);
                        JAnalyticsInterface.onEvent(getContext(),event);
                        break;
                }
            }
        });
    }

    //初始化用户数据
    public void initUserInfo(String id,final int type ) {
        showProgressDialog("正在初始化数据...");
        JMessageClient.getUserInfo(id, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                dismissProgressDialog();
                if (i==0){
                    Log.e("info-Login", ""+JMessageClient.getMyInfo()+"\n"+JMessageClient.getConversationList()+"\n"+userInfo);
                    Intent intent = new Intent(LoginActivity.this
                            , MainActivity.class);
                    intent.putExtra("LOGINTYPE", type);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}
