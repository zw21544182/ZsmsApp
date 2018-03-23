package ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * Created by zhongwang on 2018/3/22.
 */

public class AddMessage extends BaseActivity {
    private TextView tvGoBack;
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
                    if (pattern.matcher(messageUrl).matches()) {
                        Intent intent = new Intent(AddMessage.this, DetailActivity.class);
                        intent.putExtra("messageUrl", messageUrl);
                        AddMessage.this.startActivity(intent);
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
    private String cacheImagePath = getFilesDir().getAbsolutePath() + "cacheimage.jpg";
    private BmobFile imageFile;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAddImage:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case R.id.btSave:
                if (edTile.getEditableText().toString().trim().equals("")) {
                    showToast("标题输入不符合规范");
                    return;
                }
                if (edMessageUrl.getEditableText().toString().trim().equals("") || (!pattern.matcher(messageUrl).matches())) {
                    showToast("网页链接输入不符合规范");
                    return;
                }

                break;
        }
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
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            ivAddImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAddImage.setImageDrawable(drawable);
            ivMessage.setImageDrawable(drawable);
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
