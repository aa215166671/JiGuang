package com.example.a21516.ceshi_jiguang.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.PortUnreachableException;

@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView{

    //按统一比例缩放图片
    private static final ScaleType SCALE_TYPE=ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG =Bitmap.Config.ARGB_8888;

    private static final int COLORDRAWABLE_DIMENSION=2;
    //边框颜色为黑色
    private static final int DEFAULT_BORDER_COLOR= Color.BLACK;
    //填充背景为透明
    private static final int DEFAULT_FILL_COLOR=Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    //设置边框的宽度
    private static final int DEFAULT_BORDER_WIDTH=0;

    //边框颜色
    private int mBorderColor=DEFAULT_BORDER_COLOR;
    //填充的颜色
    private int mFillColor=DEFAULT_FILL_COLOR;
    //边框的宽度
    private int mBorderWidth=DEFAULT_BORDER_WIDTH;

    //画刷 描绘用的
    private final Paint mBorderPaint=new Paint();
    //填充画刷
    private final Paint mFillPaint= new Paint();
    //
    private final Paint mBitmapPaint=new Paint();
    //
    private final Matrix mShaderMatrix=new Matrix();

    //设置图片
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private final RectF mBorderRect=new RectF();
    private final RectF mDrawableRect=new RectF();

    private float mBorderRadius;
    private float mDrawableRadius;


    //颜色滤镜
    private ColorFilter mColorFilter;

    private boolean mReady;
    //设置挂起
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    private boolean mDisableCircularTransformation;

  public CircleImageView(Context context){
      super(context);
      init();
  }

  public CircleImageView (Context context,AttributeSet attrs){
      this(context,attrs,0);
  }

  public CircleImageView(Context context,AttributeSet attrs,int defStyle){
      super(context,attrs,defStyle);

      init();
  }



    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        //if adjustViewBounds=true  图片宽高比有可能发生变化
        if (adjustViewBounds){
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDisableCircularTransformation){
            super.onDraw(canvas);
            return;
        }
            if (mBitmap == null){
            return;
            }
            if (mFillColor!= Color.TRANSPARENT){
            canvas.drawCircle(mDrawableRect.centerX(),mDrawableRect.centerY(),mDrawableRadius,mFillPaint);
            }
            canvas.drawCircle(mDrawableRect.centerX(),mDrawableRect.centerY(),mDrawableRadius,mBitmapPaint);
        if (mBorderWidth>0){
            canvas.drawCircle(mBorderRect.centerX(),mBorderRect.centerY(),mBorderRadius,mBorderPaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    //边框颜色
    public int getBorderColor(){
        return mBorderColor;
    }

    //设置边框颜色
    public void setBorderColor(@ColorInt int borderColor){
        if (borderColor==mBorderColor){
            return;
        }

        mBorderColor=borderColor;
        mBorderPaint.setColor(mBorderColor);
        //用于触发视图的绘制刷新
        invalidate();
    }

    @Deprecated
    //设置边框颜色
    public void setBorderColorResource(@ColorRes int borderColorRes){
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    /**
     *返回绘制在圆形可绘制图形后面的颜色。
     *
     *@返回在抽屉后面绘制的颜色
     *@不推荐的填充颜色支持将在将来被删除
     */

    @Deprecated
    public int getFillColor() {
        return mFillColor;
    }
    /**
     *设置要在圆形可拉伸的后面绘制的颜色。请注意
     *如果drawable不透明或未设置drawable，则此操作无效。
     *
     *@param fillColor 要在可绘制的后面绘制的颜色
     *@不推荐的填充颜色支持将在将来被删除              */
    @Deprecated
    public void setFillColor(@ ColorInt int fillColor) {
        if (fillColor==mFillColor){
            return;
        }
        mFillColor=fillColor;
        mFillPaint.setColor(fillColor);
        invalidate();
    }
    /**
     *设置要在圆形可拉伸的后面绘制的颜色。请注意
     *如果drawable不透明或未设置drawable，则此操作无效。
     *
     *@param fillColorRes 要解析为颜色的颜色资源
     *拉在可拉的后面
     *@不推荐的填充颜色支持将在将来被删除
     */
    @Deprecated
    public void setFillColorResource(@ColorRes int fillColorRes){
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }

    public int getBorderWidth(){
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth){
        if(borderWidth == mBorderWidth){
            return;
        }
        mBorderWidth=borderWidth;
        setup();
    }

    //边界是否覆盖
    public boolean isBorderOverlay(){
        return mBorderOverlay;
    }

    //设置边界是否覆盖
    public void setBorderOverlay(boolean borderOverlay){
        if (borderOverlay == mBorderOverlay){
            return;
        }

        mBorderOverlay=borderOverlay;
        setup();
    }
    //设置外边圆环是否压住内部圆形图片
    public boolean isDisableCircularTransformation(){
        return mDisableCircularTransformation;
    }

    //设置外边圆环是否压住内部圆形图片
    public void setDisableCircularTransformation(boolean disableCircularTransformation){
        if ( mDisableCircularTransformation == disableCircularTransformation){
            return;
        }
        mDisableCircularTransformation=disableCircularTransformation;
        //更新Bitmap
       initializeBitmap();
    }

    //设置图片
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable( Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI( Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    ////颜色滤镜

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf== mColorFilter){
            return;
        }
        mColorFilter=cf;
        applyColorFilter();
        invalidate();
    }
    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }




    //应用颜色过滤器
    private void applyColorFilter() {
        if (mBitmapPaint!=null){
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if (drawable == null){
            return null;
        }
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable){
                bitmap= Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,COLORDRAWABLE_DIMENSION,BITMAP_CONFIG);
            }else {
                bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),BITMAP_CONFIG);
            }
            Canvas canvas=new Canvas(bitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //更新
    private void initializeBitmap() {
        if (mDisableCircularTransformation){
            mBitmap =null;
        }else{
            mBitmap=getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    //设置
    private void setup(){
        if (!mReady){
            mSetupPending=true;
            return;
        }
        if (getWidth()==0 && getHeight()==0){
            return;
        }
        if (mBitmap == null) {
            invalidate();
            return;
        }
        /*
         * bitmap用来指定图案，
         * tileX用来指定当X轴超出单个图片大小时时所使用的重复策略，
         * tileY用于指定当Y轴超出单个图片大小时时所使用的重复策略
         *
         * TileMode.CLAMP:用边缘色彩填充多余空间  拉伸
         *TileMode.REPEAT:重复原图像来填充多余空间  重复
         *TileMode.MIRROR:重复使用镜像模式的图像来填充多余空间  镜像
         */
        mBitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        //抗锯齿
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        /*
         * Paint.Style.FILL：填充内部
         *Paint.Style.FILL_AND_STROKE  ：填充内部和描边
         *Paint.Style.STROKE  ：描边
         */
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        //取的原图片的宽高
        mBitmapWidth=mBitmap.getWidth();
        mBitmapHeight=mBitmap.getHeight();

        mBorderRect.set(calculateBounds());

        //计算整个圆形带Border部分的最小半径，取mBorderRect的宽高减去一个边缘大小的一半的较小值
        mBorderRadius=Math.min((mBorderRect.height()-mBorderWidth)/2.0f,(mBorderRect.width()-mBorderWidth)/2.0f);

        mDrawableRect.set(mBorderRect);
        //初始图片显示区域为mBorderRect（CircleImageView中图片区域的实际大小
        if (!mBorderOverlay&&mBorderWidth>0){
            mDrawableRect.inset(mBorderWidth-1.0f,mBorderWidth-1.0f);
        }
        //计算内圆最小半径，即去除边框后的Rect（内部图片Rect->mDrawableRect）宽度的半径
        mDrawableRadius=Math.min(mDrawableRect.height()/2.0f,mDrawableRect.width()/2.0f);

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    //设置外环矩形
    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
}
