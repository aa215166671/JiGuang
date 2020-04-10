package com.example.a21516.ceshi_jiguang.famework.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.a21516.ceshi_jiguang.famework.base.BaseApplication;

import java.io.File;

public class SharedPrefHelper {
    private static SharedPrefHelper sharedPrefHelper=null;
    private static SharedPreferences sharedPreferences;

    public static synchronized SharedPrefHelper getInstance(){
        if (null==sharedPrefHelper){
            sharedPrefHelper=new SharedPrefHelper();
        }
        return sharedPrefHelper;
    }

    private SharedPrefHelper(){
        sharedPreferences= BaseApplication.baseApplication
                .getSharedPreferences("SPH_NAME", Context.MODE_PRIVATE);

    }

    //保存账号
    public void setUserId(String gusetId){
        sharedPreferences.edit().putString("username",gusetId).commit();
    }
    public String getUserId(){
        return sharedPreferences.getString("username","");
    }
//保存密码
    public void setUserPW(String gusetId){
        sharedPreferences.edit().putString("userPW",gusetId).commit();
    }
    public String getUserPW(){
        return sharedPreferences.getString("userPW","");
    }

    //名称
    public void setNakeName(String gusetId){
        sharedPreferences.edit().putString("userNakeName",gusetId).commit();
    }
    public String getNakeName(){
        return sharedPreferences.getString("userNakeName","");
    }

    //漫游记录 开启状态
    public void setRoaming(boolean flag){
        sharedPreferences.edit().putBoolean("roaming",flag).commit();
    }
    public boolean getRoaming(){
        return sharedPreferences.getBoolean("roaming",false);
    }

    //推送开启状态
    public void setPush(boolean flag){
        sharedPreferences.edit().putBoolean("push",flag).commit();
    }
    public boolean getPush(){
        return sharedPreferences.getBoolean("push",false);
    }

    //声音推送开启状态
    public void setMusic(boolean flag){
        sharedPreferences.edit().putBoolean("push_music",flag).commit();
    }
    public boolean getMusic(){
        return sharedPreferences.getBoolean("push_music",false);
    }
    //震动开启状态
    public void setVib(boolean flag){
        sharedPreferences.edit().putBoolean("push_vib",flag).commit();
    }
    public boolean getVib(){
        return sharedPreferences.getBoolean("push_vib",false);
    }

    //设置AppKEY
    public void setAppKey(String gusetId){
        sharedPreferences.edit().putString("appkey",gusetId).commit();
    }
    public String getAppKey(){
        return sharedPreferences.getString("appkey","");
    }


}
