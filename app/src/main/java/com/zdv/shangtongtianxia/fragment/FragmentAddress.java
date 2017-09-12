package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.AddressItemAdapter;
import com.zdv.shangtongtianxia.bean.AddressBean;
import com.zdv.shangtongtianxia.customView.slideItemView.SlideListView;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.service.AddressPickTask;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IAddressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.ResponseBody;

public class FragmentAddress extends BaseFragment implements IAddressView, AddressItemAdapter.IAddressAdapter {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    ArrayList<AddressBean> a_data;

    @Bind(R.id.address_data_list)
    SlideListView address_data_list;
    @Bind(R.id.address_add_lay)
    LinearLayout address_add_lay;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;

    AddressItemAdapter a_adapter;

    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    private PopupWindow popupWindow;
    View popupWindowView;
    AddressBean addressBean;
    EditText address_name_et, address_phone_et, address_detail_et;
    TextView address_location_tv_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(FragmentAddress.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        popupWindowView = View.inflate(getContext(), R.layout.pop_address, null);

        RxView.clicks(address_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Add());

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());

        header_title.setText("我的地址");

        a_adapter = new AddressItemAdapter(getContext(), a_data);
        a_adapter.setListener(FragmentAddress.this);
    /*    address_data_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(a_adapter, address_data_list);
        address_data_list.setEmptyView(empty_lay);
        address_data_list.setAdapter(animatorApdapter);*/
        address_data_list.setAdapter(a_adapter);

        //    setEmptyStatus(false);
        a_adapter.notifyDataSetChanged();
        fetchFromNetWork();

    }

    public void Delete(int i) {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        addressBean = a_data.get(i);
        present.QueryDeleteAddress(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"), addressBean.getCode());
    }

    public void Default(int i) {
        KLog.v("I=" + i);
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        addressBean = a_data.get(i);
        present.QueryDefaultAddress(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"), addressBean.getCode());
    }

    private void Add() {
        ShowPopupWindow(address_add_lay);
    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));

            Button address_save_lay = (Button) popupWindowView.findViewById(R.id.address_save_lay);
            LinearLayout shut_btn_lay = (LinearLayout) popupWindowView.findViewById(R.id.shut_btn_lay);
            address_name_et = (EditText) popupWindowView.findViewById(R.id.address_name_et);
            address_phone_et = (EditText) popupWindowView.findViewById(R.id.address_phone_et);
            address_location_tv_show = (TextView) popupWindowView.findViewById(R.id.address_location_tv_show);
            address_detail_et = (EditText) popupWindowView.findViewById(R.id.address_detail_et);
            address_name_et.setText("");
            address_phone_et.setText("");
            address_detail_et.setText("");
           // address_detail_et.setText(Constant.location == null ? "" : Constant.location.getAddrStr());
            RxView.clicks(address_save_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Save());
            RxView.clicks(shut_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> popupWindow.dismiss());
            RxView.clicks(address_location_tv_show).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> initLocation());

        }
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(view,
                Gravity.CENTER | Gravity.BOTTOM, 0, 0);

    }

    private void Save() {
        if (address_name_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入收件人姓名");
            return;
        }
        if (!util.verifyPhone(address_phone_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确的手机号");
            return;
        }
        if (address_location_tv_show.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入收件人地址");
            return;
        }
        if (address_detail_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入收件人地址");
            return;
        }
        addressBean = new AddressBean();
        addressBean.setAddress_name(address_name_et.getText().toString().trim());
        addressBean.setAddress_phone(address_phone_et.getText().toString().trim());
        addressBean.setAddress_str(address_detail_et.getText().toString().trim());
        addressBean.setAddress_district_str(address_location_tv_show.getText().toString().trim());
        showWaitDialog("正在保存");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryAddAddress(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                address_name_et.getText().toString().trim(), address_phone_et.getText().toString().trim()
                , address_location_tv_show.getText().toString().trim(), address_detail_et.getText().toString().trim());
    }

    @Override
    public void refreshState() {

        if (a_data != null) {
            a_data.clear();
            a_adapter.notifyDataSetChanged();
        }
        fetchFromNetWork();
        super.refreshState();
    }

    private void initLocation() {
        AddressPickTask task = new AddressPickTask(getActivity());
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                VToast.toast(getContext(), "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    address_location_tv_show.setText(province.getAreaName() + city.getAreaName());
                } else {
                    address_location_tv_show.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute("广东", "深圳", "南山");

    }
  /*  public void onConstellationPicker(View view) {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        OptionPicker picker = new OptionPicker(this,
                isChinese ? new String[]{
                        "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
                        "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
                } : new String[]{
                        "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                        "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                });
        picker.setCycleDisable(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(30);
        picker.setTopLineColor(0xFFEE0000);
        picker.setTopLineHeight(1);
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFFEE0000);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFFEE0000);
        picker.setSubmitTextSize(13);
        picker.setTextColor(0xFFEE0000, 0xFF999999);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setDividerConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(7);
        picker.setCanceledOnTouchOutside(true);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item);
            }
        });
        picker.show();
    }*/

    public void Back() {
        super.Back();
        popupWindowView.destroyDrawingCache();
        popupWindow = null;
        listener.gotoProfile();
    }

    protected void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.smile);
            empty_tv.setText("没有地址记录");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryAddress(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));

    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentAddress.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        a_data = new ArrayList<>();

    }

    @Override
    public void dissmiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    @Override
    public void ResolveAddress(ResponseBody info) {
        hideWaitDialog();
        if (info == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        //  VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            a_data.clear();
            a_data.addAll(JSON.parseArray(jsonObject.optString("content"), AddressBean.class));
            a_adapter.notifyDataSetChanged();
            if (a_data.size() == 1 && a_data.get(0).getUsed().equals("0")) {
                Default(0);
            }
        }
    }


    @Override
    public void ResolveAddAddress(ResponseBody info) {
        hideWaitDialog();
        if (info == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            popupWindow.dismiss();
            fetchFromNetWork();
        }
    }

    @Override
    public void ResolveDeleteAddress(ResponseBody info) {
        hideWaitDialog();
        if (info == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            a_data.remove(addressBean);
            a_adapter.notifyDataSetChanged();
            if (a_data.size() == 1) {
                Default(0);
            }
        }
    }

    @Override
    public void ResolveDefaultAddress(ResponseBody info) {
        hideWaitDialog();
        if (info == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            fetchFromNetWork();

        }
    }
}
