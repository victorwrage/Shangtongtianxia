package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.OrderBean;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Info: 订单
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class OrderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<OrderBean> items;
    Utils util;

    public OrderItemAdapter(ArrayList<OrderBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.order_lay_item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        OrderBean item = items.get(i);
        holder.order_item_bg.setSelected(false);
        RxView.clicks(holder.order_item_lay_).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Select(holder.order_item_bg));
    }

    private void Select(ImageView order_item_bg) {
        order_item_bg.setSelected(true);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order_express_name, order_express_state, order_item_name,order_item_add,order_item_date,order_item_count;
        LinearLayout order_item_lay_;
        ImageView order_item_icon,order_item_bg;

        public MyViewHolder(View view) {
            super(view);
            order_express_name = (TextView) view.findViewById(R.id.order_express_name);
            order_express_state = (TextView) view.findViewById(R.id.order_express_state);
            order_item_name = (TextView) view.findViewById(R.id.order_item_name);
            order_item_add = (TextView) view.findViewById(R.id.order_item_add);
            order_item_date = (TextView) view.findViewById(R.id.order_item_date);
            order_item_count = (TextView) view.findViewById(R.id.order_item_count);

            order_item_lay_ = (LinearLayout) view.findViewById(R.id.order_item_lay_);
            order_item_icon = (ImageView) view.findViewById(R.id.order_item_icon);
            order_item_bg = (ImageView) view.findViewById(R.id.order_item_bg);
        }
    }

    private void setRead(MyViewHolder holder, int position) {
        OrderBean item =  items.get(position);
       // item.setIs_read(true);
    }

}
