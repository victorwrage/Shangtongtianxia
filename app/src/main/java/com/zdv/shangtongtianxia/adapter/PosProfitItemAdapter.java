package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.PosProfitBean;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class PosProfitItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PosProfitBean> items;
    Utils util;

    public PosProfitItemAdapter(ArrayList<PosProfitBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();


    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.pos_lay_item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        PosProfitBean item = items.get(i);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pos_item_content, pos_item_type, pos_item_count;

        ImageView pos_item_icon;

        public MyViewHolder(View view) {
            super(view);
            pos_item_content = (TextView) view.findViewById(R.id.pos_item_content);
            pos_item_type = (TextView) view.findViewById(R.id.pos_item_type);
            pos_item_count = (TextView) view.findViewById(R.id.pos_item_count);

            pos_item_icon = (ImageView) view.findViewById(R.id.pos_item_icon);
        }
    }

    private void setRead(MyViewHolder holder, int position) {
        PosProfitBean item =  items.get(position);
       // item.setIs_read(true);
    }

}
