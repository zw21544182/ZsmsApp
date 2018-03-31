package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhongwang.zsmsapp.R;

import java.util.List;

import model.ZsMessage;
import ui.activity.DetailActivity;

/**
 * Created by zhongwang on 2018/3/22.
 */

public class ZsMessageAdapter extends RecyclerView.Adapter<ZsMessageAdapter.BaseViewHolder> {
    protected List<ZsMessage> data;
    protected Context context;
    private int layoutId;
    private ImageView ivMessage;
    private TextView tvMessageTitle;
    private LinearLayout rootLayout;

    public ZsMessageAdapter(List<ZsMessage> data, Context context, int layoutId) {
        this.data = data;
        this.context = context;
        this.layoutId = layoutId;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ivMessage = view.findViewById(R.id.ivMessage);
        tvMessageTitle = view.findViewById(R.id.tvMessageTitle);
        rootLayout = view.findViewById(R.id.rootLayout);
        return new BaseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        Glide.with(context).load(data.get(position).getImageFile().getUrl()).into(ivMessage);
        tvMessageTitle.setText(data.get(position).getTitle());
        tvMessageTitle .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZsMessage zsMessage = data.get(position);
                String messageUrl = zsMessage.getMessageUrl();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("messageUrl", messageUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addDatas(List<ZsMessage> object) {

        data.addAll(object);
        notifyDataSetChanged();
    }

    public void setDatas(List<ZsMessage> object) {
        data.clear();
        data.addAll(object);
        notifyDataSetChanged();
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
