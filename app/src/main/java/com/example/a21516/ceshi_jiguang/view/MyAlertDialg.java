package com.example.a21516.ceshi_jiguang.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.example.a21516.ceshi_jiguang.famework.utils.UIUtils;

public class MyAlertDialg {
    private Context context;
    private String[] strings;
    private DialogInterface.OnClickListener onClick;

    AlertDialog dialog;
    private Window window;
    private WindowManager.LayoutParams params;

    public MyAlertDialg(Context context, String[] strings, DialogInterface.OnClickListener onClick){
        this.context=context;
        this.strings=strings;
        this.onClick=onClick;
    }

    //窗体位置
    public void initDialog(int gravity){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setItems(strings,onClick);
        dialog=builder.create();
        dialog.show();
        initGravity(gravity);
    }
    //窗体位置
    public void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(strings, onClick);
        dialog = builder.create();
        dialog.show();
    }


    /**
     *
     * @param width 宽度
     * @param hight 高度
     * @param x x坐标
     * @param y y坐标
     */
    public void dialogSize(int width,int hight,int x,int y){
        if(width!=0){
            params.width= UIUtils.dip2px(context,width);
        }
        if (hight!=0){
            params.height=UIUtils.dip2px(context,hight);
        }
        if (x!=0){
            params.x = UIUtils.dip2px(context,x);
        }
        if (y!=0){
            params.y = UIUtils.dip2px(context,y);
        }
        dialog.getWindow().setAttributes(params);
    }

    private void initGravity(int gravity) {
        window=dialog.getWindow();
        params=window.getAttributes();
        if (gravity!=0){
            window.setGravity(gravity);
        }
    }
}
