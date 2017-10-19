package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentWealth extends BaseFragment implements IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    @Bind(R.id.wealth_coin)
    LinearLayout wealth_coin;
    @Bind(R.id.wealth_profit)
    LinearLayout wealth_profit;
    @Bind(R.id.wealth_recharge)
    LinearLayout wealth_recharge;
    @Bind(R.id.wealth_spread)
    LinearLayout wealth_spread;
    @Bind(R.id.wealth_history)
    LinearLayout wealth_history;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wealth, container, false);
        ButterKnife.bind(FragmentWealth.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(wealth_coin).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Coin());
        RxView.clicks(wealth_profit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Profit());
        RxView.clicks(wealth_recharge).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Recharge());
        RxView.clicks(wealth_spread).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Share());
        RxView.clicks(wealth_history).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Search());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("财富");
    }

    @Override
    public void refreshState() {
        super.refreshState();
    }

    public void Back() {
        super.Back();
        listener.gotoMain();
    }

    private void Search() {
        listener.gotoResearch();
    }

    private void Share() {
        if (Constant.user_info == null) {
            VToast.toast(getContext(), "请先注册");
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
  /*      shareIntent.putExtra(Intent.EXTRA_TEXT, Constant.user_info.optString("name") + " 邀请你体验【商通天下】，邀请码为:" + Constant.user_info.optString("tel")
                + "\r\n下载地址:http://t.cn/RNg0dqH");*/
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constant.user_info.optString("name") + " 邀请你体验【商通天下】，欢迎点击下载:" + Constant.PUBLIC_SHARE_URL + Constant.user_info.optString("tel"));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享邀请码"));
    }

    private void Recharge() {
        listener.gotoRecharge();
    }

    private void Profit() {
        listener.gotoProfit();
    }

    private void Coin() {
        listener.gotoCoin();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentWealth.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }

}
