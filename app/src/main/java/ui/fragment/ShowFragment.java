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
    private List<Integer> zsMessageLayout;
    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_show, null);
        initFindViewById(view);
        return view;
    }

    @Override
    public void initFindViewById(View view) {
        rvShow = view.findViewById(R.id.rvShow);
        rvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        zsMessageLayout = new ArrayList<>();
        zsMessages = new ArrayList<>();
        zsMessageLayout.add(R.layout.item_zsmessage);
        zsMessageAdapter = new ZsMessageAdapter(zsMessages,getActivity(),zsMessageLayout);
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
                if(e==null){
                    Log.i("bmob","查询成功：共"+object.size()+"条数据。");
                    zsMessageAdapter.addDatas(object);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });   }
}
