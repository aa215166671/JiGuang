package com.example.a21516.ceshi_jiguang.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a21516.ceshi_jiguang.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerImageActivity extends Activity {
    ViewPager mPhotoVp;
    TextView mPhotoNumTv;

    SimpleDraweeView[] mPhotoSdvs;

 //   private ArrayList<String> mPhotoList;
 //   private int mSelectPosition;


    private List<String> mPathList = new ArrayList<>();
    private List<String> mMsgIdList = new ArrayList<>();

    private PagerAdapter mPagerAdapter;
    private int Position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_image);
        Fresco.initialize(this);
        initView();
    }


    public void initView() {
        mPhotoNumTv = findViewById(R.id.tv_photo_num);

        Intent intent = getIntent();
        if (intent != null) {

//            mPhotoList = intent.getStringArrayListExtra("photoList");
//            mSelectPosition = intent.getIntExtra("position",0);

            mPathList = intent.getStringArrayListExtra("photoList");
            mMsgIdList = intent.getStringArrayListExtra("idList");
            String msgId = intent.getStringExtra("position");
            Position = mMsgIdList.indexOf(msgId);
            mPhotoSdvs = new SimpleDraweeView[mPathList.size()];

            mPhotoVp = findViewById(R.id.vp_photo);

            mPhotoNumTv.setText(Position + 1 + "/" + mPathList.size());
            mPhotoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Position = position;
                    mPhotoNumTv.setText(Position + 1 + "/" + mPathList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mPagerAdapter = new PagerAdapter() {
                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    SimpleDraweeView photoSdv = new SimpleDraweeView(ViewPagerImageActivity.this);
                    mPhotoSdvs[position] = photoSdv;
                    GenericDraweeHierarchy hierarchy = photoSdv.getHierarchy();
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                    photoSdv.setImageURI(Uri.parse(mPathList.get(position)));
                    Log.i("url", "instantiateItem: "+Uri.parse(mPathList.get(position)));
                    container.addView(photoSdv);

                    photoSdv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });

                    return photoSdv;
                }

                @Override
                public int getCount() {
                    return mPathList.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView(mPhotoSdvs[position]);
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }
            };
            mPhotoVp.setAdapter(mPagerAdapter);
            setDefaultItem(Position);
        } else {
            return;
        }
    }


    private void setDefaultItem(int position) {
        try {
            Class c = Class.forName("android.support.v4.view.ViewPager");
            Field field = c.getDeclaredField("mCurItem");
            field.setAccessible(true);
                field.setInt(mPhotoVp, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPagerAdapter.notifyDataSetChanged();
        mPhotoVp.setCurrentItem(position);
    }
}
