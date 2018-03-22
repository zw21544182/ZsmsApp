package ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhongwang.zsmsapp.R;

import java.util.ArrayList;
import java.util.List;

import adapter.ZsMessageAdapter;
import base.BaseFragment;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import model.ZsMessage;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class ShowFragment extends BaseFragment {
    private RecyclerView rvShow;
    private List<ZsMessage> zsMessages;
    private ZsMessageAdapter zsMessageAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_show, null);
        initFindViewById(view);
        return view;
    }

    @Override
    public void initFindViewById(View view) {
        rvShow = view.findViewById(R.id.rvShow);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvShow.setLayoutManager(layoutManager);
        zsMessages = new ArrayList<>();
        zsMessageAdapter = new ZsMessageAdapter(zsMessages, getActivity(), R.layout.item_zsmessage);
        rvShow.setAdapter(zsMessageAdapter);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getMessageInfo();
    }


    @Override
    public void click(View view) {

    }

    public void getMessageInfo() {
        BmobQuery<ZsMessage> query = new BmobQuery<>();
        query.setLimit(10);
//执行查询方法
        query.findObjects(new FindListener<ZsMessage>() {
            @Override
            public void done(List<ZsMessage> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：共" + object.size() + "条数据。");
                    zsMessageAdapter.addDatas(object);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
