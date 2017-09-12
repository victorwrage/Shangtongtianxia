package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.MemberBean;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class TeamItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<MemberBean> items;
    Utils util;

    public TeamItemAdapter(ArrayList<MemberBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();


    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.team_lay_item, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        MemberBean item = items.get(i);
        holder.team_tel_tv.setText(TextUtils.isEmpty(item.getTel()) ? "" : util.getPhoneEncrypt(item.getTel()));
        holder.team_name_tv.setText(TextUtils.isEmpty(item.getName()) ? "" : item.getName());
        holder.team_level_tv.setText(TextUtils.isEmpty(item.getLid()) ? "" : item.getLid());
        if (item.getStatus().equals("0")) {
            holder.team_level_lay.setVisibility(View.INVISIBLE);
            holder.team_item_icon.setImageResource(R.drawable.unavailable);
        } else {
            holder.team_level_lay.setVisibility(View.VISIBLE);
            holder.team_item_icon.setImageResource(R.drawable.team_member);
        }

       /* if (i % 2 == 0) {
            holder.team_item_lay.setCardBackgroundColor(R.color.text_grey);
        } else {
            holder.team_item_lay.setCardBackgroundColor(R.color.whitesmoke);
        }*/
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView team_tel_tv, team_name_tv, team_level_tv;
        ImageView team_item_icon;
        LinearLayout team_level_lay;
        CardView team_item_lay;
        public MyViewHolder(View view) {
            super(view);
            team_tel_tv = (TextView) view.findViewById(R.id.team_tel_tv);
            team_name_tv = (TextView) view.findViewById(R.id.team_name_tv);
            team_level_tv = (TextView) view.findViewById(R.id.team_level_tv);
            team_item_icon = (ImageView) view.findViewById(R.id.team_item_icon);
            team_level_lay = (LinearLayout) view.findViewById(R.id.team_level_lay);
            team_item_lay = (CardView) view.findViewById(R.id.team_item_lay);

        }
    }


}
