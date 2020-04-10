package com.example.a21516.ceshi_jiguang.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.adapter.MessageRecyclerAdapter;
import com.example.a21516.ceshi_jiguang.entity.MessageBean;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.greendao.RequestListDao;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.RequestList;
import com.example.a21516.ceshi_jiguang.famework.helper.GreenDaoHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PullMsgListMoreActivity  extends BaseActivity{
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.list_more_rv)
    RecyclerView mListMoreRv;

    private List<RequestList> list = new ArrayList<>();
    private List<MessageBean> list1 = new ArrayList<>();
    private MessageRecyclerAdapter adapter;
    private GreenDaoHelper daoHelper;
    private RequestListDao dao;
    @Override
    protected int setContentView() {
        return R.layout.activity_more_msglist;
    }

    @Override
    protected void initView() {
        initBar();

    }

    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("好友通知");
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        mListMoreRv.setLayoutManager(layoutManager);
        adapter = new MessageRecyclerAdapter(list1,this);
        //分割线
        mListMoreRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));;
        mListMoreRv.setAdapter(adapter);

        daoHelper = new GreenDaoHelper(this);
        dao = daoHelper.initDao().getRequestListDao();
        //查询所有
        list = dao.queryBuilder().list();

        //判断是否数据
        if(list.size() == 0 ){
            mListMoreRv.setVisibility(View.GONE);
        }else {
            mListMoreRv.setVisibility(View.VISIBLE);
        }
        for (int i =0;i<list.size();i++){
            MessageBean bean1 =new MessageBean();
            bean1.setType(2);
            bean1.setTitle(list.get(i).getNakeName());
            bean1.setContent("验证消息："+list.get(i).getMsg());
            bean1.setUserName(list.get(i).getUserName());
            bean1.setImg(list.get(i).getImg());
            list1.add(bean1);
        }
        Collections.reverse(list1);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_bar_back, R.id.title_options_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.title_options_tv:
                break;
        }
    }
}
