package base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import util.ToastUtil;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public View view;
    private ToastUtil toastUtil;

    public void showToast(String content) {
        toastUtil.showToast(content);
    }

    public void showToast(int resourecId) {
        toastUtil.showToast(getString(resourecId));
    }

    @Override
    public void onClick(View view) {
        click(view);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        return view;
    }


    public View getRootView() {
        return view;
    }

    //子类复写此方法初始化事件
    protected void initEvent() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toastUtil = ToastUtil.getInstance(getActivity());
        initData(savedInstanceState);
        initEvent();

    }




    /*
        子类实现此方法返回View展示
         */
    public abstract View initView(LayoutInflater inflater);

    //初始化控件
    public abstract void initFindViewById(View view);

    //子类在此方法中实现数据的初始化
    public abstract void initData(@Nullable Bundle savedInstanceState);

    public abstract void click(View view);






}

