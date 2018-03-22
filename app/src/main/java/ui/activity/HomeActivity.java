package ui.activity;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zhongwang.zsmsapp.R;

import base.BaseActivity;
import base.BaseFragment;
import ui.fragment.SearchFragment;
import ui.fragment.SettingFragment;
import ui.fragment.ShowFragment;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private FrameLayout content;
    private BottomNavigationBar bottombar;
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
        initTab();
    }

    @Override
    protected void initView() {
        content = findViewById(R.id.content);
        bottombar = findViewById(R.id.bottombar);
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

    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (showFragment == null)
                    showFragment = new ShowFragment();
                transaction.replace(R.id.content, showFragment);
                break;
            case 1:
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                transaction.replace(R.id.content, searchFragment);
                break;
            case 2:
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