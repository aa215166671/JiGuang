package com.example.a21516.ceshi_jiguang.view;

import android.content.Context;
import android.content.IntentFilter;
import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsEditText;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jmessage.support.google.protobuf.MessageLite;

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

    private OnKeyboardChangedListener mKeyboardListener;
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

        mMesgList=(MessageList) findViewById(R.id.msg_list);
        /*
        * 当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
        * 并通过Adapter的增删改插方法去刷新RecyclerView，
        * 而不是通过notifyDataSetChanged()。
        * （其实可以直接设置为true，
        * 当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
        */
        mMesgList.setHasFixedSize(false);
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
        mChatInput.setCameraCaptureFile(path,fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener){
        mRecordVoiceBtn.setRecordVoiceListener(listener);
    }

    public void setKeyboardChangedListener(OnKeyboardChangedListener listener) {
        mKeyboardListener = listener;
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener){
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener){
        mSizeChangedListener=listener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        super.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener){
        mChatInput.setOnClickEditTextListener(listener);
    }

    @Override
    public boolean performClick() {
         super.performClick();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeChangedListener != null){
            mSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit){
            mHasInit = true;
            mHeight = b;
            if (null != mKeyboardListener){

            }else{
                if (null != mKeyboardListener){

                }
                mHeight = mHeight < b ? b : mHeight;
            }
            if (mHasInit && mHeight > b){
                mHasKeyboard =true;
                if (null != mKeyboardListener){

                }
            }

            if (mHasInit && mHasKeyboard && mHeight == b){
                mHasKeyboard = false;
                if ( null != mKeyboardListener){

                }
            }
        }
    }
    public ChatInputView getChatInputView() {
        return mChatInput;
    }

    public ChatInputView getmChatInput() {
        return mChatInput;
    }

    public MessageList getmMesgList() {
        return mMesgList;
    }

    public void setMenuHeight(int height){
        mChatInput.setMenuContainerHeight(height);
    }

    /**
     *键盘状态更改将调用onKeyBoardStateChanged
     */
    public interface OnKeyboardChangedListener{
        /**
         *软键盘状态更改将调用此回调，使用此回调做逻辑处理。
         *
         *@param state 三种状态：init、show、hide。
         */
        public void onKeyBoardStateChanged(int state);
    }

    public interface OnSizeChangedListener{
        void onSizeChanged(int w,int h,int oldw,int oldh);
    }
}
