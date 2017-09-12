package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class FragmentProfit extends BaseFragment implements IView{


    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    @Bind(R.id.profit_avatar_iv)
    ImageView profit_avatar_iv;
    @Bind(R.id.profit_total_profit_tv)
    TextView profit_total_profit_tv;
    @Bind(R.id.profit_total_income_tv)
    TextView profit_total_income_tv;
    @Bind(R.id.profit_total_outcome_tv)
    TextView profit_total_outcome_tv;


    @Bind(R.id.profit_terminal_lay)
    RelativeLayout profit_terminal_lay;
    @Bind(R.id.profit_alliance_lay)
    RelativeLayout profit_alliance_lay;
    @Bind(R.id.profit_mall_lay)
    RelativeLayout profit_mall_lay;
    @Bind(R.id.profit_member_lay)
    RelativeLayout profit_member_lay;
    @Bind(R.id.profit_introduce_lay)
    RelativeLayout profit_introduce_lay;
    @Bind(R.id.profit_pos_lay)
    RelativeLayout profit_pos_lay;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;
    @Bind(R.id.header_right_tv)
    TextView header_right_tv;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profit, container, false);
        ButterKnife.bind(FragmentProfit.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(profit_terminal_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Terminal());
        RxView.clicks(profit_alliance_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Alliance());
        RxView.clicks(profit_mall_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Mall());
        RxView.clicks(profit_member_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Member());
        RxView.clicks(profit_introduce_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Introduce());
        RxView.clicks(profit_pos_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Pos());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Back());
        RxView.clicks(header_edit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Withdraw());
        header_title.setText("我的收益");
        header_edit_lay.setVisibility(View.VISIBLE);
        header_right_tv.setText("提现");
        profit_total_profit_tv.setText(Constant.user_info==null?"0":Constant.user_info.optString("balance"));
        profit_total_income_tv.setText("0");
        profit_total_outcome_tv.setText("0");
    }

    private void Withdraw() {
        VToast.toast(getContext(),"暂未开通");
       // listener.gotoWithdraw();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        profit_total_profit_tv.setText(Constant.user_info==null?"0":Constant.user_info.optString("balance"));
    }

    public void Back() {
        super.Back();
        listener.gotoWealth();
    }

    private void Pos() {
        listener.gotoPosProfit();
    }

    private void Introduce() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void Member() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void Mall() {
        VToast.toast(getContext(),"暂未开通");
       //listener.gotoMall();
    }

    private void Terminal() {
        listener.gotoTerminal();
    }

    private void Alliance() {
        VToast.toast(getContext(),"暂未开通");
       // listener.gotoAlliance();
    }


    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentProfit.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

    }


}
