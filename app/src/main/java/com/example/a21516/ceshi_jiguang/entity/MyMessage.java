package com.example.a21516.ceshi_jiguang.entity;

import android.widget.TextView;

import java.util.HashMap;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jpush.im.android.api.model.Message;

public class MyMessage implements IMessage {

    private long id;
    private String text;
    private String timeString;
    private MessageType type;
    private IUser user;
    private String mediaFilePath;
    private long duration;
    private String progress;
    private Message message;
    private int position;
    private long msgID;

    public MyMessage(String text,MessageType type){
        this.text = text;
        this.type = type;
        this.id = System.currentTimeMillis() % 100;
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

    public void setDuration(long duration) {
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
    public MessageType getType() {
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