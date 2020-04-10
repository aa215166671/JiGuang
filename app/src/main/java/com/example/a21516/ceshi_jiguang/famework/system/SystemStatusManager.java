package com.example.a21516.ceshi_jiguang.famework.system;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.print.PrinterId;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import java.lang.reflect.Method;

/**
 * The type 系统管理器.
 */
public class SystemStatusManager {
    Activity mContext;

    public void setTranslucentStatus(int res){
        //如果sdk版本大于4.4则设置状态栏透明化  else会导致首页状态栏减少
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //设置透明的状态栏
            this.mContext.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.mContext.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager=new SystemStatusManager(this.mContext);
            tintManager.setStatusBarTintEnabled(true);
            //设置状态栏颜色
            tintManager.setStatusBarTinResource(res);
            this.mContext.getWindow().getDecorView().setFitsSystemWindows(true);

        }
    }


    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Class c= null;
            try {
                c = Class.forName("android.os.SystemProperties");
                Method m=c.getDeclaredMethod("get",String.class);
                m.setAccessible(true);
                sNavBarOverride=(String)m.invoke(null,"qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride=null;
            }
        }
    }

    // 默认系统栏淡色值
    public static final int DEFAULT_TINT_COLOR=0x99000000;
    public static String sNavBarOverride;
    //判断状态栏
    private boolean mStatusBarAvailable;
    //判断导航栏
    private boolean mNavBarAvailable;
    //设备导航键
    private final SystemBarConfig mConfig;

    private View mStatusBarTintView;
    private View mNavBarTintView;

    private boolean mStatusBarTintEnbled;
    private boolean mNavBarTintEnbled;


    /**
     * 构造器。在主机活动onCreate方法中调用它
     * 已设置内容视图。当
     * 将重新创建主机活动。
     *
     * @param activity the activity
     */
    //@TargetApi()当你使用一个较高版本才有的api（比工程中的minSdkVersion高)时，编译器会报错
    @TargetApi(19)
    public SystemStatusManager(Activity activity){
        this.mContext=activity;
        Window win=activity.getWindow();
        ViewGroup decorViewGroup=(ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //检查主题属性
            int[] attrs={
                    // 检查窗口半透明和窗口半透明导航状态
                    android.R.attr.windowTranslucentStatus,
                    android.R.attr.windowTranslucentNavigation };
            TypedArray a=activity.obtainStyledAttributes(attrs);
            try{
                mStatusBarAvailable=a.getBoolean(0,false);
                mNavBarAvailable=a.getBoolean(0,false);
            }
            finally {
                a.recycle();
            }
            //检查窗口标志
            WindowManager.LayoutParams winParams=win.getAttributes();
            int bits=WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags&bits)!=0){
                mStatusBarAvailable=true;
            }
            bits=WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags&bits)!=0){
                mNavBarAvailable=true;
            }
        }

        //设备可能没有虚拟导航键
        mConfig=new SystemBarConfig(activity,mStatusBarAvailable,mNavBarAvailable);
        if (!mConfig.hasNavigtionBar()){
            mNavBarAvailable=false;
        }
        if (mStatusBarAvailable){
            //设置状态栏视图
            setupStatusBarView(activity,decorViewGroup);
        }
        if (mNavBarAvailable){
            //设置导航栏视图
            setupNavBarView(activity,decorViewGroup);
        }
    }


    /**
    *启用系统状态栏的着色。
     * *如果平台运行的是果冻豆或更早版本，或半透明系统
    *主题或窗口标志中都未启用UI模式，
     *那么这个方法什么也做不了。
     *
     *@param enabled True启用着色，false禁用着色（默认）。
    */
    private void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnbled=enabled;
        if (mStatusBarAvailable){
            mStatusBarTintView.setVisibility(enabled?View.VISIBLE:View.GONE);
        }
    }

    /**
     *启用系统导航栏的着色。
     * *如果平台运行的是果冻豆或更早版本，或半透明系统
     *主题或窗口标志中都未启用UI模式，
     *那么这个方法什么也做不了。
     *
     *@param enabled True启用着色，false禁用着色（默认）。
     */
    private void setNavigationBarTintEnabled(boolean enabled) {
        mNavBarTintEnbled=enabled;
        if (mNavBarAvailable){
            mNavBarTintView.setVisibility(enabled?View.VISIBLE:View.GONE);
        }
    }
    /**
     * *将指定的颜色着色应用于所有系统用户界面栏。
     * *
     * *@param color背景色调的颜色。
     * */
    public void setTintColor(int color) {
        setStatusBarTintColor(color);
        setNavigationBarTintColor(color);
    }
    /**
     * *将指定的颜色着色应用于系统状态栏。
     * *
     * *@param color背景色调的颜色。
     * */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    /**
     *设置导航栏淡色。
     *
     *@param给颜色上色
     */
    public void setNavigationBarTintColor(int color) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundColor(color);
        }
    }

    /**
     *将指定的可绘制或颜色资源应用于系统状态栏。
     *
     *@param res 资源的标识符。
     */
    public void setTintResource(int res) {
        setStatusBarTintResource(res);
        setNavigationBarTintResource(res);
    }

    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    public void setNavigationBarTintResource(int res) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundResource(res);
        }
    }
    /**
     *将指定的drawable应用于系统状态栏。
     *
     *@param drawable 可将drawable用作背景，或使用null移除它。
     */

    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
        setNavigationBarTintDrawable(drawable);
    }

    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    public void setNavigationBarTintDrawable(Drawable drawable) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     *将指定的alpha应用于所有系统UI栏。
     *@param alpha 要使用的透明度
     **/

    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
        setNavigationBarAlpha(alpha);
    }

    @TargetApi(11)
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    @TargetApi(11)
    public void setNavigationBarAlpha(float alpha) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView.setAlpha(alpha);
        }
    }

    /**
     *将指定的可绘制或颜色资源应用于系统状态栏。
     *
     *@param res 资源的标识符。
     */

    private void setStatusBarTinResource(int res) {
        if (mStatusBarAvailable){
            mStatusBarTintView.setBackgroundColor(res);
        }
    }
    //设置状态栏视图
    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView=new View(context);
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,mConfig.getStatusBarHeight());
        params.gravity= Gravity.TOP;
        if (mNavBarAvailable&&!mConfig.isNavigationAtBottom()){
            params.rightMargin=mConfig.getNavigationBarWidth();
        }
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    //设置导航栏视图
    private void setupNavBarView(Context context, ViewGroup decorViewGroup) {
        mNavBarTintView=new View(context);
        LayoutParams params;
        if (mConfig.isNavigationAtBottom()){
            params=new LayoutParams(LayoutParams.MATCH_PARENT,mConfig.getNavigationBarHeight());
            params.gravity=Gravity.BOTTOM;
        }else {
            params=new LayoutParams(mConfig.getNavigationBarWidth(),LayoutParams.MATCH_PARENT);
            params.gravity=Gravity.RIGHT;
        }
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mNavBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mNavBarTintView);
    }

    /**
     *类，该类描述当前
     *设备配置。
     */
    private class SystemBarConfig {
        private static final String STATUS_BAR_HEIGHT_RES_NAME="status_bar_height";
        private static final String NAV_BAR_HEIGHT_RES_NAME="navigation_bar_height";
        private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME="navigation_bar_height_landscape";
        private static final String NAV_BAR_WIDTH_RES_NAME="navigation_bar_width";
        private static final String SHOW_NAV_BAR_RES_NAME="config_showNavigationBar";

        private final boolean mTranslucentStatusBar;
        private final boolean mTranslucentNavBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;
        private final boolean mHasNavigationBar;
        private final int mNavigationBarHeight;
        private final int mNavigationBarWidth;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;




        private SystemBarConfig(Activity activity,boolean translucentStatusBar,boolean traslucentNavBar){
            Resources res=activity.getResources();
            mInPortrait=(res.getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT);
            //获取屏幕最小宽度
            mSmallestWidthDp=getSmallestWidthDp(activity);
            //获取状态栏高度
            mStatusBarHeight=getInternalDimensionSize(res,STATUS_BAR_HEIGHT_RES_NAME);

            mActionBarHeight=getActionBarHeight(activity);
            //获取导航栏高度
            mNavigationBarHeight=getNavigationBarHeight(activity);
            //获取导航栏宽度
            mNavigationBarWidth=getNavigationBarWidth(activity);
            //导航栏
            mHasNavigationBar=(mNavigationBarHeight>0);

            mTranslucentStatusBar=translucentStatusBar;

            mTranslucentNavBar=traslucentNavBar;

        }



        //获取屏幕最小宽度
        @SuppressLint("NewApi")
        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics metrics=new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            }else{
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
            float widthDp=metrics.widthPixels/metrics.density;
            float heightDp=metrics.heightPixels/metrics.density;
            return Math.min(widthDp,heightDp);
        }

        //获取状态栏高度
        private int getInternalDimensionSize(Resources res,String key) {
            int result=0;
            int resourceId=res.getIdentifier(key,"dimen","android");
            if (resourceId>0){
                result=res.getDimensionPixelOffset(resourceId);
            }
            return result;
        }
        /*
        * 当前开发代号：Build.VERSION.CODENAME
        *源码控制版本号：Build.VERSION.RELEASE
        *版本号：Build.VERSION.SDK_INT
        * */
        //获去导航栏的高度
        @TargetApi(14)
        private int getActionBarHeight(Context context) {
            int result=0;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                TypedValue tv=new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.actionBarSize,tv,true);
                result=TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
            }
            return result;
        }

        //获去导航栏的高度
        @TargetApi(14)
        private int getNavigationBarHeight(Context context) {
            Resources res=context.getResources();
            int result=0;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                if (hasNavBar(context)){
                    String key;
                    if (mInPortrait){
                        key=NAV_BAR_HEIGHT_RES_NAME;
                    }else {
                        key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                    }
                    return getInternalDimensionSize(res,key);
                }
            }
            return  result;
        }

        //检查覆盖标志 状态栏和导航条是否覆盖
        @TargetApi(14)
        private boolean hasNavBar(Context context) {
            Resources res=context.getResources();
            int resourceId=res.getIdentifier(SHOW_NAV_BAR_RES_NAME,"bool","android");
            if (resourceId!=0){
                boolean hasNav=res.getBoolean(resourceId);
                if ("1".equals(sNavBarOverride)){
                    hasNav=false;

                }else if ("0".equals(sNavBarOverride)){
                    hasNav=true;
                }
                return hasNav;
            }else{
                return !ViewConfiguration.get(context).hasPermanentMenuKey();
            }
        }

        //获取导航栏的宽度
        @TargetApi(14)
        private int getNavigationBarWidth(Context context) {
            Resources res=context.getResources();
            int result=0;
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                if (hasNavBar(context)){
                    return getInternalDimensionSize(res,NAV_BAR_WIDTH_RES_NAME);
                }
            }
            return result;
        }

        /**
         *这个设备有系统导航栏吗？
         *
         *@如果此设备使用软键导航，则返回True；否则返回False。
         */
        public boolean hasNavigtionBar() {
            return mHasNavigationBar;
        }

        /**
         * Gets navigation bar width.
         *
         * *获取系统导航栏垂直放置在屏幕上时的宽度。
         * *@返回导航栏的宽度（像素）。如果设备没有
         * *软导航键，这将始终返回0。
         * @return the navigation bar width
         */
        public int getNavigationBarWidth() {
            return mNavigationBarWidth;
        }

        /**              
          *获取系统状态栏的高度。              
          *              
         *@返回状态栏的高度（像素）。              
          */
        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }

        /**
         * *如果导航栏出现在当前屏幕的底部
         * *设备配置？导航栏可能出现在
         * *某些配置中的屏幕。
         * *
         * *@return True如果导航出现在屏幕底部，则返回False，否则返回False。
         * */
        public boolean isNavigationAtBottom() {
            return (mSmallestWidthDp>=600||mInPortrait);
        }

        /**
         * *获取系统导航栏的高度。
         * *
         * *@返回导航栏的高度（像素）。如果设备没有
         * *软导航键，这将始终返回0。
         * */
        public int getNavigationBarHeight() {
            return mNavigationBarHeight;
        }
    }
}
