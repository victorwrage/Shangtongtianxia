package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.HistoryBean;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class CoinHistoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<HistoryBean> items;
    Utils util;

    public CoinHistoryItemAdapter(ArrayList<HistoryBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_coin_history_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        HistoryBean item = items.get(i);
        String cost_type = "";
        switch (Integer.parseInt(item.getCost_type())) {
            case 1:
                cost_type = "充值";
                break;
            case 2:
                cost_type = "消费";
                break;
            case 3:
                cost_type = "转出";
                break;
            case 4:
                cost_type = "转入";
                break;
            case 5:
                cost_type = "申请";
                break;
        }
        String opacount_type = "";
        if (item.getOpacount_type() == null) {
            item.setOpacount_type("2");
        }
        switch (Integer.parseInt(item.getOpacount_type())) {
            case 1:
                opacount_type = "商家";
                break;
            case 2:
                opacount_type = "会员";
                break;
            default:
                opacount_type = "会员";
                break;

        }

        String action = "";
        switch (Integer.parseInt(item.getAction())) {
            case 1:
                holder.coin_history_pay_tv.setTextColor(context.getResources().getColor(R.color.red));
                action = "收入";
                break;
            case 2:
                holder.coin_history_pay_tv.setTextColor(context.getResources().getColor(R.color.green));
                action = "支出";
                break;
        }

        String opacount_state = "";
        switch (Integer.parseInt(item.getOpacount_type())) {
            case 0:
                opacount_state = "现金";
                break;
            case 1:
                opacount_state = "微信";
                break;
            case 2:
                opacount_state = "支付宝";
                break;
            case 3:
                opacount_state = "银行卡";
                break;
            case 4:
                opacount_state = "余额";
                break;
            case 5:
                opacount_state = "商通币";
                break;

        }
        String cost_state = "";
        switch (Integer.parseInt(item.getCost_state())) {
            case 0:
                cost_state = "现金";
                break;
            case 1:
                cost_state = "微信";
                break;
            case 2:
                cost_state = "支付宝";
                break;
            case 3:
                cost_state = "银行卡";
                break;
            case 4:
                cost_state = "余额";
                break;
            case 5:
                cost_state = "商通币";
                break;

        }
        holder.coin_history_date_tv.setText("交易时间:" + item.getCreatetime());
        holder.coin_history_state_tv.setText("交易类型:" + cost_state);
        holder.coin_history_type_tv.setText(" 交易账户:" + item.getOpacount_name());
        holder.coin_history_pay_tv.setText(action + ":" + item.getMoney());
        holder.coin_history_opacount_name_tv.setText("交易账户类型:" + opacount_type + " " + opacount_state);
        // holder.coin_history_opacount_name_tv.setText(item.getOpacount_name()+"("+opacount_type+":"+opacount_state+")");
        if (i % 2 == 0) {
            holder.coin_history_item_lay.setCardBackgroundColor(context.getResources().getColor(R.color.wheat));
        } else {
            holder.coin_history_item_lay.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coin_history_date_tv, coin_history_pay_tv, coin_history_type_tv, coin_history_state_tv, coin_history_opacount_name_tv;
        RelativeLayout coin_history_lay;
        CardView coin_history_item_lay;

        public MyViewHolder(View view) {
            super(view);
            coin_history_date_tv = (TextView) view.findViewById(R.id.coin_history_date_tv);
            coin_history_pay_tv = (TextView) view.findViewById(R.id.coin_history_pay_tv);
            coin_history_type_tv = (TextView) view.findViewById(R.id.coin_history_type_tv);
            coin_history_state_tv = (TextView) view.findViewById(R.id.coin_history_state_tv);
            coin_history_opacount_name_tv = (TextView) view.findViewById(R.id.coin_history_opacount_name_tv);
            coin_history_item_lay = (CardView) view.findViewById(R.id.coin_history_item_lay);

            coin_history_lay = (RelativeLayout) view.findViewById(R.id.coin_history_lay);

        }
    }

    private void setRead(MyViewHolder holder, int position) {
        HistoryBean item = items.get(position);
        // item.setIs_read(true);
    }

}
