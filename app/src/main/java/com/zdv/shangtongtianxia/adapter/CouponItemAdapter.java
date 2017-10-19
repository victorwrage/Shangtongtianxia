package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.ConponsBean;
import com.zdv.shangtongtianxia.bean.CouponBean;
import com.zdv.shangtongtianxia.customView.QrCodeShareDialog;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class CouponItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ConponsBean.ContentBean> items;
    Utils util;
    private QrCodeShareDialog dialog;

    public CouponItemAdapter(ArrayList<ConponsBean.ContentBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.coupon_lay_item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        ConponsBean.ContentBean item = items.get(i);
        holder.coupon_left_v.setVisibility(View.GONE);
        holder.team_center1_tv.setText("有效期" + item.getStarttime() + "至" + item.getStoptime());
        holder.coupon_name_tv.setText(item.getName());
        holder.coupon_name2_tv.setText(item.getTitle());
        if (item.getStatus().equals("0")) {
            holder.coupon_receive_iv.setVisibility(View.VISIBLE);
            holder.team_center2_tv.setText("待兑换数量:x" + (Integer.valueOf(item.getNumber()) - Integer.valueOf(item.getUse_num())));
        } else if (item.getStatus().equals("1")) {
            holder.coupon_left_v.setVisibility(View.VISIBLE);
            holder.coupon_receive_iv.setVisibility(View.GONE);
            holder.team_center2_tv.setText("已兑换数量:" + item.getUse_num());
        } else if (item.getStatus().equals("2")) {
            holder.coupon_left_v.setVisibility(View.VISIBLE);
            holder.coupon_receive_iv.setVisibility(View.VISIBLE);
            holder.team_center2_tv.setText("已过期数量:x" + (Integer.valueOf(item.getNumber()) - Integer.valueOf(item.getUse_num())));
        }
       /* holder.team_center1_tv.setText(TextUtils.isEmpty(item.getName()) ? "" : item.getName());
        holder.team_center2_tv.setText(TextUtils.isEmpty(item.getType()) ? "" : item.getType());
        holder.team_center3_tv.setText(TextUtils.isEmpty(item.getPrice()) ? "" : item.getPrice());
        holder.team_item_icon.setImageResource(item.getIcon());

        holder.coupon_count_tv.setText(item.getCur_count()+"");
        holder.coupon_available_tv.setText(item.getAvailable());
        holder.coupon_select_ck.setChecked(item.getSelect());
        RxView.clicks(holder.coupon_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Add(item));
        RxView.clicks(holder.coupon_dec_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Dec(item));
        RxView.clicks(holder.coupon_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit(item));*/
       /* if (i % 2 == 0) {
            holder.team_item_lay.setCardBackgroundColor(R.color.text_grey);
        } else {
            holder.team_item_lay.setCardBackgroundColor(R.color.whitesmoke);
        }*/
        RxView.clicks(holder.coupon_item_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showQrCodeDialog(item));
    }

    private void showQrCodeDialog(ConponsBean.ContentBean item) {
        if (item.getStatus().equals("0")) {
            dialog = new QrCodeShareDialog(context, item.getSn_code());
            dialog.show();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView team_center1_tv, team_center2_tv, team_center3_tv, coupon_available_tv, coupon_name_tv, coupon_name2_tv;
        ImageView coupon_receive_iv;
        View coupon_left_v;

        CardView coupon_item_lay;

        public MyViewHolder(View view) {
            super(view);
            team_center1_tv = (TextView) view.findViewById(R.id.team_center1_tv);
            team_center2_tv = (TextView) view.findViewById(R.id.team_center2_tv);
            team_center3_tv = (TextView) view.findViewById(R.id.team_center3_tv);
            coupon_name_tv = (TextView) view.findViewById(R.id.coupon_name_tv);
            coupon_name2_tv = (TextView) view.findViewById(R.id.coupon_name2_tv);
            coupon_available_tv = (TextView) view.findViewById(R.id.coupon_available_tv);

            coupon_receive_iv = (ImageView) view.findViewById(R.id.coupon_receive_iv);
            coupon_left_v = (View) view.findViewById(R.id.coupon_left_v);


            coupon_item_lay = (CardView) view.findViewById(R.id.coupon_item_lay);

        }
    }
}
