package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.BankCardBean;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;

/**
 * Info: 银行卡
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class BankItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<BankCardBean> items;
    Utils util;

    public BankItemAdapter(ArrayList<BankCardBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.bank_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        BankCardBean item = items.get(i);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bank_name_tv, bank_num_tv;

        ImageView bank_icon1;

        public MyViewHolder(View view) {
            super(view);
            bank_name_tv = (TextView) view.findViewById(R.id.bank_name_tv);
            bank_num_tv = (TextView) view.findViewById(R.id.bank_num_tv);
            bank_icon1 = (ImageView) view.findViewById(R.id.bank_icon1);
        }
    }


}
