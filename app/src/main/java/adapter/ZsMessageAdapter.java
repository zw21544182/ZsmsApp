package adapter;

import android.content.Context;

import java.util.List;

import base.BaseRecyclerViewAdapter;
import model.ZsMessage;

/**
 * Created by zhongwang on 2018/3/22.
 */

public class ZsMessageAdapter extends BaseRecyclerViewAdapter<ZsMessage> {


    /**
     * 适配器构造方法 #注意List
     *
     * @param data      数据集合
     * @param context   上下文对象
     * @param layoutIds 布局集合
     */
    public ZsMessageAdapter(List<ZsMessage> data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
    }

    @Override
    protected void onCreate(BaseViewHolder baseViewHolder, ZsMessage o, int pos) {

    }

    @Override
    protected void onBind(BaseViewHolder baseViewHolder, ZsMessage itmeModule, int position) {

    }
}
