package com.example.a21516.ceshi_jiguang.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.activity.AddFriendMsgActivity;
import com.example.a21516.ceshi_jiguang.famework.utils.GlideUtil;
import com.example.a21516.ceshi_jiguang.famework.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

//  BaseQuickAdapter  和原始的adapter相对，减少70%的代码量。
public class FriendsAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> {
    private EnumMessageType mMessageType;

    public FriendsAdapter(EnumMessageType messageType){
        super(R.layout.item_main_message);
        this.mMessageType = messageType;
    }
    @Override
    protected void convert(BaseViewHolder helper, T item) {
        final Button button=helper.getView(R.id.item_main_bt);
        final TextView time=helper.getView(R.id.item_main_time);
        final ImageView imageView=helper.getView(R.id.item_main_img);
        if (mMessageType != null){
            switch (mMessageType){
                case RECOMMEND:
                    //推荐列表
//                    if (item.getFriends()) {
//                        button.setText("已添加");
//                        button.setEnabled(false);
//                    } else {
//                        button.setText("添加");
//                        button.setEnabled(true);
//                    }
//                    button.setVisibility(View.VISIBLE);
//                    button.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Intent intent = new Intent(mContext, AddFriendMsgActivity.class);
//                                    intent.putExtra("ID", item.getContent());
//                                    intent.putExtra("NAME", item.getTitle());
//                                    mContext.startActivity(intent);
//                                }
//                            });
                    break;
                case FRIENDS:
                    //好友列表
                    UserInfo data= (UserInfo) item;
                    if (helper.getAdapterPosition() == 0){
                        LogUtils.e("nakeName:"+data.getNickname()+",userName:"+data.getUserName()+",id:"+data.getUserID()+data.getAvatar());
                    }
                    String name="";
                    if ( ! data.getNickname().isEmpty()){
                        name=data.getNickname();
                    }else if (!data.getUserName().isEmpty()){
                        name=data.getUserName();
                    }else {
                        name=""+data.getUserID();
                    }
                    helper.setText(R.id.item_main_username,name)
                            .setText(R.id.item_main_content,data.getSignature())
                            .setText(R.id.item_main_time, TimeUtils.unix2Date("MM-dd HH:mm",data.getmTime()));
                    GlideUtil.loadUserHeadImg(mContext,data.getAvatarFile().toURI().toString(),imageView);

                    break;
                case CERT:
                    //好友申请
//                    JMessageClient.getUserInfo(item.getUserName(), new GetUserInfoCallback() {
//                        @Override
//                        public void gotResult(int i, String s, UserInfo userInfo) {
//                            if (i == 0) {
//                                Log.e("isFriends", userInfo.getUserName() + "...." + userInfo.isFriend());
//                                if (userInfo.isFriend()) {
//                                    button.setText("已同意");
//                                    button.setEnabled(false);
//                                } else {
//                                    button.setText("同意");
//                                    button.setEnabled(true);
//                                }
//                                button.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                    button.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    ContactManager.acceptInvitation(item.getUserName(), "", new BasicCallback() {
//                                        @Override
//                                        public void gotResult(int i, String s) {
//                                            if (i == 0) {
//                                                button.setText("已同意");
//                                            } else {
//                                                ToastUtils.showShort("验证失败");
//                                            }
//                                        }
//                                    });
//                                }
//                            });
//                    break;
                case MESSAGE:
                    //会话列表
//                    if (Integer.valueOf(item.time) <= 0) {
//                        time.setText(item.time + "条未读消息");
//                        time.setTextColor(Color.parseColor("#66000000"));
//                    } else {
//                        time.setText(item.time + "条未读消息");
//                        time.setTextColor(Color.parseColor("#E5955D"));
//                    }
                        break;
                        default:
                            break;
            }
        }
    }

    public enum  EnumMessageType {
        //好友列表
        FRIENDS,
        //推荐列表
        RECOMMEND,
        //好友申请
        CERT,
        //会话列表
        MESSAGE,
    }
}
