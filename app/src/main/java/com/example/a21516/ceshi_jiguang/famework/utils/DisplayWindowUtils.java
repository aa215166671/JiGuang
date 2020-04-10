package com.example.a21516.ceshi_jiguang.famework.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

public class DisplayWindowUtils {
    private static DisplayWindowUtils instance;
    private Activity mActivity;
    private Context mContext;
    private int mStatusHeight;



    private DisplayWindowUtils(Activity mActivity){
        this.mActivity=mActivity;
    }

    public static DisplayWindowUtils getInstance(Activity mActivity){
        if (instance == null){
            instance = new DisplayWindowUtils(mActivity);
        }
        return instance;
    }

    public final int[] getScreenSize(){
        int[] size = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }

    public final static int getWindowWidth(Activity mActivity){
        DisplayMetrics dm =new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels;
    }

    public final static int getWindowHeight(Activity mActivity){
        DisplayMetrics dm =new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.heightPixels;
    }
}
