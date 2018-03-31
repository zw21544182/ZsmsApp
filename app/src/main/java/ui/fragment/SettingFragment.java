package ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.zhongwang.zsmsapp.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import base.BaseFragment;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import model.ZsUser;
import ui.activity.LoginActivity;
import util.CommonUtil;
import util.SharedPreferencesUtil;

import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getFilesDir;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class SettingFragment extends BaseFragment {
    private static final int IMAGE_REQUEST_CODE = 33;
    private static final int RESIZE_REQUEST_CODE = 44;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 32;
    private CircleImageView ivHead;
    private String baseObjectId;
    private LinearLayout about;
    private LinearLayout loginout;
    private Bitmap photo;
    private String cachehead;
    private BmobFile headFile;
    private ZsUser zsUser;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initFindViewById(view);
        return view;
    }

    @Override
    public void initFindViewById(View view) {
        ivHead = view.findViewById(R.id.imageView);
        about = view.findViewById(R.id.about);
        loginout = view.findViewById(R.id.loginout);
        about.setOnClickListener(this);
        loginout.setOnClickListener(this);
        ivHead.setOnClickListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        baseObjectId = (String) SharedPreferencesUtil.getParam(baseActivity, CommonUtil.BASEOBJECTID, "");
        if(baseObjectId.trim().equals("")){
            showDialog("登陆状态异常，请重新登陆");
            enterActivityAndKill(LoginActivity.class);
            return;
        }
        getInfoByObjectId(baseObjectId);
    }

    private void getInfoByObjectId(final String baseObjectId) {
        BmobQuery<ZsUser> query = new BmobQuery<>();
        query.getObject(baseObjectId, new QueryListener<ZsUser>() {

            @Override
            public void done(ZsUser object, BmobException e) {
                if(e==null){
                 //成功时调用
                    zsUser=object;
                    String imageUrl = zsUser.getImageHead().getFileUrl();
                    Log.i("ZSAPP","IMAGEURL: "+imageUrl);
                    Glide.with(baseActivity).load(imageUrl).error(R.mipmap.head_man).into(ivHead);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    @Override
    public void click(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.loginout:
                SharedPreferencesUtil.removeParam(baseActivity, CommonUtil.BASEOBJECTID);
                enterActivityAndKill(LoginActivity.class);
                break;
            case R.id.about:
                showToast("关于");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri originalUri = data.getData();
                    resizeImage(originalUri);
                    break;
                case RESIZE_REQUEST_CODE:
                    showResizeImageAndUpload(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showResizeImageAndUpload(Intent data) {
        showDialog("上传头像中");
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            final Drawable drawable = new BitmapDrawable(photo);
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(baseActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(baseActivity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    return;
                }
            }
            cachehead = getFilesDir().getAbsolutePath() + "cachehead.jpg";
            headFile = new BmobFile(saveBitmapFile(photo, cachehead));
            headFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){

                        zsUser.setImageHead(headFile);
                        zsUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                           dismissDialog();
                                if(e==null){
                               ivHead.setImageDrawable(drawable);
                           }
                           else {
                                    showToast("头像更换失败");
                                }
                            }
                        });
                    }
                    else
                        showToast("头像更换失败");
                }
            });
        }
    }

    public File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void resizeImage(Uri uri) {//重塑图片大小
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//能够裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
}
