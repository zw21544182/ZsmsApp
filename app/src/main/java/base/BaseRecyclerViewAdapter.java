package base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 创建时间: 2017/11/29
 * 创建人: Administrator
 * 功能描述:RecyclerView基类(需要注意类型转换问题)
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected ArrayList<T> data;
    protected Context context;
    private int layoutId;

    /**
     * 适配器构造方法 #注意List
     *
     * @param data      数据集合
     * @param context   上下文对象
     * @param layoutIds 布局集合
     */
    public BaseRecyclerViewAdapter(List<T> data, Context context, int layoutIds) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.layoutId = layoutIds;
        this.context = context;
    }

    public void setData(List data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAll() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public Object getDataByPos(int index) {
        if (data.size() <= index) {
            return null;
        }
        return data.get(index);
    }

    public void addData(T t) {
        data.add(t);
        notifyDataSetChanged();
    }

    public void addDatas(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        Log.d("zww", "onCreateViewHolder " + pos);
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        BaseViewHolder baseViewHolder = new BaseViewHolder(view);
        onCreate(baseViewHolder, data.get(pos), pos);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        onBind((BaseViewHolder) viewHolder, data.get(position), position);//设置显示数据
    }


    /**
     * 在oncreateViewHolder方法中设置点击事件
     * 避免重复调用
     *
     * @param baseViewHolder itme控件
     * @param o              itme    实体类
     * @param pos            位置
     */
    protected abstract void onCreate(BaseViewHolder baseViewHolder, T o, int pos);

    protected abstract void onBind(BaseViewHolder baseViewHolder, T itmeModule, int position);

    protected void setNodataInfo(ImageView nodataImage, TextView nodataText) {
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    public class BaseViewHolder extends RecyclerView.ViewHolder {
        //该类下部分方法可以自行添加
        View rootView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        public void setText(int viewId, int resourceId) {
            ((TextView) getViewById(viewId)).setText(resourceId);
        }

        public void setClickListent(int viewId, View.OnClickListener onClickListener) {
            getViewById(viewId).setOnClickListener(onClickListener);
        }

        public void setText(int viewId, String content) {
            ((TextView) getViewById(viewId)).setText(content);
        }

        public void setCheckChangeListen(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            ((CheckBox) getViewById(viewId)).setOnCheckedChangeListener(onCheckedChangeListener);
        }

        public void setImageSource(int imageViewId, int sourceId) {
            ImageView imageView = (ImageView) getViewById(imageViewId);
            imageView.setImageResource(sourceId);
        }

        public View getViewById(int viewId) {
            return rootView.findViewById(viewId);
        }
    }
}

