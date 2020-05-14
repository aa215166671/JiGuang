package com.example.a21516.ceshi_jiguang.view;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;

public class MyViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>  implements MsgListAdapter.DefaultMessageViewHolder{
        protected TextView mMsgTv;
        protected TextView mDateTv;
        protected TextView mDisplayNameTv;
        protected CircleImageView mAvatarIv;
        protected ImageButton mResendIb;
        protected ProgressBar mSendingPb;
        protected boolean  mIsSender;

        public MyViewHolder(View itemView,boolean isSender){
            super(itemView);
            this.mIsSender=isSender;
            this.mMsgTv=(TextView) itemView.findViewById(cn.jiguang.imui.R.id.aurora_tv_msgitem_message);
            this.mDateTv=(TextView) itemView.findViewById(cn.jiguang.imui.R.id.aurora_tv_msgitem_date);
            this.mAvatarIv=(CircleImageView) itemView.findViewById(cn.jiguang.imui.R.id.aurora_iv_msgitem_avatar);
            this.mResendIb=(ImageButton) itemView.findViewById(cn.jiguang.imui.R.id.aurora_ib_msgitem_resend);
            this.mSendingPb=(ProgressBar)itemView.findViewById(cn.jiguang.imui.R.id.aurora_pb_msgitem_sending);
        }

    @Override
    @SuppressLint("WrongConstant")
    public void onBind(final MESSAGE message) {
            this.mMsgTv.setText(message.getText());
            if (message.getTimeString() !=null){
                this.mDateTv.setText(message.getTimeString());
            }
            boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null && !message.getFromUser().getAvatarFilePath().isEmpty();
                if (isAvatarExists && this.mImageLoader != null){
                    this.mImageLoader.loadAvatarImage(this.mAvatarIv,message.getFromUser().getAvatarFilePath());
                }else if (this.mImageLoader == null){
                    /*
                    VISIBLE:0  意思是可见的
                    INVISIBILITY:4 意思是不可见的，但还占着原来的空间
                    GONE:8  意思是不可见的，不占用原来的布局空间
                    * */
                    this.mAvatarIv.setVisibility(8);
                }

                if (!this.mIsSender){
                    if (this.mDisplayNameTv.getVisibility() == 0){
                        this.mDisplayNameTv.setText(message.getFromUser().getDisplayName());
                    }
                }else{

                }
                this.mMsgTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyViewHolder.this.mMsgClickListener != null){
                            MyViewHolder.this.mMsgClickListener.onMessageClick(message);
                        }
                    }
                });

                this.mMsgTv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (MyViewHolder.this.mMsgLongClickListener != null){
                            MyViewHolder.this.mMsgLongClickListener.onMessageLongClick(itemView,message);
                        }
                        return true;
                    }
                });

                this.mAvatarIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyViewHolder.this.mAvatarClickListener != null){
                            MyViewHolder.this.mAvatarClickListener.onAvatarClick(message);
                        }
                    }
                });

        }

    @Override
    @SuppressLint("WrongConstant")
    public void applyStyle(MessageListStyle style) {
            this.mMsgTv.setMaxWidth((int)((float)style.getWindowWidth() * style.getBubbleMaxWidth()));
            if (this.mIsSender){
                this.mMsgTv.setBackground(style.getSendBubbleDrawable());
                this.mMsgTv.setTextColor(style.getSendBubbleTextColor());
                this.mMsgTv.setTextSize(style.getSendBubbleTextSize());
                this.mMsgTv.setPadding(style.getSendBubblePaddingLeft(),style.getReceiveBubblePaddingTop(),style.getReceiveBubblePaddingRight(),style.getReceiveBubblePaddingBottom());

                if (style.getSendBubbleDrawable() != null){
                    this.mSendingPb.setProgressDrawable(style.getReceiveBubbleDrawable());
                }
                if (style.getSendingIndeterminateDrawable() != null){
                    this.mSendingPb.setIndeterminateDrawable(style.getSendingIndeterminateDrawable());
                }
            }else{
                this.mMsgTv.setBackground(style.getSendBubbleDrawable());
                this.mMsgTv.setTextColor(style.getSendBubbleTextColor());
                this.mMsgTv.setTextSize(style.getSendBubbleTextSize());
                this.mMsgTv.setPadding(style.getSendBubblePaddingLeft(),style.getReceiveBubblePaddingTop(),style.getReceiveBubblePaddingRight(),style.getReceiveBubblePaddingBottom());
            }

            this.mDateTv.setTextSize(style.getDateTextSize());
            this.mDateTv.setTextColor(style.getDateTextColor());
            ViewGroup.LayoutParams layoutParams = this.mAvatarIv.getLayoutParams();
            layoutParams.width=style.getAvatarWidth();
            layoutParams.height=style.getAvatarHeight();
            this.mAvatarIv.setLayoutParams(layoutParams);
    }
    public TextView getMsgTextView(){
            return this.mMsgTv;
    }

    public CircleImageView getAvatarIv() {
        return mAvatarIv;
    }
}
