package ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.zhongwang.zsmsapp.R;

import java.util.List;

import base.BaseActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import model.ZsUser;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class RegisterActivity extends BaseActivity {
    private EditText edNickName;
    private EditText edUserName;
    private EditText edPassWord;
    private EditText edSurePassWord;
    private EditText edIdNum;
    private RelativeLayout btRegister;
    private String nickName;
    private String userName;
    private String passWord;
    private String surePassWords;
    private String idNum;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_register;
    }

    @Override
    protected void initEvent() {
        btRegister.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        edNickName = findViewById(R.id.edNickName);
        edUserName = findViewById(R.id.edUserName);
        edPassWord = findViewById(R.id.edPassWord);
        edSurePassWord = findViewById(R.id.edSurePassWord);
        edIdNum = findViewById(R.id.edIdNum);
        btRegister = findViewById(R.id.btRegister);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btRegister:
                nickName = edNickName.getEditableText().toString().trim();
                userName = edUserName.getEditableText().toString().trim();
                passWord = edPassWord.getEditableText().toString().trim();
                surePassWords = edSurePassWord.getEditableText().toString().trim();
                idNum = edIdNum.getEditableText().toString().trim();
                boolean result = isRegister();
                if (result) {
                    // TODO: 2018/3/16 ZsmsUser添加一条数据，state默认位1
                    showDialog();
                    checkUserName();
                }
                break;
        }
    }

    private void checkUserName() {
        BmobQuery<ZsUser> query = new BmobQuery<>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("userName", userName);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<ZsUser>() {
            @Override
            public void done(List<ZsUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        addUserInfo();
                    } else {
                        sweetAlertDialog.dismiss();
                        new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("注册失败")
                                .setContentText("用户名已存在，请重新填写")
                                .show();
                        edUserName.setText("");
                    }

                } else {
                    sweetAlertDialog.dismiss();
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("注册失败")
                            .setContentText("网络原因，请尝试重新注册")
                            .show();
                }
            }
        });
    }

    private void addUserInfo() {
        ZsUser zsUser = new ZsUser();
        zsUser.setNickName(nickName);
        zsUser.setUserName(userName);
        zsUser.setPassWord(passWord);
        zsUser.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismiss();
                if (e == null) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("注册成功")
                            .setContentText("请等待管理员审核")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent data = new Intent();
                                    data.putExtra("username", userName);
                                    data.putExtra("password", passWord);
                                    setResult(88, data);
                                    finish();
                                }
                            })
                            .show();
                } else {
                    showToast("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void showDialog() {
        if (sweetAlertDialog == null)
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("加载中");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }

    private boolean isRegister() {
        boolean result = true;
//        if (nickName.trim().equals("") || nickName.length() > 15 || nickName.length() < 3) {
//            result = false;
//            showToast("昵称输入不规范");
//            return result;
//        }
        if (userName.trim().equals("") || userName.length() < 8) {
            result = false;
            showToast("账号输入不规范");
            return result;
        }
//        if (passWord.trim().equals("") || passWord.length() < 8) {
//            result = false;
//            showToast("密码输入不规范");
//            return result;
//        }
//        if (!surePassWords.trim().equals(passWord.trim())) {
//            result = false;
//            showToast("两次密码输入不一致");
//            return result;
//        }
//        if (!(idNum.length() == 18)) {
//            result = false;
//            showToast("身份证号码输入不规范");
//            return result;
//        }
        return result;
    }
}
