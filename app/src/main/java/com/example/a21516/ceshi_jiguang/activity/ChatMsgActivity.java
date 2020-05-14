package com.example.a21516.ceshi_jiguang.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.a21516.ceshi_jiguang.MainActivity;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.entity.DefaultUser;
import com.example.a21516.ceshi_jiguang.entity.ImgBean;
import com.example.a21516.ceshi_jiguang.entity.MyMessage;
import com.example.a21516.ceshi_jiguang.entity.UserStateBean;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.example.a21516.ceshi_jiguang.famework.network.NetWorkManager;
import com.example.a21516.ceshi_jiguang.famework.utils.DateTimeUtils;
import com.example.a21516.ceshi_jiguang.famework.utils.StringUtils;
import com.example.a21516.ceshi_jiguang.view.ChatView;
import com.example.a21516.ceshi_jiguang.view.MyAlertDialg;
import com.example.a21516.ceshi_jiguang.view.MyViewHolder;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.view.ShapeImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_IMAGE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_TEXT;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_VOICE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_CUSTOM;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_FILE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_IMAGE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_LOCATION;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_TEXT;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_VIDEO;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_VOICE;
import static cn.jpush.im.android.api.enums.ContentType.prompt;


public class ChatMsgActivity extends BaseActivity implements View.OnTouchListener,ChatView.OnSizeChangedListener,EasyPermissions.PermissionCallbacks {

    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.title_options_tv)
    TextView mTitleOptionsTv;
    @BindView(R.id.chat_view)
    ChatView mChatView;
    @BindView(R.id.title_options_img)
    ImageView mTitleOptionsImg;
    @BindView(R.id.title)
    LinearLayout mTitle;
    @BindView(R.id.msg_list)
    MessageList mMsgList;
    @BindView(R.id.chat_input)
    ChatInputView mChatInput;


    private SharedPrefHelper helper;
    private Context mContext;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;
    private List<MyMessage> list = new ArrayList<>();

    //收发的头像
    private ImageView imageAvatarSend, imageAvatarReceive;

    private ShapeImageView image_send;
    private String userName = "";

    //撤回消息的视图 msgid
    private String msgID = "";

    private String msgid;
    //哪个会话
    private int position;

    //会话列表
    List<Conversation> conversations;
    //当前会话
    Conversation conversation;

    private UserInfo userInfo;
    private String imgSend = "R.drawable.ironman";
    private String imgRecrive = "R.drawable.ironman";
    MyMessage myMessage;
    InputMethodManager mManager;
    Window mWindow;

    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    private String msgId;

    @Override
    protected int setContentView() {
        return R.layout.activity_chat;

    }

    @Override
    protected void initView() {
        editTextHeight();
        this.mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mChatView.initModule();
       mChatView.setOnSizeChangedListener(this);
        mWindow = getWindow();
        helper = SharedPrefHelper.getInstance();
        mContext = ChatMsgActivity.this;
        userInfo = JMessageClient.getMyInfo();
        //注册接收者
        JMessageClient.registerEventReceiver(this);
        conversations = JMessageClient.getConversationList();
        userName = getIntent().getStringExtra("USERNAME");

        //进入这个页面，不在进行消息提示
        JMessageClient.enterSingleConversation(userName);

        msgID = getIntent().getStringExtra("MSGID");
        //position 从上个页面传递的会话位置
        position = getIntent().getIntExtra("position", 0);
        conversation = conversations.get(position);

        View view = View.inflate(mContext, R.layout.item_receive_photo, null);
        View view1 = View.inflate(mContext, R.layout.item_send_photo, null);

        imageAvatarSend = (ImageView) view.findViewById(R.id.aurora_iv_msgitem_avatar);
        imageAvatarReceive = (ImageView) view1.findViewById(R.id.aurora_iv_msgitem_avatar);
        image_send = (ShapeImageView)view1.findViewById(R.id.aurora_iv_msgitem_photo);


        try {
            imgSend = userInfo.getAvatarFile().toURI().toString();
            imgRecrive = StringUtils.isNull(conversation.getAvatarFile().toURI().toString()) ? "R.drawable.ironman" : conversation.getAvatarFile().toURI().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mData = getMessages();
        //标题栏
        initTitleBar();
        //初始化adater
        initMsgAdapter();
      //  imageLoader.loadAvatarImage(imageAvatarSend, userInfo.getAvatarFile().toURI().toString());
      //  imageLoader.loadAvatarImage(imageAvatarReceive, imgRecrive);
        mTitleBarBack.setVisibility(View.VISIBLE);
        //获取键盘的高度

        mChatInput.setMenuContainerHeight(heightDifference);

        //对输入框操作
        initInputView();

    }

    int heightDifference = 800;
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatInput.setMenuContainerHeight(oldh - h);
            mChatView.setMenuHeight(oldh - h);
        }
        scrollToBottom();
    }

    /*获取键盘的高度*/
    private void editTextHeight() {
        mChatInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ChatMsgActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ChatMsgActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                if (screenHeight - r.bottom > 0) {
                    heightDifference = screenHeight - r.bottom;
                }
            }
        });
    }

    //输入框操作
    private void initInputView() {
        mChatView.setOnTouchListener(this);
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }else {
                    //发送文字消息
                    sendMessage(input.toString());
                    return true;
                }
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                //选择照片或视频文件或录制完视频
                if (list == null || list.isEmpty()) {
                    return ;
                }

                MyMessage message;
                for (FileItem item: list){
                    if (item.getType() == FileItem.Type.Image) {
                        try {
                            final Message sendImgMessage = conversation.createSendImageMessage(new File(item.getFilePath()));
                            message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                            message.setMessage(sendImgMessage);
                            message.setMediaFilePath(item.getFilePath());
                            message.setTimeString(DateTimeUtils.getTimeStringAutoShort2(sendImgMessage.getCreateTime(), System.currentTimeMillis(), true));
                            message.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(),JMessageClient.getMyInfo().getNickname(), imgSend));
                            final MyMessage sendMessage = message;
                            sendImgMessage.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        // 在底部插入一条消息，第二个参数表示是否滚动到底部。
                                        mAdapter.addToStart(sendMessage, true);
                                        if (mData != null|| mMsgIdList != null || mPathList != null){
                                                mMsgIdList.clear();
                                                mPathList.clear();
                                                mData.clear();
                                        }
                                        mData= getMessages();

                                    } else {
                                        Log.e("ChatMsgActivity", "发送失败: " + s);
                                    }
                                }
                            });
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JMessageClient.sendMessage(sendImgMessage);
                                }
                            }).start();
                            scrollToBottom();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this, perms)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };
                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this, perms)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_PHOTO, perms);
                }
                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this, perms)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);
                    return false;
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }
        });

        //录音
        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                //设置语音文件路径，录音后，音频文件将保存在此处
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                mChatView.setRecordVoiceFile(path, new DateFormat().format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                //发送语音消息
                sendVoiceMessage(voiceFile,duration);
            }

            @Override
            public void onCancelRecord() {
                //取消了录音的补充，已经删除了文件
            }

            @Override
            public void onPreviewCancel() {
                //在预览记录的语音布局中，单击“取消”按钮时触发

            }

            @Override
            public void onPreviewSend() {
                //在预览记录的语音布局中，单击“发送”按钮时触发

            }
        });
        //拍摄
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                mPathList.add(photoPath);
                mMsgIdList.add(message.getMsgId());
                message.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
                ChatMsgActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });


        mChatView.getChatInputView().getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollToBottom();

                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    //初始化消息列表
    public List<MyMessage> getMessages() {
        list = new ArrayList<>();
        for (int i = 0; i < conversation.getAllMessage().size(); i++) {
            Log.i("list全部数据", "getMessages: "+conversation.getAllMessage());
            MyMessage message = null;
            //根据消息判断接收方或者发送的类型
            if (conversation.getAllMessage().get(i).getDirect() == MessageDirect.send) {
                //判断消息是否撤回
                if (conversation.getAllMessage().get(i).getContent().getContentType().equals(prompt)) {
                    if (conversation.getAllMessage().get(i).getContentType() == ContentType.voice){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), SEND_VOICE.ordinal());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.text){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), SEND_TEXT.ordinal());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.image){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), SEND_IMAGE.ordinal());
                    }
                } else {
                    if (conversation.getAllMessage().get(i).getContentType() == ContentType.voice){
                        message = MyMessage.SendTextMesg(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), SEND_VOICE.ordinal());
                        message.setMediaFilePath(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                        message.setDuration(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.text){
                        message = MyMessage.SendTextMesg(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), SEND_TEXT.ordinal());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.image){
                        message = MyMessage.SendTextMesg(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath(), SEND_IMAGE.ordinal());
                        message.setMediaFilePath(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath());


                        mMsgIdList.add(String.valueOf(conversation.getAllMessage().get(i).getId()));

                        NetWorkManager.getImgUrl(((ImageContent) conversation.getAllMessage().get(i).getContent()).getMediaID(), new Callback<ImgBean>() {
                            @Override
                            public void onResponse(Call<ImgBean> call, Response<ImgBean> response) {
                                if (response.code() == 200) {
                                    mPathList.add(response.body().getUrl());
                                }
                            }
                            @Override
                            public void onFailure(Call<ImgBean> call, Throwable t) {
                            }
                        });
//                        String sendPathUrl= ((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath();
//                        mSendPathList.add(sendPathUrl);
//                        Log.e("xiaoxi2", "getMessages: "+sendPathUrl);
                        }
                }
                message.setUserInfo(new DefaultUser(userName, JMessageClient.getMyInfo().getNickname(), (StringUtils.isNull(imgSend)) ? " R.drawable.ironman" : imgSend));
            }
            //接收方
            else {
                //判断消息是否撤回
                if (conversation.getAllMessage().get(i).getContent().getContentType().equals(prompt)) {
                    if (conversation.getAllMessage().get(i).getContentType() == ContentType.voice){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), RECEIVE_VOICE.ordinal());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.text){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), RECEIVE_TEXT.ordinal());
                    }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.image){
                        message = MyMessage.SendTextMesg(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), RECEIVE_IMAGE.ordinal());


                    }
                } else {
                        if (conversation.getAllMessage().get(i).getContentType() == ContentType.voice){
                            message = MyMessage.SendTextMesg(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), RECEIVE_VOICE.ordinal());
                            message.setMediaFilePath(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                            message.setDuration(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                             }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.text){
                            message = MyMessage.SendTextMesg(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), RECEIVE_TEXT.ordinal());
                        }else if (conversation.getAllMessage().get(i).getContentType() == ContentType.image){
                            message = MyMessage.SendTextMesg(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath(), RECEIVE_IMAGE.ordinal());
                            message.setMediaFilePath(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath());
                            mMsgIdList.add(String.valueOf(conversation.getAllMessage().get(i).getId()));
                            NetWorkManager.getImgUrl(((ImageContent) conversation.getAllMessage().get(i).getContent()).getMediaID(), new Callback<ImgBean>() {
                                @Override
                                public void onResponse(Call<ImgBean> call, Response<ImgBean> response) {
                                    if (response.code() == 200) {
                                        mPathList.add(response.body().getUrl());
                                    }
                                }
                                @Override
                                public void onFailure(Call<ImgBean> call, Throwable t) {
                                }
                            });
                        }
                   }
               message.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getNickname(), (StringUtils.isNull(imgRecrive)) ? "R.drawable.ironman" : imgRecrive));
            }
            message.setPosition(i);
            message.setMessage(conversation.getAllMessage().get(i));
            message.setMsgID(conversation.getAllMessage().get(i).getServerMessageId());
            message.setTimeString(DateTimeUtils.getTimeStringAutoShort2(conversation.getAllMessage().get(i).getCreateTime(), System.currentTimeMillis(), true));
            list.add(message);
        }
        Collections.reverse(list);
        conversation.resetUnreadCount();
        return list;
    }

    @Override
    protected void onResume() {
        friendState();
        super.onResume();
    }

    //接收消息事件
    public void onEvent(MessageEvent event) {
        final Message message = event.getMessage();
        runOnUiThread(new Runnable() {
           @Override
           public void run() {
                if (message.getContentType() == ContentType.text || message.getContentType().equals("text")) {
                    myMessage = new MyMessage(((TextContent) message.getContent()).getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                    myMessage.setMessage(message);
                    myMessage.setText(((TextContent) message.getContent()).getText() + "");
                   mAdapter.addToStart(myMessage, true);
                   mAdapter.notifyDataSetChanged();
               }else if (message.getContentType() == ContentType.voice || message.getContentType().equals("voice")){
                    VoiceContent voiceContent=(VoiceContent)message.getContent();
                   myMessage = new MyMessage(voiceContent.getLocalPath(),IMessage.MessageType.RECEIVE_VOICE.ordinal());
                   myMessage.setMessage(message);
                    myMessage.setMediaFilePath(voiceContent.getLocalPath());
                    myMessage.setDuration(voiceContent.getDuration());
                    mAdapter.addToStart(myMessage, true);
                    mAdapter.notifyDataSetChanged();
                }else if (message.getContentType() == ContentType.image || message.getContentType().equals("image")) {
                    ImageContent imageContent = (ImageContent) message.getContent();
                    myMessage = new MyMessage(imageContent.getLocalThumbnailPath(), IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                    myMessage.setMessage(message);
                    myMessage.setMediaFilePath(imageContent.getLocalThumbnailPath());
                    mAdapter.addToStart(myMessage, true);
                    mAdapter.notifyDataSetChanged();
                }
               myMessage.setMsgID(message.getServerMessageId());
               myMessage.setTimeString(DateTimeUtils.getTimeStringAutoShort2(message.getCreateTime(), System.currentTimeMillis(), true));
               myMessage.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getNickname(), imgRecrive));
               //收到消息时，添加到集合
               list.add(myMessage);
           }
       });
    }

    //接收到撤回的消息
    public void onEvent(MessageRetractEvent event) {
        final Message message = event.getRetractedMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    //getServerMessageId 获取消息对应服务端的messageId。
                    if (list.get(i).getMsgID() == message.getServerMessageId()) {
                        mAdapter.delete(list.get(i));
                        MyMessage message1 = new MyMessage("[对方撤回一条消息]", RECEIVE_TEXT.ordinal());
                        //将消息添加到列表底部
                        mAdapter.addToStart(message1, true);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.updateMessage(message1);
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        //退出会话
        JMessageClient.exitConversation();
        super.onDestroy();
    }

    //初始化adapter

    private void initMsgAdapter() {
        final float density = getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 150 * density;
        final float MAX_WIDTH = 300 * density;
        final float MIN_HEIGHT = 150 * density;
        final float MAX_HEIGHT = 300 * density;
        //加载头像图片的方法
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());
                    avatarImageView.setImageResource(resId);
                } else {
                    Glide.with(ChatMsgActivity.this)
                            .load(string)
                            .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default))
                            .into(avatarImageView);
                }
            }

            @Override
            public void loadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(string)
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();

                                // 裁剪 bitmap
                                float width, height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth, scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                            }
                        });
            }

            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;

                Glide.with(ChatMsgActivity.this)
                        .asBitmap()
                        .load(uri)
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
            }
        };



        //自定义viewholder  聊天对话
        class MyTexViewHolder extends MyViewHolder<IMessage> {
            public MyTexViewHolder(View itemView, boolean isSender) {
                super(itemView, isSender);
            }
        }
        /**
         * 1、Sender Id: 发送方 Id(唯一标识)。
         * 2、HoldersConfig，可以用这个对象来构造自定义消息的 ViewHolder 及布局界面。
         * 如果不自定义则使用默认的布局
         * 3、ImageLoader 的实例，用来展示头像。如果为空，将会隐藏头像。
         */
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();

        mAdapter = new MsgListAdapter<>(helper.getUserId(), holdersConfig, imageLoader);

        //单击消息事件，可以选择查看大图 或者播放视频
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                msgid = String.valueOf(message.getPosition()+1);
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    Intent intent = new Intent(mContext, VideoActivity.class);
                           intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                    startActivity(intent);
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    Intent intent = new Intent(ChatMsgActivity.this, ViewPagerImageActivity.class);
                    intent.putExtra("position", msgid);
                    intent.putStringArrayListExtra("photoList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);

                    Log.i("mMsgIdList", "onMessageClick: "+mMsgIdList);
                    Log.i("mPathList", "onMessageClick: "+mPathList.size());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.message_click_hint),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //长按消息
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, final MyMessage message) {
                String[] strings;
                msgID = message.getMsgId();
                Log.e("msgIDdelect1", "onMessageLongClick: " + msgID);
                //判断消息的类型
                if (message.getType() == SEND_TEXT.ordinal()
                        || message.getType() == SEND_CUSTOM.ordinal()
                        || message.getType() == SEND_FILE.ordinal()
                        || message.getType() == SEND_IMAGE.ordinal()
                        || message.getType() == SEND_LOCATION.ordinal()
                        || message.getType() == SEND_VIDEO.ordinal()
                        || message.getType() == SEND_VOICE.ordinal()
                        ) {
                    strings = new String[]{"复制", "撤回", "转发", "删除"};
                } else {
                    strings = new String[]{"复制", "转发", "删除"};
                }
                final MyAlertDialg dialg = new MyAlertDialg(ChatMsgActivity.this, strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                //复制，当消息类型为文字才可以复制
                                if (message.getType() == SEND_TEXT.ordinal()
                                        || message.getType() == RECEIVE_TEXT.ordinal()) {
                                    if (Build.VERSION.SDK_INT > 11) {
                                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("Simple text", message.getText());
                                        clipboard.setPrimaryClip(clip);
                                    } else {
                                        ClipboardManager clip = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                        if (clip.hasText()) {
                                            clip.getText();
                                        }
                                    }
                                    showToast(ChatMsgActivity.this, "复制成功");
                                } else {
                                    showToast(ChatMsgActivity.this, "复类型失败");
                                }
                                break;
                            //撤回： 发送方才可以撤回
                            case 1:
                                conversation.retractMessage(message.getMessage(), new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            showToast(ChatMsgActivity.this, "撤回了一条消息");
                                            mAdapter.deleteById(message.getMsgId());
                                            //添加一条撤回的item
                                            mAdapter.addToStart(new MyMessage("[你撤回了一条消息]", SEND_TEXT.ordinal()), true);
                                            mAdapter.updateMessage(message);

                                        } else {
                                            showToast(ChatMsgActivity.this, "撤回失败" + s);
                                        }
                                    }
                                });
                                break;
                            case 2:
                                //转发
                                break;
                            default:
                                //从本地删除
                                conversation.deleteMessage(new Integer(message.getMsgId()));
                                //移除视图
                                mAdapter.deleteById(message.getMsgId());
                                Log.i("视图ID1", "onClick: "+list);
                                Log.i("视图ID2", "onClick: "+message.getMsgId());

                                mAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                dialg.initDialog(Gravity.CENTER);
                dialg.dialogSize(200, 0, 0, 55);
            }
        });

        //点击头像
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Intent intent;
                if (message.getType() == SEND_TEXT.ordinal()) {
                    intent = new Intent(mContext, UserActovity.class);
                } else {
                    intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("USERNAME", userName);
                }
                Log.e("userName", userInfo + "\n" + userName);
                startActivity(intent);
            }
        });


        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT.ordinal());
        message.setUserInfo(new DefaultUser("0", "Deadpool", "R.drawable.deadpool"));
        mAdapter.addToEnd(mData);

        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount <= mData.size()) {
                    Log.i("MessageListActivity", "Loading next page");
                //    loadNextPage();
                }
            }
        });
        mChatView.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);

    }

    //加载更多
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }

    /*滚动到底部*/
    private void scrollToBottom() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatView.getmMesgList().smoothScrollToPosition(0);
            }
        },200);

    }

