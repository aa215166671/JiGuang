package com.example.a21516.ceshi_jiguang.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.activity.ChatMsgActivity;
import com.example.a21516.ceshi_jiguang.activity.GroupListActivity;
import com.example.a21516.ceshi_jiguang.adapter.MessageRecyclerAdapter;
import com.example.a21516.ceshi_jiguang.entity.MessageBean;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.example.a21516.ceshi_jiguang.view.MyAlertDialg;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class MessageFragment extends Fragment {

    //初始化控件
    @BindView(R.id.fragment_main_group)
    RelativeLayout mFragmentMainGroup;
    @BindView(R.id.fragment_main_none)
    TextView mFragmentMainNone;
    @BindView(R.id.fragment_main_rf)
    SwipeRefreshLayout mFragmentMainRf;
    @BindView(R.id.fragment_main_header)
    RecyclerViewHeader mFragmentMainHeader;
    @BindView(R.id.fragment_main_rv)
    RecyclerView mFragmentMainRv;
    @BindView(R.id.item_main_img)
    ImageView mItemMainImg;
    @BindView(R.id.item_main_username)
     TextView mItemMainUsername;
    @BindView(R.id.item_main_content)
    TextView mItemMainContent;
    @BindView(R.id.item_main_time)
    TextView mItemMainTime;

    Unbinder unbinder;
    private List<Conversation> list=new ArrayList<>();
    //消息列表数据
    private List<MessageBean> data=new ArrayList<>();
    //接收撤回消息
    private Message retractMsg;
    Handler handler=new Handler();

    MessageRecyclerAdapter adapter;
    MessageBean bean;


    //漫游
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.fragment_main,null);

        /**调用时将解除视图绑定的解除绑定合约。*/
        unbinder= ButterKnife.bind(this,view);
        JMessageClient.registerEventReceiver(this);
        list=JMessageClient.getConversationList();
        initView();
        return view;
    }

    private void initView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updataData();
            }
        },2000);
        //下拉刷新
        initRefresh();
        //数据
        initData();
        //群组
        initGroup();
        //监听Item
        onClickItem();
    }


    //下拉刷新
    private void initRefresh() {
        mFragmentMainRf.setColorSchemeResources(
                R.color.color_shape_right,
                R.color.colorAccent,
                R.color.aurora_msg_receive_bubble_default_color,
                R.color.oriange);
        //开启一个刷新的线程
        mFragmentMainRf.post(new Runnable() {
            @Override
            public void run() {
                //显示那个圈
                mFragmentMainRf.setRefreshing(true);
            }
        });
        //监听刷新状态
        mFragmentMainRf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updataData();
                    }
                },1800);
            }
        });
    }

    //刷新数据
    private void updataData() {
        data.clear();
        list.clear();
        intiDataBean();
    }


    //配数据
    private void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mFragmentMainRv.setLayoutManager(linearLayoutManager);
        adapter =new MessageRecyclerAdapter(data,getActivity());
        //分割线
        mFragmentMainRv.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        mFragmentMainRv.setAdapter(adapter);
        mFragmentMainHeader.attachTo(mFragmentMainRv);
    }


    /*群组*/
    private void initGroup() {
        mItemMainImg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_group));
        mItemMainUsername.setText("群助手");
        mItemMainUsername.setTextSize(16);
        mItemMainContent.setText("[有一个未读消息]");
        mItemMainContent.setTextColor(Color.parseColor("#E5955D"));
    }

    //监听Item
    private void onClickItem() {
        adapter.setOnItemClickListener(new MessageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view != null){
                    Intent intent=new Intent(getActivity(),ChatMsgActivity.class);
                    intent.putExtra("USERNAME",data.get(position).getUserName());
                    intent.putExtra("NAKENAME",data.get(position).getTitle());
                    intent.putExtra("MSGID",data.get(position).getMsgID());
                    Log.i("MSG", "onItemClick: "+data.get(position).getMsgID());
                    intent.putExtra("position",position);

                    startActivity(intent);

                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                String[] strings={"删除会话"};
                MyAlertDialg dialg=new MyAlertDialg(getActivity(), strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0){
                            JMessageClient.deleteSingleConversation(data.get(position).getUserName());
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialg.initDialog();
            }
        });
    }

    //配置消息数据
    private void intiDataBean() {
        list=JMessageClient.getConversationList();
        //判断有没有列表消息，没有显示【没有回话消息】
        if (list.size() <= 0){
            mFragmentMainNone.setVisibility(View.VISIBLE);
            mFragmentMainRv.setVisibility(View.GONE);
        }else{
            mFragmentMainNone.setVisibility(View.GONE);
            mFragmentMainRv.setVisibility(View.VISIBLE);
            for (int i = 0;i < list.size();i++){
                bean=new MessageBean();
                try{
                    //进行撤回消息判断
                    if (list.get(i).getLatestMessage().getContent().getContentType() == ContentType.prompt){
                        bean.setContent(((PromptContent)list.get(i).getLatestMessage().getContent()).getPromptText());
                    }else{
                        bean.setContent(((TextContent)(list.get(i).getLatestMessage()).getContent()).getText());
                    }
                }catch (Exception e){
                    bean.setContent("最近没有消息");
                }
                bean.setMsgID(list.get(i).getId());
                bean.setUserName(((UserInfo)list.get(i).getTargetInfo()).getUserName());
                bean.setTitle(list.get(i).getTitle());
                bean.setTime(list.get(i).getUnReadMsgCnt()+"");
                bean.setConversation(list.get(i));
                try{
                    bean.setImg(list.get(i).getAvatarFile().toURI()+"");
                }catch (Exception e){

                }
                data.add(bean);
            }
        }
        mFragmentMainRf.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        updataData();
        initGroup();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    //释放资源
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    //接收消息
    public void onEvent(final MessageEvent event){
        final Message msg= event.getMessage();
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (JMessageClient.getMyInfo().getUserName() == "1006"|| JMessageClient.getMyInfo().getUserName().equals("1006")){
                    final  Message message1=
                            JMessageClient.createSingleTextMessage(((UserInfo)msg.getTargetInfo()).getUserName(),
                                    SharedPrefHelper.getInstance().getAppKey(),"[自动回复]你好，我是机器人");

                    JMessageClient.sendMessage(message1);
                }
                updataData();
            }
        });
    }
    //接收撤回消息
    public void onEvent(MessageRetractEvent event){
        retractMsg=event.getRetractedMessage();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updataData();
            }
        },500);
    }
    //接收离线消息
    public void onEvent(OfflineMessageEvent event){
        Conversation conv=event.getConversation();
        updataData();
    }
    //消息漫游完成事件
    public void onEvent(ConversationRefreshEvent event){
        final Conversation conv=event.getConversation();
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updataData();
            }
        });
    }
    @OnClick(R.id.fragment_main_group)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fragment_main_group:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
        }
    }
}
