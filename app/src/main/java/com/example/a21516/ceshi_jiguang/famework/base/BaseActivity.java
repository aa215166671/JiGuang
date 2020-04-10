package com.example.a21516.ceshi_jiguang.famework.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.blankj.utilcode.util.BarUtils;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.activity.LoginActivity;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.example.a21516.ceshi_jiguang.famework.system.SystemStatusManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;

import static cn.jpush.im.android.api.event.LoginStateChangeEvent.Reason.user_logout;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    //记录处于前台的Activity
    private static BaseActivity mForegroundActivity=null;

    //记录所有活动Activity
    private static final List<BaseActivity> mActivities=new LinkedList<BaseActivity>();
    Context mContext;
    //加载中
    private ProgressDialog progressDialog;
    SharedPrefHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // : 设置导航栏沉浸式 会显示出来
        BarUtils.setNavBarImmersive(this);


        //进行判断界面是否加载出来。没有返回没有数据界面
        setContentView(rootContentView());
        ButterKnife.bind(this);
        new SystemStatusManager(this).setTranslucentStatus(R.drawable.shape_titlebar);
        JMessageClient.registerEventReceiver(this);
        mContext = BaseActivity.this;
        helper=SharedPrefHelper.getInstance();
        initView();
        initData();

    }

    @Override
    public void onClick(View view) {
    }

    public void onEventMainThread(LoginStateChangeEvent event){
        final LoginStateChangeEvent.Reason reason=event.getReason();
        if (reason== user_logout){
            showLondToast(this,"该账号在其他设备登录，被强制下线");
            JMessageClient.logout();
            helper.setUserPW("");
            helper.setNakeName("");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    //销毁Activity 释放资源
    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }
    // 返回界面布局 如果没有加载界面出来就是无数据界面
    protected abstract int setContentView();


    //初始化页面布局
    protected abstract void initView();

    //初始化数据 抽象方法要重写
    protected abstract void initData();





    /**
     * 处理点击事件
     */
    protected void processClick(View v) {

    }



    //获取根部View  如果没有加载界面出来就是无数据界面
    public View rootContentView(){

        if (setContentView()!=0){
            return View.inflate(this,setContentView(),null);
        }else{
            return View.inflate(this, R.layout.page_default,null);
        }
    }

    @Override
    protected void onResume() {
        mForegroundActivity=this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mForegroundActivity=null;
        super.onPause();
    }


    //返回错误
    protected void showPhoneErr(){
        new SVProgressHUD(this).showErrorWithStatus("格式错误",
                SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
    }

    /***
     * 关软件盘
     * @param activity
     */
    protected void dismissKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.hideSoftInputFromWindow(activity
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示软键盘
     * @param v
     */
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 关闭所有activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    /**
     * 短时间Toast提示
     * @param activity
     * @param s
     */
    public void showToast(Activity activity,String s){

       // Toast.makeText(activity,s,Toast.LENGTH_SHORT).show();

        Toast toast=new Toast(activity);
        TextView view = new TextView(activity);
        view.setBackgroundResource(android.R.color.holo_green_light);
        view.setTextColor(Color.RED);
        view.setText(s);
        view.setPadding(10, 10, 10, 10);
        toast.setGravity(Gravity.CENTER, 0, 40);
        toast.setView(view);
        toast.show();


    }

    /**
     * 短时间自定义的Toast提示 可居中
     * @param activity
     * @param s
     */
    public void ShowCustomToast(Activity activity,String s){

        Toast toast = Toast.makeText(activity, s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    /**
     * 短时间带图片的Toast提示
     * @param activity
     * @param s
     */
    public void showImageToast(Activity activity,String s,int image,int duration ){


        Toast toast = Toast.makeText(activity, s, Toast.LENGTH_LONG);

        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);

        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageCodeProject = new ImageView(activity);

        imageCodeProject.setImageResource(image);

        toastView.addView(imageCodeProject, 0);

        toast.show();

    }

    /**
     * 短时间Toast提示
     * @param activity
     * @param s
     */
    public void showLondToast(Activity activity,String s){
        Toast.makeText(activity,"s",Toast.LENGTH_LONG).show();

    }

    /*加载中的进度条*/
    public void showProgressDialog(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog=null;
        }
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage("正在加载.....");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        try{
            progressDialog.show();
        }catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        }
    }
    /*自定义消息加载进度条*/
    public void showProgressDialog(String s){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog=null;
        }
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage(s);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        try{
            progressDialog.show();
        }catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        }
    }

    /**
     * 隐藏正在加载的进度条
     *
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
    }


    /*返回上下文*/
    public Context getContext() {
        return mContext;
    }


}
