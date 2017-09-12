package com.zdv.shangtongtianxia.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.AddressBean;
import com.zdv.shangtongtianxia.customView.slideItemView.SlideBaseAdapter;
import com.zdv.shangtongtianxia.customView.slideItemView.SlideListView;
import com.zdv.shangtongtianxia.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Info: 地址Adapter
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class AddressItemAdapter extends SlideBaseAdapter {
    Context context;
    ArrayList<AddressBean> items;
    Utils util;
    IAddressAdapter listener;

    public AddressItemAdapter(Context context, ArrayList<AddressBean> items) {
        super(context);
        this.items = items;
        this.context = context;
        util = Utils.getInstance();

    }

    @Override
    public SlideListView.SlideMode getSlideModeInPosition(int position) {
        return SlideListView.SlideMode.RIGHT;
    }

    @Override
    public int getFrontViewId(int position) {
        return R.layout.address_lay_item;
    }

    @Override
    public int getLeftBackViewId(int position) {
        return R.layout.row_left_back_view;
    }

    @Override
    public int getRightBackViewId(int position) {
        return R.layout.row_right_back_view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public AddressBean getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = createConvertView(i);
            holder = new ViewHolder();
            holder.address_name_tv = (TextView) view.findViewById(R.id.address_name_tv);
            holder.address_phone_tv = (TextView) view.findViewById(R.id.address_phone_tv);
            holder.address_add_tv = (TextView) view.findViewById(R.id.address_add_tv);
            holder.address_list_item_lay = (CardView) view.findViewById(R.id.address_list_item_lay);

            holder.address_select_ck = (CheckBox) view.findViewById(R.id.address_select_ck);
            holder.address_default_iv = (ImageView) view.findViewById(R.id.address_default_iv);
            holder.delete = (Button) view.findViewById(R.id.delete);
            holder.edit = (Button) view.findViewById(R.id.edit);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.edit.setText("设为默认");
        AddressBean item = items.get(i);
        holder.address_name_tv.setText(item.getUsername() == null ? "无" : item.getUsername());
        holder.address_phone_tv.setText(item.getTel());
        holder.address_add_tv.setText(item.getAddress());
        if(item.getUsed()!=null && item.getUsed().equals("1")){
            holder.address_default_iv.setVisibility(View.VISIBLE);
        }else{
            holder.address_default_iv.setVisibility(View.GONE);
        }
        RxView.clicks(holder.delete).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Delete(i));
        RxView.clicks(holder.edit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Default(i));
        if (i % 2 == 0) {
            holder.address_list_item_lay.setCardBackgroundColor(0x88BCBBBB);
        } else {
            holder.address_list_item_lay.setCardBackgroundColor(0x33BCBBBB);
        }
        return view;
    }

    private void Default(int i) {
        new MaterialDialog.Builder(context)
                .title("提示")
                .content("您确定把此地址设置为默认？")
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)
                .autoDismiss(true)
                .onPositive((materialDialog, dialogAction) -> {
                    listener.Default(i);
                })
                .show();
    }

    public void setListener(IAddressAdapter listener) {
        this.listener = listener;
    }

    private void Delete(int position) {
        KLog.v("position"+position);
        new MaterialDialog.Builder(context)
                .title("提示")
                .content("您确定删除此地址？")
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)
                .autoDismiss(true)

                .onPositive((materialDialog, dialogAction) -> {
                    KLog.v("position-"+position);
                    listener.Delete(position);
                })
                .show();
    }

    class ViewHolder {
        TextView address_name_tv, address_phone_tv, address_add_tv;
        ImageView address_default_iv;
        CheckBox address_select_ck;
        CardView address_list_item_lay;
        Button delete;
        Button edit;
    }

    public interface IAddressAdapter {
        void dissmiss();

        void Default(int i);

        void Delete(int position);
    }

}
