package ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhongwang.zsmsapp.R;

import base.BaseFragment;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class SettingFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_setting,null);
        initFindViewById(view);
        return view;
    }

    @Override
    public void initFindViewById(View view) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void click(View view) {

    }
}
