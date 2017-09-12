package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.CouponBean;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class CouponManegerItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CouponBean> items;
    Utils util;

    public CouponManegerItemAdapter(ArrayList<CouponBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();


    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.coupon_manager__lay_item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        CouponBean item = items.get(i);
        holder.team_center1_tv.setText(TextUtils.isEmpty(item.getName()) ? "" : item.getName());
        holder.team_center2_tv.setText(TextUtils.isEmpty(item.getType()) ? "" : item.getType());
        holder.team_center3_tv.setText(TextUtils.isEmpty(item.getPrice()) ? "" : item.getPrice());
        holder.team_item_icon.setImageResource(item.getIcon());

        holder.coupon_count_tv.setText(item.getCur_count()+"");
        holder.coupon_available_tv.setText(item.getAvailable());
        holder.coupon_select_ck.setChecked(item.getSelect());
        RxView.clicks(holder.coupon_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Add(item));
        RxView.clicks(holder.coupon_dec_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Dec(item));
        RxView.clicks(holder.coupon_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit(item));
       /* if (i % 2 == 0) {
            holder.team_item_lay.setCardBackgroundColor(R.color.text_grey);
        } else {
            holder.team_item_lay.setCardBackgroundColor(R.color.whitesmoke);
        }*/
    }

    private void Submit(CouponBean item) {
        new MaterialDialog.Builder(context)
                .title("提示")
                .content("您确定发布"+item.getName()+"\n\r优惠券吗？")
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)
                .autoDismiss(true)
                .onPositive((materialDialog, dialogAction) -> {

                })
                .show();
    }

    private void Dec(CouponBean item) {
        if(item.getCur_count() ==1 ){
            return;
        }
        item.setCur_count(item.getCur_count()-1);
        notifyDataSetChanged();
    }

    private void Add(CouponBean item) {
        if(item.getCur_count() >= item.getCount()){
            VToast.toast(context,"当前优惠券最大发布数量");
            return;
        }
        item.setCur_count(item.getCur_count()+1);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView team_center1_tv, team_center2_tv, team_center3_tv,coupon_count_tv,coupon_available_tv;
        ImageView team_item_icon;
        Button coupon_submit_btn;
        LinearLayout coupon_dec_lay,coupon_add_lay;
        CheckBox coupon_select_ck;
        CardView coupon_item_lay;
        public MyViewHolder(View view) {
            super(view);
            team_center1_tv = (TextView) view.findViewById(R.id.team_center1_tv);
            team_center2_tv = (TextView) view.findViewById(R.id.team_center2_tv);
            team_center3_tv = (TextView) view.findViewById(R.id.team_center3_tv);
            coupon_count_tv = (TextView) view.findViewById(R.id.coupon_count_tv);
            coupon_available_tv = (TextView) view.findViewById(R.id.coupon_available_tv);

            team_item_icon = (ImageView) view.findViewById(R.id.team_item_icon);
            coupon_submit_btn = (Button) view.findViewById(R.id.coupon_submit_btn);
            coupon_dec_lay = (LinearLayout) view.findViewById(R.id.coupon_dec_lay);
            coupon_add_lay = (LinearLayout) view.findViewById(R.id.coupon_add_lay);
            coupon_select_ck = (CheckBox) view.findViewById(R.id.coupon_select_ck);
            coupon_item_lay = (CardView) view.findViewById(R.id.coupon_item_lay);

        }
    }


}
