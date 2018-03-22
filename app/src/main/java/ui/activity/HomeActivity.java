package ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zhongwang.zsmsapp.R;

import base.BaseActivity;
import base.BaseFragment;
import base.ZsmsApplication;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import model.ZsUser;
import ui.fragment.SearchFragment;
import ui.fragment.SettingFragment;
import ui.fragment.ShowFragment;
import util.CommonUtil;
import util.SharedPreferencesUtil;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private static final int MESSAGECODE = 11;
    private static final int CUSTECORDCODE = 22;
    private FrameLayout content;
    private BottomNavigationBar bottombar;
    private TextView tvTitle, tvAddRecord;
    private ZsmsApplication zsmsApplication;
    private boolean isShow = true;
    private BaseFragment showFragment, searchFragment, settingFragment;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_home;
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        zsmsApplication = (ZsmsApplication) getApplication();
        showDialog();
        setAuthority();
        initTab();
    }

    private void setAuthority() {
        String objectId = (String) SharedPreferencesUtil.getParam(this, CommonUtil.BASEOBJECTID, "");
        if (objectId.trim().equals("")) {
            showToast("登陆信息异常，请重新登陆");
            enterActivityAndKill(LoginActivity.class);
            return;
        }
        BmobQuery<ZsUser> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<ZsUser>() {
            @Override
            public void done(ZsUser zsUser, BmobException e) {
                if (sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismiss();
                if (e == null) {
                    zsmsApplication.setZsUser(zsUser);
                    if (zsUser.getState() == 3)
                        setManageAuthorty();
                }
            }
        });
    }

    /**
     * 设置管理员进入权限
     */
    private void setManageAuthorty() {
        tvAddRecord.setVisibility(View.VISIBLE);
        tvAddRecord.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        content = findViewById(R.id.content);
        bottombar = findViewById(R.id.bottombar);
        tvTitle = findViewById(R.id.tvTitle);
        tvAddRecord = findViewById(R.id.tvAddRecord);
        tvTitle.setText("首页");
        setBottomStyle();
    }

    @SuppressLint("ResourceAsColor")
    private void setBottomStyle() {
        bottombar.setActiveColor(R.color.colorTopic);
        bottombar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.mipmap.home, "主页"))
                .addItem(new BottomNavigationItem(R.mipmap.search, "搜索"))
                .addItem(new BottomNavigationItem(R.mipmap.setting, "设置"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottombar.setTabSelectedListener(this);
    }

    private void initTab() {
        showFragment = new ShowFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, showFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddRecord:
                if (isShow) {
                    enterActivity(AddMessage.class);
                } else {
                    enterActivity(AddCusRecord.class);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(zsmsApplication.isManageer())
            tvAddRecord.setVisibility(View.VISIBLE);
        switch (position) {
            case 0:
                isShow=true;
                tvTitle.setText("首页");
                if (showFragment == null)
                    showFragment = new ShowFragment();
                transaction.replace(R.id.content, showFragment);
                break;
            case 1:
                isShow=false;
                tvTitle.setText("搜索");
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                transaction.replace(R.id.content, searchFragment);
                break;
            case 2:
                if(tvAddRecord.getVisibility()==View.VISIBLE)
                    tvAddRecord.setVisibility(View.GONE);
                tvTitle.setText("设置");
                if (settingFragment == null)
                    settingFragment = new SettingFragment();
                transaction.replace(R.id.content, settingFragment);

                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}