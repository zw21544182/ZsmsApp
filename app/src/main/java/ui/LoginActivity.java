package ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongwang.zsmsapp.R;

import base.BaseActivity;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class LoginActivity extends BaseActivity {
    private TextView tvTitle;
    private EditText edUserName;
    private EditText edPassWord;
    private RelativeLayout btLogin;
    private TextView tvRegister;
    private int requestCode = 168;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_login;
    }

    @Override
    protected void initEvent() {
        btLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        edUserName = findViewById(R.id.edUserName);
        edPassWord = findViewById(R.id.edPassWord);
        btLogin = findViewById(R.id.btLogin);
        tvRegister = findViewById(R.id.tvRegister);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == 88) {
            String userName = data.getExtras().getString("username");
            String passWord = data.getExtras().getString("password");
           edUserName.setText(userName.trim());
           edPassWord.setText(passWord.trim());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                // TODO: 2018/3/16 进入注册界面
                enterActivityForResult(RegisterActivity.class, requestCode);
                break;
            case R.id.btLogin:
                // TODO: 2018/3/16 连接bmob服务器进行登陆操作
                break;
        }
    }
}
