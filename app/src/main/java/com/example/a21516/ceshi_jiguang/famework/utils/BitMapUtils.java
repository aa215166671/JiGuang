package com.example.a21516.ceshi_jiguang.famework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitMapUtils {
    //图片转化成String
    public  static String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] appicon=baos.toByteArray();
        return Base64.encodeToString(appicon,Base64.DEFAULT);
    }

    //string转化成bitmap
    public static Bitmap string2bitmp(String str){
        Bitmap bitmap=null;
        try{
            byte[] bitmapArray;
            bitmapArray=Base64.decode(str,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
            return bitmap;
        }catch (Exception e){
          return null;
        }
    }
    //drawble 转化成bitmap

    public static Bitmap drawable2Bitmap(Drawable drawable){
        //取drawable 的长宽
        int w=drawable.getIntrinsicWidth();
        int h=drawable.getIntrinsicHeight();

        //取drawable 的颜色格式
        Bitmap.Config config=drawable.getOpacity()!= PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        //建立对应的bitmap
        Bitmap bitmap=Bitmap.createBitmap(w,h,config);
        //建立对应 bitmap的画布
        Canvas canvas=new Canvas(bitmap);

        drawable.setBounds(0,0,w,h);
        //把drawable 内容滑道画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
