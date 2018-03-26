package ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import util.CommonUtil;

import static ui.activity.HomeActivity.MESSAGECODE;

/**
 * Created by zhongwang on 2018/3/17.
 */

public class ShowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvShow;
    private SwipeRefreshLayout slLoad;
    private ZsMessageAdapter zsMessageAdapter;
    private List<ZsMessage> zsMessages;
    private int page = 0;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_show, null);
        initFindViewById(view);
        return view;
    }

    @Override
    public void initFindViewById(View view) {
        rvShow = view.findViewById(R.id.rvShow);
        slLoad = view.findViewById(R.id.slLoad);
        setRecyclerviewStyle();
        setLoadviewStyle();
    }

    private void setLoadviewStyle() {
        slLoad.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        slLoad.setOnRefreshListener(this);
    }


    private void setRecyclerviewStyle() {
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
        query.setLimit(5);
        query.order("-createdAt");
//执行查询方法
        query.findObjects(new FindListener<ZsMessage>() {
            @Override
            public void done(List<ZsMessage> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：共" + object.size() + "条数据。");
                    zsMessageAdapter.setDatas(object);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ZSAPP","onActivityResult requestCode " +requestCode
        + " resultCode" +  resultCode+  " extra data "+(boolean) data.getExtras().get(CommonUtil.ISSAVEMESSAGE));
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MESSAGECODE&&resultCode== CommonUtil.MESSAGECALLBACK){
          boolean isSaved= (boolean) data.getExtras().get(CommonUtil.ISSAVEMESSAGE);
           operateRefresh(isSaved);
        }
    }

    private void operateRefresh(boolean isSaved) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slLoad.setRefreshing(true);
                ShowFragment.this.onRefresh();
            }
        }, 100);
    }

    @Override
    public void onRefresh() {
        Log.i("ZSAPP","onRefresh");
        initData(null);
        //刷新完成
        slLoad.setRefreshing(false);
        showToast("加载完成");

    }
}
