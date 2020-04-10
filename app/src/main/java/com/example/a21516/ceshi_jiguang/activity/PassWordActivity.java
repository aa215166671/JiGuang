package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class PassWordActivity  extends BaseActivity {

    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.password_old)
    EditText mPasswordOld;
    @BindView(R.id.password_new)
    EditText mPasswordNew;
    @BindView(R.id.password_new2)
    EditText mPasswordNew2;
    @BindView(R.id.password_ok)
    Button mPasswordOk;

    @Override
    protected int setContentView() {
        return R.layout.activity_password;
    }

    @Override
    protected void initView() {

        initBar();

    }

    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("修改密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.title_bar_back,R.id.password_ok})
    public void OnViewClicked(View view){
        switch (view.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.password_ok:
                if (mPasswordNew.getText().toString().equals(mPasswordNew2.getText().toString())){
                    JMessageClient.updateUserPassword(mPasswordOld.getText().toString(), mPasswordNew2.getText().toString(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0){
                                showToast(PassWordActivity.this,"修改成功");
                                startActivity(new Intent(PassWordActivity.this,LoginActivity.class));

                            }else {
                                showToast(PassWordActivity.this,"修改失败");
                            }
                        }
                    });
                }else {
                    showToast(PassWordActivity.this,"两次输入的密码不一致");
                }
                break;
        }
    }
}
