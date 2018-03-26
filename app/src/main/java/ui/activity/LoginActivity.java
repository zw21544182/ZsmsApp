package ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhongwang.zsmsapp.R;

import java.util.List;

import base.BaseActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.ZsUser;
import util.CommonUtil;
import util.SharedPreferencesUtil;

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
        if (isLogined())
            enterActivityAndKill(HomeActivity.class);
        return R.layout.activity_login;
    }

    private boolean isLogined() {
        return !SharedPreferencesUtil.getParam(this, CommonUtil.BASEOBJECTID, "").toString().trim().equals("");
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
                enterActivityForResult(RegisterActivity.class, requestCode);
                break;
            case R.id.btLogin:
                showDialog();
                login();
                break;
        }
    }

    private void login() {
        String userName = edUserName.getEditableText().toString();
        String passWord = edPassWord.getEditableText().toString();
        BmobQuery<ZsUser> query = new BmobQuery<>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("userName", userName);
        query.addWhereEqualTo("passWord", passWord);
//执行查询方法
        query.findObjects(new FindListener<ZsUser>() {
            @Override
            public void done(List<ZsUser> list, BmobException e) {
                String errorMessage = "";
                sweetAlertDialog.dismiss();
                if (e == null && list.size() == 1) {
                    int userState = list.get(0).getState();
                    if (userState == 1) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("登陆失败")
                                .setContentText("管理员暂未审核通过")
                                .show();
                        return;
                    }
                    if (e == null && list == null) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("登陆失败")
                                .setContentText("您输入的账号或密码不符合规范,请注册")
                                .show();
                        return;
                    }
                    SharedPreferencesUtil.setParam(LoginActivity.this, CommonUtil.BASEOBJECTID, list.get(0).getObjectId());
                    enterActivityAndKill(HomeActivity.class);
                    return;
                }
                if (e == null && list.size() == 0) {
                    errorMessage = "账号密码不匹配";
                } else {
                    errorMessage = e.getMessage();
                }
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("登陆失败")
                        .setContentText("失败原因: " + errorMessage)
                        .show();
            }
        });
    }
}
