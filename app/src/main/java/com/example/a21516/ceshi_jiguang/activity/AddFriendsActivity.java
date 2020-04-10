package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.adapter.AddSearchAdapter;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.greendao.SearchAddDao;
import com.example.a21516.ceshi_jiguang.famework.greendao.model.SearchAdd;
import com.example.a21516.ceshi_jiguang.famework.helper.GreenDaoHelper;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/*
* 添加好友 和群组
* */
public class AddFriendsActivity  extends BaseActivity{
    @BindView(R.id.add_tab)
    SegmentTabLayout mAddTab;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.add_search)
    EditText mAddSearch;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.add_commit)
    Button mAddCommit;
    @BindView(R.id.add_lv)
    ListView mAddLv;
    @BindView(R.id.add_del)
    Button mAddDel;

    private String[] mTitles={"加好友","加群"};

    private GreenDaoHelper daoHelper;
    private SearchAddDao dao;
    private AddSearchAdapter adapter;
    private List<SearchAdd> list;
    private String name="";
    private SharedPrefHelper helper;
    @Override
    protected int setContentView() {
        return R.layout.activity_add_firends;
    }

    @Override
    protected void initView() {

        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleOptionsImg.setVisibility(View.GONE);
        helper=SharedPrefHelper.getInstance();
        initbar();
        initSearchDB();
        initEditKey();
        initOnClick();
    }



    @Override
    protected void initData() {

    }

    private void initbar() {
        mAddTab.setTabData(mTitles);
        mTitleBarTitle.setText("加好友");
        mAddTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                if (i == 0){
                    mTitleBarTitle.setText("加好友");
                    mAddSearch.setHint("搜索好友");
                }else {
                    mTitleBarTitle.setText("加群");
                    mAddSearch.setHint("搜索群");
                }
            }

            //当用户再次选择已选中的选项卡时调用。一些应用程序可以使用此操作返回到类别的顶层
            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    //监听搜索文本
    private void initEditKey() {
        mAddSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
                Log.e("name===", ":" + name);
            }
        });
    }

    //监听点击哪个 list
    private void initOnClick() {
        adapter.setListener(new AddSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final String id, int position) {
                JMessageClient.getUserInfo(id, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        if (i==0){
                            Intent intent = new Intent(AddFriendsActivity.this,AddFriendMsgActivity.class);
                            intent.putExtra("ID",id);
                            if (userInfo.getNickname() != null){
                                intent.putExtra("NAME",userInfo.getNickname());
                            }if (userInfo.getAvatarFile() != null){
                                intent.putExtra("ICON",userInfo.getAvatarFile().toURI().toString());
                                Log.i("url", "gotResult: "+userInfo.getAvatarFile().toURI().toString());
                            }
                            startActivity(intent);
                        }else {
                            showToast(AddFriendsActivity.this,"未找到用户");
                        }
                    }
                });
            }
        });
    }

    /*数据库*/
    private void initSearchDB(){
        //初始化
        daoHelper = new GreenDaoHelper(this);
        dao = daoHelper.initDao().getSearchAddDao();
        initQuery();
        adapter = new AddSearchAdapter(this,list);
        mAddLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initQuery();
        adapter.notifyDataSetChanged();

    }


    //参训所有数据
    private void initQuery() {
        list = dao.queryBuilder().list();
        //判断是否有数据
        if (list.size() == 0){
            mAddLv.setVisibility(View.GONE);
            mAddDel.setVisibility(View.GONE);
        }else {
            mAddLv.setVisibility(View.VISIBLE);
            mAddDel.setVisibility(View.VISIBLE);
        }
        //list 倒序排序
        Collections.reverse(list);
    }

    //增加
    private void initInsert(String name) {
        try{
            if (list.size() < 8){
                //删除已经重复的搜索历史
                List<SearchAdd> list2 = dao.queryBuilder()
                        .where(SearchAddDao.Properties.Content.eq(name)).build().list();
                dao.deleteInTx(list2);
                //添加
                if (! name.equals("")){
                    dao.insert(new SearchAdd(null,name));
                    list.add(new SearchAdd(null,name));
                }else {
                    //删除第一条数据，用于替换最后一条上搜索
                    dao.delete(dao.queryBuilder().list().get(0));
                    //删除已经存在重复的搜索历史
                    List<SearchAdd> list3 = dao.queryBuilder()
                            .where(SearchAddDao.Properties.Content.eq(name)).build().list();
                    dao.deleteInTx(list3);
                    //添加
                    if (! name.equals("")){
                        dao.insert(new SearchAdd(null,name));
                    }
                }
                //添加后更新列表
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            showToast(this,"插入失败");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.title_bar_back,R.id.add_commit,R.id.add_del})
    public void OnViewClicked(View view){
        switch (view.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.add_commit:
                JMessageClient.getUserInfo(name, new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        if ( i  == 0){
                            Log.i("url", "gotResult: "+userInfo.getAvatarFile().toURI().toString());
                            initInsert(name);
                            Intent intent = new Intent(AddFriendsActivity.this,AddFriendMsgActivity.class);
                            intent.putExtra("ID",name);
                            if (userInfo.getNickname() != null){
                                intent.putExtra("NAME",userInfo.getNickname());
                            }if (userInfo.getAvatarFile() != null){
                                intent.putExtra("ICON",userInfo.getAvatarFile().toURI().toString());
                            }
                            startActivity(intent);
                        }else {
                            showToast(AddFriendsActivity.this,"未找到用户");
                        }

                    }
                });
                break;
            case R.id.add_del:
                try {
                    dao.deleteAll();
                    mAddDel.setVisibility(View.GONE);
                    list.clear();
                    initQuery();
                    adapter.notifyDataSetChanged();
                }catch (Exception e){

                }
                break;
                default:
                    break;
        }
    }


}
