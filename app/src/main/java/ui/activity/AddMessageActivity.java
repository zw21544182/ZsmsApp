package ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongwang.zsmsapp.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import base.BaseActivity;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import model.ZsMessage;
import util.CommonUtil;
import util.SharedPreferencesUtil;

/**
 * Created by zhongwang on 2018/3/22.
 */

public class AddMessageActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12;
    private TextView tvGoBack;
    private EditText edIntruction;
    private EditText edTile;
    private EditText edMessageUrl;
    private ImageView ivAddImage;
    private LinearLayout rootLayout;
    private ImageView ivMessage;
    private TextView tvMessageTitle;
    private RelativeLayout btSave;
    private String messageUrl;
    private static final int RESIZE_REQUEST_CODE = 168;
    private static final int IMAGE_REQUEST_CODE = 188;
    private Pattern pattern = Pattern
            .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
    private TextWatcher titleWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            log("beforeTextChanged " + charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tvMessageTitle.setText(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            log("afterTextChanged " + editable.toString());

        }
    };
    private TextWatcher messageUrlWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            messageUrl = charSequence.toString();
            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pattern.matcher(messageUrl).matches() || messageUrl.trim().equals("")) {
                        Intent intent = new Intent(AddMessageActivity.this, DetailActivity.class);
                        intent.putExtra("messageUrl", messageUrl);
                        AddMessageActivity.this.startActivity(intent);
                    } else {
                        showToast("消息链接输入不符合规范");
                    }
                }
            });
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private String cacheImagePath = "";
    private BmobFile imageFile;
    private Bitmap photo;
    private boolean isSave = false;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_addmessage;
    }

    @Override
    protected void initEvent() {
        edTile.addTextChangedListener(titleWatcher);
        edMessageUrl.addTextChangedListener(messageUrlWatcher);
        ivAddImage.setOnClickListener(this);
        btSave.setOnClickListener(this);
        tvGoBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvGoBack = findViewById(R.id.tvGoBack);
        edTile = findViewById(R.id.edTile);
        edMessageUrl = findViewById(R.id.edMessageUrl);
        ivAddImage = findViewById(R.id.ivAddImage);
        rootLayout = findViewById(R.id.rootLayout);
        ivMessage = findViewById(R.id.ivMessage);
        tvMessageTitle = findViewById(R.id.tvMessageTitle);
        btSave = findViewById(R.id.btSave);
        tvGoBack = findViewById(R.id.tvGoBack);
        edIntruction = findViewById(R.id.edIntruction);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvGoBack:
                Intent data = new Intent();
                data.putExtra(CommonUtil.ISSAVEMESSAGE, isSave);
                setResult(CommonUtil.MESSAGECALLBACK, data);
                finish();
                break;
            case R.id.ivAddImage:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.btSave:
                showDialog("数据上传中");
                saveInfo();
                break;
        }
    }

    /**
     * 保存信息
     */
    private void saveInfo() {
        String title = edTile.getEditableText().toString().trim();
        String instruction = edIntruction.getEditableText().toString().trim();
        String userObjectId = (String) SharedPreferencesUtil.getParam(this, CommonUtil.BASEOBJECTID, "");
        if (title.equals("") || title.length() < 6) {
            showToast("标题输入不符合规范");
            edTile.setText("");
            dismissDialog();
            return;
        }
        if (instruction.equals("") || instruction.length() < 10) {
            showToast("简介输入不符合规范");
            edIntruction.setText("");
            dismissDialog();
            return;
        }
        if (messageUrl.equals("") || (!pattern.matcher(messageUrl).matches())) {
            showToast("网页链接输入不符合规范");
            edMessageUrl.setText("");
            dismissDialog();
            return;
        }
        if (userObjectId.trim().equals("")) {
            showToast("登陆状态异常");
            enterActivityAndKill(LoginActivity.class);
            return;
        }
        if(imageFile==null){
            showToast("请先选择图片");
            return;
        }
        final ZsMessage zsMessage = new ZsMessage();
        zsMessage.setMessageUrl(messageUrl);
        zsMessage.setTitle(title);
        zsMessage.setInstruction(instruction);
        zsMessage.setUserObjectId(userObjectId);
        imageFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    new File(cacheImagePath).delete();
                    zsMessage.setImageFile(imageFile);
                    zsMessage.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            dismissDialog();
                            if (e == null) {
                                showToast("保存成功");
                                isSave = true;
                                return;
                            }
                            showToast("保存失败，原因: " + e.getMessage());

                        }
                    });
                } else {
                    dismissDialog();
                    showToast("预览图上传失败");
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            Intent data = new Intent();
            data.putExtra(CommonUtil.ISSAVEMESSAGE, isSave);
            setResult(CommonUtil.MESSAGECALLBACK, data);
            finish();
            return true;

    }

    public void resizeImage(Uri uri) {//重塑图片大小
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//能够裁剪
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) {//显示图片
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            ivAddImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAddImage.setImageDrawable(drawable);
            ivMessage.setImageDrawable(drawable);
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    return;
                }
            }
            cacheImagePath = getFilesDir().getAbsolutePath() + "cacheimage.jpg";
            imageFile = new BmobFile(saveBitmapFile(photo, cacheImagePath));
        }
    }

    public File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cacheImagePath = getFilesDir().getAbsolutePath() + "cacheimage.jpg";
                    imageFile = new BmobFile(saveBitmapFile(photo, cacheImagePath));
                } else {
                    imageFile = null;
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri originalUri = data.getData();//获取图片uri
                    resizeImage(originalUri);
                    break;
                case RESIZE_REQUEST_CODE:
                    showResizeImage(data);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