//

    //标题栏
    private void initTitleBar() {
        mTitleBarBack.setVisibility(View.VISIBLE);
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleOptionsImg.setVisibility(View.GONE);
        mTitleOptionsTv.setVisibility(View.VISIBLE);
        mTitleBarTitle.setText(userName);
        mTitleBarTitle.setTextSize(16);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.title_bar_back, R.id.title_bar_title, R.id.title_options_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_title:
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("USERNAME", userName);
                break;
            case R.id.title_bar_back:
                finish();
                //重置会话为未读
                conversation.resetUnreadCount();
                break;
            case R.id.title_options_tv:
                MyAlertDialg dialg = new MyAlertDialg(this, new String[]{"清空聊天记录", "清空并删除会话"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                showProgressDialog("正在删除聊天记录");
                                if (conversation.deleteAllMessage()) {
                                    mAdapter.clear();
                                    mData.clear();
                                    mAdapter.notifyDataSetChanged();
                                    dismissProgressDialog();
                                }
                                break;
                            case 1:
                                showProgressDialog("正在删除");
                                if (JMessageClient.deleteSingleConversation(userName)) {
                                    startActivity(new Intent(ChatMsgActivity.this, MainActivity.class));
                                }
                                break;
                        }
                    }
                });
                dialg.initDialog(Gravity.RIGHT | Gravity.TOP);
                dialg.dialogSize(200, 0, 0, 55);
                break;
        }
    }

    //发送信息，只能发送文本
    private void sendMessage(String msg) {
            TextContent content = new TextContent(msg);
            Message sendTextmessage = conversation.createSendMessage(content);
            final MyMessage myMessage = new MyMessage(msg, SEND_TEXT.ordinal());
            myMessage.setMessage(sendTextmessage);
            myMessage.setTimeString(DateTimeUtils.getTimeStringAutoShort2(sendTextmessage.getCreateTime(), System.currentTimeMillis(), true));
            myMessage.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), "DeadPool", imgSend));
        sendTextmessage.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        // 在底部插入一条消息，第二个参数表示是否滚动到底部。
                        mAdapter.addToStart(myMessage, true);
                    } else {
                        Log.e("发送失败？", s);
                    }
                }
            });
            JMessageClient.sendMessage(sendTextmessage);
            if (mData != null) {
                mData.clear();
            }
    }


    //发送语音消息
    private void sendVoiceMessage(File voiceFile, int duration) {
        try {
            Message sendVoiceMessage  = conversation.createSendVoiceMessage(voiceFile,duration);

            final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());

            message.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getNickname(), imgSend));
            message.setMediaFilePath(voiceFile.getPath());
            message.setDuration(duration);
            message.setTimeString(DateTimeUtils.getTimeStringAutoShort2(sendVoiceMessage.getCreateTime(), System.currentTimeMillis(), true));
            message.setMessage(sendVoiceMessage);
            sendVoiceMessage.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        mAdapter.addToStart(message, true);
                        Log.i("message录音", "gotResult: "+message);
                    } else {

                        Log.e("发送失败？", s);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            //发送语音消息。
           JMessageClient.sendMessage(sendVoiceMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取贷方在线状态
    String state;

    public void friendState() {
        NetWorkManager.isFriendState(userName, new Callback<UserStateBean>() {
            @Override
            public void onResponse(@NonNull Call<UserStateBean> call, @NonNull Response<UserStateBean> response) {
                if (response.code() == 200) {
                    if (response.body().online) {
                        state = "[在线]";
                    } else {
                        state = "[离线]";
                    }
                    mTitleBarTitle.setText(userName + state);
                } else {
                    mTitleBarTitle.setText(userName);
                }
            }

            @Override
            public void onFailure(Call<UserStateBean> call, Throwable throwable) {
//                LogUtils.e(throwable.getMessage()+".");
            }
        });
    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }

                try {
                    View v = getCurrentFocus();
                    if (mManager != null && v != null) {
                        mManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }
}

