package com.example.a21516.ceshi_jiguang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.MainActivity;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class UserInfoOptionsActivity extends BaseActivity{

    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_right)
    RelativeLayout mBottomBarRight;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title_bar_right)
    RelativeLayout mTitleBarRight;
    @BindView(R.id.bottom_bar_tv2)
    TextView mBottomBarTv2;
    private UserInfo info;
    @Override
    protected int setContentView() {
        return R.layout.activity_userinfo_options;
    }

    @Override
    protected void initView() {
        initUserInfo(getIntent().getStringExtra("USERNAME"));
        intiBar();

    }

    private void initUserInfo( final String username) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                info=userInfo;
            }
        });
    }

    private void intiBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("更多");
        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setBackground(getResources().getDrawable(R.drawable.shaoe_button_lines3));
        mBottomBarTv2.setText("删除好友");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bottom_bar_tv2,R.id.bottom_bar_right,R.id.title_bar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bottom_bar_tv2:
                new AlertDialog.Builder(UserInfoOptionsActivity.this).setTitle("好友删除提示：")
                        .setMessage("该操作会将好友删除，并且会清空会话")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //删除好友
                              info.removeFromFriendList(new BasicCallback() {
                                  @Override
                                  public void gotResult(int i, String s) {
                                      if (i == 0){
                                          showToast(UserInfoOptionsActivity.this,"删除成功");
                                          //删除会话
                                          JMessageClient.deleteSingleConversation(info.getUserName());
                                          Intent intent = new Intent(UserInfoOptionsActivity.this,MainActivity.class);
                                          intent.putExtra("REMOVEID",info.getUserName());
                                          startActivity(intent);
                                      } else{
                                          showToast(UserInfoOptionsActivity.this,"删除失败"+s);
                                      }
                                  }
                              });
                            }
                        }).show();
                break;
            case R.id.title_bar_back:
                finish();
                break;
        }
    }
}
