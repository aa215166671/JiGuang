package com.example.a21516.ceshi_jiguang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.a21516.ceshi_jiguang.R;

public class EmptyView extends RelativeLayout{

    public EmptyView(Context context) {
        super(context);
        initView(context);
    }
    public EmptyView(Context context, AttributeSet attrs){
        super(context,attrs);
        initView(context);

    }

    private void initView(Context context) {
        View.inflate(context,R.layout.empty_view,null);
    }
}
