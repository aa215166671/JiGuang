package com.example.a21516.ceshi_jiguang.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.activity.ChatMsgActivity;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsEditText;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.menu.MenuManager;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;


public class ChatView extends RelativeLayout{
    private TextView mTitle;
    private MessageList mMesgList;
    //聊天界面输入框组件
    private ChatInputView mChatInput;
    private LinearLayout mMenuLl;
    //录音按钮
    private RecordVoiceButton mRecordVoiceBtn;

     private boolean mHasInit;
    private boolean mHasKeyboard;
    private int mHeight;
     private OnSizeChangedListener mSizeChangedListener;



    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule(){

        mTitle=(TextView) findViewById(R.id.title_bar_title);
        mMesgList=(MessageList) findViewById(R.id.msg_list);
        mMenuLl=(LinearLayout) findViewById(R.id.aurora_ll_menuitem_container);
        mChatInput=(ChatInputView) findViewById(R.id.chat_input);

        mRecordVoiceBtn=mChatInput.getRecordVoiceButton();

        /*
        * 当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
        * 并通过Adapter的增删改插方法去刷新RecyclerView，
        * 而不是通过notifyDataSetChanged()。
        * 其实可以直接设置为true，
        * 当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
        */
        mMesgList.setHasFixedSize(false);
        //添加Menu View
//        MenuManager menuManager = mChatInput.getMenuManager();
//        menuManager.addCustomMenu("MY_CUSTOM",R.layout.menu_text_item,R.layout.menu_text_feature);
//        // Custom menu order
//        menuManager.setMenu(Menu.newBuilder().
//                customize(true).
//                setRight(Menu.TAG_SEND).
//                setBottom(Menu.TAG_VOICE,Menu.TAG_EMOJI,Menu.TAG_GALLERY,Menu.TAG_CAMERA,"MY_CUSTOM").
//                build());
//        menuManager.setCustomMenuClickListener(new CustomMenuEventListener() {
//            @Override
//            public boolean onMenuItemClick(String tag, MenuItem menuItem) {
//                //Menu feature will not be show shown if return false；
//                return true;
//            }
//            @Override
//            public void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature) {
//                if(visibility == View.VISIBLE){
//                    // Menu feature is visible.
//                }else {
//                    // Menu feature is gone.
//                }
//            }
//        });
    }
    
    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setMenuClickListener(OnMenuClickListener listener){
        mChatInput.setMenuClickListener(listener);
    }
    public void setAdapter(MsgListAdapter adapter){
        mMesgList.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        mMesgList.setLayoutManager(layoutManager);
    }
    public void  setRecordVoiceFile(String path,String fileName){
        mRecordVoiceBtn.setVoiceFilePath(path,fileName);
    }


    public void setCameraCaptureFile(String path,String fileName){
        mChatInput.setCameraCaptureFile(path, fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mChatInput.setRecordVoiceListener(listener);
    }
    public void setOnCameraCallbackListener(OnCameraCallbackListener listener){
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mMesgList.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener) {
        mChatInput.setOnClickEditTextListener(listener);
    }


    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mSizeChangedListener = listener;
    }

    @Override
    public boolean performClick() {
         super.performClick();
        return true;
    }

    public ChatInputView getChatInputView() {
        return mChatInput;
    }

    public MessageList getmMesgList() {
        return mMesgList;
    }


    public void setMenuHeight(int height) {
        mChatInput.setMenuContainerHeight(height);
    }

        @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeChangedListener != null) {
            mSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }


    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
