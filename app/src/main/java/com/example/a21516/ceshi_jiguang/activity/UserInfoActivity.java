package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.example.a21516.ceshi_jiguang.famework.utils.StringUtils;
import com.example.a21516.ceshi_jiguang.famework.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wapchief on 2017/7/28.
 * 用户资料
 */

public class UserInfoActivity extends BaseActivity{

    @BindView(R.id.userinfo_scroll)
    ScrollView mUserinfoScroll;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_bar_left)
    LinearLayout mTitleBarLeft;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.title_bar_right)
    RelativeLayout mTitleBarRight;
    @BindView(R.id.bottom_bar_tv1)
    TextView mBottomBarTv1;
    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_tv2)
    TextView getmBottomBarTv2;
    @BindView(R.id.bottom_bar_right)
    RelativeLayout mBottomBarRight;
    @BindView(R.id.title)
    LinearLayout mTitle;
    @BindView(R.id.userinfo_avatar)
    CircleImageView mUserinfoAvatar;
    @BindView(R.id.userinfo_nikename)
    TextView mUserinfoNikename;
    @BindView(R.id.userinfo_signature)
    TextView mUserinfoSignature;
    @BindView(R.id.userinfo_username)
    TextView mUserinfoUsername;
    @BindView(R.id.userinfo_gender)
    TextView mUserinfoGender;
    @BindView(R.id.userinfo_birthday)
    TextView mUserinfoBirthday;
    @BindView(R.id.userinfo_region)
    TextView mUserinfoRegion;
    @BindView(R.id.userinfo_mtime)
    TextView mUserinfoMtime;
    @BindView(R.id.userinfo_fill)
    ImageView mUserinfoFill;

    private String username;
    private SharedPrefHelper helper;
    private String avtar="";
    private String getUserName="";
    private UserInfo userInfo;
    @Override
    protected int setContentView() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {

        //获取跳转过来的数据
        getUserName = getIntent().getStringExtra("USERNAME");
        initBar();
        //获取用户资料
        initUserInfo(getUserName);

        JAnalyticsInterface.onPageStart(this,this.getClass().getCanonicalName());
    }

    //获取用户资料
    private void initUserInfo(final String userName) {
        JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i==0){
                    userInfo = userInfo;
                    Picasso.with(UserInfoActivity.this)
                            .load(userInfo.getAvatarFile())
                            .placeholder(R.mipmap.icon_user)
                            .into(mUserinfoAvatar);

                    mUserinfoUsername.setText(userInfo.getUserName()+"");
                    mUserinfoNikename.setText(userInfo.getNickname()+"");
                    mUserinfoRegion.setText(userInfo.getAddress()+"");
                    mUserinfoBirthday.setText(TimeUtils.ms2date("yyyy-MM-dd",userInfo.getBirthday()));
                    mUserinfoGender.setText(StringUtils.constant2String(userInfo.getGender()+""));
                    mUserinfoMtime.setText("上次活动："+TimeUtils.unix2Date("yyyy-MM-dd",userInfo.getmTime()));
                    if (userInfo.getSignature().equals("")){
                        mUserinfoSignature.setText("签名：说点儿什么吧～");
                    }else {
                        mUserinfoSignature.setText("签名：" + userInfo.getSignature());
                    }
                    CountEvent cEvent=new CountEvent("event_userId_"+userInfo.getUserID());
                    JAnalyticsInterface.onEvent(getContext(),cEvent);
                }else {

                }
            }
        });
    }

    private void initBar() {

        helper=SharedPrefHelper.getInstance();
        //设置背景
        mTitleBarLeft.setBackground(getResources().getDrawable(R.drawable.shape_titlebar));
        mTitleBarRight.setBackground(getResources().getDrawable(R.drawable.shape_titlebar2));
        mTitleBarTitle.setText("个人资料");
        mTitleBarLeft.setBackgroundColor(Color.parseColor("#00000000"));
        mTitleBarRight.setBackgroundColor(Color.parseColor("#00000000"));
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleOptionsTv.setText("更多");
        mTitleOptionsTv.setVisibility(View.VISIBLE);
        mBottomBarLeft.setVisibility(View.GONE);
        username = getIntent().getStringExtra("USERNAME");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JAnalyticsInterface.onPageEnd(this,this.getClass().getCanonicalName());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_bar_back,R.id.title_options_tv,R.id.bottom_bar_tv1,R.id.bottom_bar_tv2})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.title_options_tv:
                Intent intent1=new Intent(UserInfoActivity.this,UserInfoOptionsActivity.class);
                intent1.putExtra("USERNAME",getIntent().getStringArrayExtra("NAME"));
                startActivity(intent1);
                break;
            case R.id.bottom_bar_tv2:
                //创建会话   发消息
                Conversation conv=JMessageClient.getSingleConversation(getIntent().getStringExtra("USERNAME"),SharedPrefHelper.getInstance().getAppKey());
                //假设没有聊过天就 创建会话
                if (conv == null){
                    Conversation.createSingleConversation(getIntent().getStringExtra("USERNAME"),SharedPrefHelper.getInstance().getAppKey());
                }
                Intent intent=new Intent(UserInfoActivity.this,ChatMsgActivity.class);
                intent.putExtra("USERNAME",getIntent().getStringExtra("USERNAME"));
                startActivity(intent);
                break;
        }
    }
}


