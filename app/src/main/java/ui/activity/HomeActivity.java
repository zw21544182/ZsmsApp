package ui.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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

import static util.CommonUtil.CUSTECORDCODE;
import static util.CommonUtil.HEADCODE;
import static util.CommonUtil.MESSAGECODE;


/**
 * Created by zhongwang on 2018/3/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private FrameLayout content;
    private BottomNavigationBar bottombar;
    private TextView tvTitle, tvAddRecord;
    private ZsmsApplication zsmsApplication;
    private boolean isShow = true;
    private BaseFragment showFragment, searchFragment, settingFragment;
    private BaseFragment mCurrentFrgment;
    private android.app.FragmentTransaction mfragmentTransaction;
    private boolean isExit;
    private Handler handler;

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
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isExit=false;
            }
        };
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
        if (showFragment == null)
            showFragment = new ShowFragment();
        switchFragment(showFragment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddRecord:
                if (isShow) {
                    enterActivityForResult(AddMessageActivity.class, MESSAGECODE);
                } else {
                    enterActivityForResult(AddCusRecordActivity.class, CUSTECORDCODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MESSAGECODE)
            showFragment.onActivityResult(requestCode, resultCode, data);
        else if (requestCode == HEADCODE)
            settingFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTabSelected(int position) {
        if (zsmsApplication.isManageer())
            tvAddRecord.setVisibility(View.VISIBLE);
        switch (position) {
            case 0:
                isShow = true;
                tvTitle.setText("首页");
                if (showFragment == null)
                    showFragment = new ShowFragment();
                switchFragment(showFragment);
                break;
            case 1:
                isShow = false;
                tvTitle.setText("搜索");
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                switchFragment(searchFragment);
                break;
            case 2:
                if (tvAddRecord.getVisibility() == View.VISIBLE)
                    tvAddRecord.setVisibility(View.GONE);
                tvTitle.setText("设置");
                if (settingFragment == null)
                    settingFragment = new SettingFragment();
                switchFragment(settingFragment);
                break;
        }
    }

    private void switchFragment(Fragment fragment) {
        mfragmentTransaction = getFragmentManager().beginTransaction();
        if (null != mCurrentFrgment) {
            mfragmentTransaction.hide(mCurrentFrgment);
        }
        if (!fragment.isAdded()) {
            mfragmentTransaction.add(R.id.content, fragment, fragment.getClass().getName());
        } else {
            mfragmentTransaction.show(fragment);
        }
        mfragmentTransaction.commit();
        mCurrentFrgment = (BaseFragment) fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            showToast("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {

            finish();

        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}