package com.example.a21516.ceshi_jiguang.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.entity.RegionBean;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.famework.helper.SharedPrefHelper;
import com.example.a21516.ceshi_jiguang.famework.utils.StringUtils;
import com.example.a21516.ceshi_jiguang.famework.utils.TimeUtils;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class UserEditActivity extends BaseActivity{
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.edit_user_avatar)
    TextView mEditUserAvatar;
    @BindView(R.id.edit_user_signature)
    EditText mEditUserSignature;
    @BindView(R.id.edit_user_nackName)
    EditText mEditUserNackName;
    @BindView(R.id.edit_user_gender)
    TextView mEditUserGender;
    @BindView(R.id.edit_user_birthday)
    TextView mEditUserBirthday;
    @BindView(R.id.edit_user_address)
    TextView mEditUserAddress;
    @BindView(R.id.bottom_bar_left)
    RelativeLayout mBottomBarLeft;
    @BindView(R.id.bottom_bar_tv2)
    TextView mBottomBarTv2;

    private SharedPrefHelper helper;
    private Activity mActivity;
    private UserInfo userInfo;
    private String mFilePath;

    private OptionsPickerView<String> mOptionsPickerView;
    //头像操作
    private Dialog dialog;
     Uri contentUri;
     private File file;
     private static final int PHOTO_TK = 0;
     private static final int PHOTO_PZ =1;
     private static final int PHOTO_CLIP= 2;

     private Bitmap photo1;
     //地区
    private ArrayList<RegionBean> optionsItems=new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items2 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items3 = new ArrayList<>();





    @Override
    protected int setContentView() {
        return R.layout.activity_edit_user;
    }

    @Override
    protected void initView() {
        mActivity=UserEditActivity.this;
        helper=SharedPrefHelper.getInstance();
        userInfo = JMessageClient.getMyInfo();
        mFilePath= Environment.getExternalStorageDirectory().getPath();//获取sdc的路径
        mFilePath= mFilePath + "/" +"temp.jpg";
        initBar();
        initData();
        //地区数据
        initGson();
    }


    private void initBar() {
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleBarTitle.setText("编辑资料");
        mBottomBarLeft.setVisibility(View.GONE);
        mBottomBarTv2.setText("保存资料");
    }

    @Override
    protected void initData() {
        mEditUserNackName.setText(userInfo.getNickname()+"");
        Log.i("生日", "initData: "+ TimeUtils.ms2date("yyyy-MM-dd", userInfo.getBirthday()));
        mEditUserBirthday.setText(TimeUtils.ms2date("yyyy-MM-dd", userInfo.getBirthday()));
        mEditUserGender.setText(StringUtils.constant2String(userInfo.getGender().name()));
        mEditUserSignature.setText(userInfo.getSignature()+"");
        mEditUserAddress.setText(userInfo.getAddress()+"");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.title_bar_back,R.id.edit_user_avatar,R.id.edit_user_gender,R.id.edit_user_birthday,R.id.edit_user_address,R.id.bottom_bar_tv2})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.title_bar_back:
                finish();
                break;
            case R.id.edit_user_avatar:
                showHeadDialog();
                break;
            case R.id.edit_user_gender:
                initPickViewGender();
                break;
            case R.id.edit_user_birthday:
                initTimePickView();
                break;
            case R.id.edit_user_address:
                initRegionPickView();
                break;
            case R.id.bottom_bar_tv2:
                showProgressDialog("正在保存....");
                userInfo.setAddress(mEditUserAddress.getText()+"");
                userInfo.setGender(UserInfo.Gender.valueOf(StringUtils.string2contant(mEditUserGender.getText().toString())));
                userInfo.setBirthday(TimeUtils.date2ms("yyyy-MM-dd", mEditUserBirthday.getText().toString()));
                userInfo.setSignature(mEditUserSignature.getText()+"");
                userInfo.setNickname(mEditUserNackName.getText()+"");
                JMessageClient.updateMyInfo(UserInfo.Field.all, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("更新资料", "gotResult: "+i);
                        dismissProgressDialog();
                        if (i == 0){
                            Log.i("生日2", "initData: "+ userInfo.getBirthday());
                            showToast(mActivity,"更新成功！！");
                            finish();
                        }
                        else {
                            showToast(mActivity,"更新失败:" + s );
                        }
                    }
                });
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case PHOTO_PZ:
                    Uri pictur;
                    //如果版本大于24的话
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        pictur=contentUri;
                    }else {
                        pictur = Uri.fromFile(
                                new File(Environment.getExternalStorageDirectory() + "/temp.jpg"));
                    }
                    startPhotoZoom(pictur);
                    break;
                case PHOTO_TK:
                    startPhotoZoom(data.getData());
                    break;
                case PHOTO_CLIP:
                    try{
                        //裁剪后的图像转化为Bitmap
                        photo1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        //创建路径
                        String path = Environment.getExternalStorageDirectory().getPath() + "/Pic";

                        //创建外部储存目录
                        file = new File(path);
                        Log.e("file", file.getPath());
                        //创建新目录
                        file.mkdirs();
                        long i = System.currentTimeMillis();
                        //生成新文件
                        file = new File(file.toString() + "/" + i + ".png");
                        Log.e("fileNew", file.getPath());
                        //创建输出流
                        OutputStream out =new FileOutputStream(file.getPath());

                        //压缩文件
                        boolean  flag = photo1.compress(Bitmap.CompressFormat.JPEG,100,out);

                        if (file.getName() != null || !file.getName().equals("")){
                            showProgressDialog("正在上传头像");
                            JMessageClient.updateUserAvatar(file, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i==0){
                                        dismissProgressDialog();
                                        showToast(mActivity,"上传成功");
                                    }else {
                                        dismissProgressDialog();
                                        showToast(mActivity,"上传失败");
                                    }
                                }
                            });
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }



    /**
     * 头像裁剪
     */
    private Uri uritempFile;

    private void startPhotoZoom(Uri uri) {
        Log.e("uri=====", "" + uri);
        Intent intent=new Intent("com.android.camera.action.CROP");
         intent.setDataAndType(uri,"image/*");
         intent.putExtra("crop","true");
         intent.putExtra("aspectX",1);
         intent.putExtra("aspectY",1);
         intent.putExtra("outputX",60);
         intent.putExtra("outputY",60);

         //uritempFile 为Uri类的变量，实例化uritempFile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //针对24 以上 操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT,uri));
            uritempFile = uri;
        }
        else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uritempFile);
        intent.putExtra("return-data",false);
        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection",true);
        startActivityForResult(intent,PHOTO_CLIP);
    }
    //头像选择  相册或者拍照
    private void showHeadDialog() {
        TextView update_dialog_TK,update_dialog_PZ,update_dialog_cencel;
        View view=getLayoutInflater().inflate(R.layout.dialog_photo_type,null);
        dialog =new Dialog(this,R.style.dialog_animal);

        Display display=getWindowManager().getDefaultDisplay();
        dialog.setContentView(view,new ViewGroup.LayoutParams(
                display.getWidth(),display.getHeight()
        ));
        //图库
        update_dialog_TK=(TextView) view.findViewById(R.id.update_dialog_TK);
        update_dialog_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,PHOTO_TK);
                dialog.dismiss();
            }
        });

        //相机拍照
        update_dialog_PZ = (TextView)view.findViewById(R.id.update_dialog_PZ);
        update_dialog_PZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动系统相机
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri mImageCaptureUri;
                //如果版本sdk大于24
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    contentUri = FileProvider.getUriForFile(mActivity,
                            "com.example.a21516.ceshi_jiguang.fileProvider",
                            new File(Environment.getExternalStorageDirectory(),"temp.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);
                }else{
                    //小于 sdk  24
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                }
                startActivityForResult(intent,PHOTO_PZ);
                dialog.dismiss();
            }
        });
        //取消
        update_dialog_cencel=(TextView) view.findViewById(R.id.update_dialog_cancel);
        update_dialog_cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //性别先择器

    private void initPickViewGender() {
        final ArrayList<String> options = new ArrayList<>();
        options.add("男");
        options.add("女");
        options.add("未知");

        mOptionsPickerView= new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2, View v) {
                mEditUserGender.setText(options.get(i));
            }
        }).build();
        mOptionsPickerView.setPicker(options);
        mOptionsPickerView.show();
    }


    //日期选择器
    private void initTimePickView() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mEditUserBirthday.setText(getTime(date));
            }

          //年月日时分秒 的显示与否，不设置则默认全部显示
        }).setType(new boolean[]{true, true, true, false, false, false})
               // .setTextColorCenter(Color.RED)//设置选中项的颜色
               // .setTextColorOut(Color.BLUE)//设置没有被选中项的颜色
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //地区选择器
    protected void initRegionPickView(){
        OptionsPickerView pvOptions =new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别选中的位置
                String tx= optionsItems.get(options1).getPickerViewText()+
                        options2Items2.get(options1).get(options2)+
                        options3Items3.get(options1).get(options2).get(options3);
                mEditUserAddress.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(optionsItems,options2Items2,options3Items3);
        pvOptions.show();
    }

    //解析数据
    private void initGson() {
        //json转化为String
        StringBuilder stringBuilder = new StringBuilder();
        try{
            // res类资源资源管理器
            AssetManager assetManager = this.getAssets();

            BufferedReader bf=new BufferedReader(new InputStreamReader(
                    assetManager.open("province.json")));
            String line;
            while ((line = bf.readLine()) != null){
                stringBuilder.append(line);
            }
            String jsonData = stringBuilder.toString();

            ArrayList<RegionBean> jsonBean = parseData(jsonData);//用Gson 转成实体

            optionsItems = jsonBean;

            for (int i = 0 ;i< jsonBean.size(); i++){ // 遍历省份
                ArrayList<String> CityList = new ArrayList<>(); //该省的城市列表（第二级）
                ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>(); //该省的所有地区列表

                for (int c =0;c<jsonBean.get(i).getCityList().size();c++){ //遍历该省份的所有城市
                    String CityName = jsonBean.get(i).getCityList().get(c).getName();
                    CityList.add(CityName);  //添加城市

                    ArrayList<String> City_AreaList = new ArrayList<>(); //该城市的所有地区列表

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项的长度不匹配造成崩溃
                    if (jsonBean.get(i).getCityList().get(c).getArea() == null
                            || jsonBean.get(i).getCityList().get(c).getArea().size() == 0){
                        City_AreaList.add("");
                    }else {
                        for (int d=0;d<jsonBean.get(i).getCityList().get(c).getArea().size();d++){
                            String AreaName= jsonBean.get(i).getCityList().get(c).getArea().get(d);
                            City_AreaList.add(AreaName);//添加该城市的所有地区数据
                        }
                    }
                    //添加该省的所有地区数据
                    Province_AreaList.add(City_AreaList);
                }
                //添加城市数据
                options2Items2.add(CityList);

                //添加读取数据
                options3Items3.add(Province_AreaList);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Gson 解析
    private ArrayList<RegionBean> parseData(String jsonData) {
        ArrayList<RegionBean> detail = new ArrayList<>();
        try{
            JSONArray data= new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RegionBean entity = gson.fromJson(data.optJSONObject(i).toString(), RegionBean.class);
                detail.add(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

}
