package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.base.BaseApplication;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.BufferedReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.IntegerCallback;
import cn.jpush.im.api.BasicCallback;

import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_SILENCE;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_SOUND;
import static cn.jpush.im.android.api.JMessageClient.FLAG_NOTIFY_WITH_VIBRATE;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;

    @BindView(R.id.setting_push)
    SwitchButton mSettingPush;
    @BindView(R.id.setting_noisy)
    SwitchButton mSettingNoisy;
    @BindView(R.id.setting_roaming)
    SwitchButton mSettingRoaming;
    @BindView(R.id.setting_push_vib)
    SwitchButton mSettingPushVib;
    @BindView(R.id.setting_push_music)
    SwitchButton mSettingPushMusic;
    @BindView(R.id.setting_push_led)
    SwitchButton mSettingPushLed;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.setting_exit)
    RelativeLayout mSettingExit;
    @BindView(R.id.setting_password)
    RelativeLayout mSettingPassword;

    private SharedPrefHelper helper;
    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        helper=SharedPrefHelper.getInstance();
        initTitleBar();
        initSwitchOnClick();
    }



    private void initTitleBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("设置");
        mTitleOptionsImg.setVisibility(View.GONE);
    }

    private void initSwitchOnClick() {
        //免打扰模式
        JMessageClient.getNoDisturbGlobal(new IntegerCallback() {
            @Override
            public void gotResult(int i, String s, Integer integer) {
                if (integer == 1){
                    mSettingNoisy.setChecked(true);
                }else{
                    mSettingNoisy.setChecked(false);
                }
            }
        });
        mSettingNoisy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //免打扰
                    disturbGlobal(1);
                }else {
                    disturbGlobal(0);
                }
            }
        });

        //消息漫游 ，默认开启状态
        mSettingRoaming.setChecked(helper.getRoaming());
        mSettingRoaming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    JMessageClient.init(BaseApplication.baseApplication,true);
                }else {
                    JMessageClient.init(BaseApplication.baseApplication,false);
                }
                helper.setRoaming(b);
            }
        });
        pushMusic();
        pushvib();
    }

    //震动
    private void pushvib() {
        mSettingPushVib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    if (helper.getMusic()){
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE | FLAG_NOTIFY_WITH_SOUND | FLAG_NOTIFY_WITH_VIBRATE);
                    }else {
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE | FLAG_NOTIFY_WITH_VIBRATE);
                    }

                }else {
                    if (helper.getMusic()){
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE | FLAG_NOTIFY_WITH_SOUND);
                    }else {
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE);
                    }
                }

                helper.setVib(b);
            }
        });

    }

    //提示音
    private void pushMusic() {
        mSettingPushMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (helper.getVib()){
                        /*
                        * FLAG_NOTIFY_SILENCE 开启通知栏
                        * FLAG_NOTIFY_WITH_SOUND 收到消息有声音
                        * FLAG_NOTIFY_WITH_VIBRATE  收到消息会震动
                        * **/
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE| FLAG_NOTIFY_WITH_SOUND| FLAG_NOTIFY_WITH_VIBRATE);
                    }else {
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE | FLAG_NOTIFY_WITH_SOUND);
                    }
                    helper.setMusic(true);
                }else {
                    if (helper.getVib()){
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE| FLAG_NOTIFY_WITH_VIBRATE);
                    }else {
                        JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE);
                    }
                    helper.setMusic(false);
                }

            }
        });
    }


    //免打扰
    private void disturbGlobal(int i) {
        JMessageClient.setNoDisturbGlobal(i, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {

            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        mSettingPushVib.setChecked(helper.getVib());
        mSettingPushMusic.setChecked(helper.getMusic());

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

    }
    @OnClick({R.id.title_bar_back,R.id.setting_exit,R.id.setting_password})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.setting_exit:
                JMessageClient.logout();
                helper.setUserPW("");
                helper.setNakeName("");
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_password:
                startActivity(new Intent(SettingActivity.this,PassWordActivity.class));
                break;
        }
    }
}
