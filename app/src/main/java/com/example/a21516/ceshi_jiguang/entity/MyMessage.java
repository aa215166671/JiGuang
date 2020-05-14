package com.example.a21516.ceshi_jiguang.entity;

import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Message;

public class MyMessage implements IMessage {

    private long id;
    private String text;
    private String timeString;
    private int type;
    private IUser user;
    private String mediaFilePath;
    private int duration;
    private String progress;
    private Message message;
    private int position;
    private long msgID;

    private MessageStatus mMsgStatus = MessageStatus.CREATED;

    public MyMessage(String text,int type){
        this.text = text;
        this.type = type;
        this.id = System.currentTimeMillis() % 100000;
    }

    public static MyMessage SendTextMesg(String text,int type){
        MyMessage message =new MyMessage(text,type);
        return message;
    }
    @Override
    public String getMsgId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getFromUser() {
        if (user == null){
            return new DefaultUser("0","user1",null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setMediaFilePath(String path) {
        this.mediaFilePath = path;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public HashMap<String,String> getExtras() {
        return  null;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String getTimeString() {
        return timeString;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return MessageStatus.SEND_SUCCEED;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return mediaFilePath;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;

    }

    /**
    *设置消息状态。发送消息后，请更改状态，以便进度栏消失。
     * * @param messageStatus
     * {@link cn.jiguang.imui.commons.models.IMessage.MessageStatus}
     * */

    public void setMessageStatus(MessageStatus messageStatus) {

        this.mMsgStatus = messageStatus;

    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", timeString='" + timeString + '\'' +
                ", type=" + type +
                ", user=" + user +
                ", mediaFilePath='" + mediaFilePath + '\'' +
                ", duration=" + duration +
                ", progress='" + progress + '\'' +
                ", position=" + position +
                ", msgID=" + msgID +
                '}';
    }
}
