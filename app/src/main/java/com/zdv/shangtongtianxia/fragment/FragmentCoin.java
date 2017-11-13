package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.CoinHistoryItemAdapter;
import com.zdv.shangtongtianxia.bean.HistoryBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IMemberView;
import com.zdv.shangtongtianxia.view.IPayView;
import com.zdv.shangtongtianxia.view.IUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import lib.homhomlib.design.SlidingLayout;
import okhttp3.ResponseBody;

/**
 * 商通币
 */
public class FragmentCoin extends BaseFragment implements IPayView,IMemberView ,IUserView{

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    @Bind(R.id.coin_avatar_iv)
    ImageView coin_avatar_iv;
    @Bind(R.id.coin_total_balance_tv)
    TextView coin_total_balance_tv;
    @Bind(R.id.coin_total_income_tv)
    TextView coin_total_income_tv;
    @Bind(R.id.coin_total_outcome_tv)
    TextView coin_total_outcome_tv;

    @Bind(R.id.coin_trasnfer)
    LinearLayout coin_trasnfer;
    @Bind(R.id.coin_recharge)
    LinearLayout coin_recharge;
    @Bind(R.id.coin_bill)
    LinearLayout coin_bill;
    @Bind(R.id.coin_buy_lay)
    RelativeLayout coin_buy_lay;
    @Bind(R.id.coin_sign_lay)
    RelativeLayout coin_sign_lay;
    @Bind(R.id.coin_about_lay)
    RelativeLayout coin_about_lay;
    @Bind(R.id.coin_history_list)
    RecyclerViewWithEmpty coin_history_list;
    @Bind(R.id.coin_step1_lay)
    RelativeLayout coin_step1_lay;
    @Bind(R.id.coin_step0_lay)
    LinearLayout coin_step0_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;

    @Bind(R.id.slidingLayout)
    SlidingLayout slidingLayout;

    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;
    private CoinHistoryItemAdapter a_adapter;
    private ArrayList<HistoryBean> a_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin, container, false);
        ButterKnife.bind(FragmentCoin.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        coin_total_balance_tv.setText("余额:" + (Constant.user_info == null ? "0" : Constant.user_info.optString("integral")) + "枚");
        coin_total_income_tv.setText("0");
        coin_total_outcome_tv.setText("0");
        Login();
    }

    private void Login() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), sp.getString("user_name", ""),
                util.getMD5(sp.getString("user_pw", "")));
    }

    private void initView() {

        RxView.clicks(coin_trasnfer).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Transfer());
        RxView.clicks(coin_recharge).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Recharge());
        RxView.clicks(coin_bill).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Bill());
        RxView.clicks(coin_buy_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Buy());
        RxView.clicks(coin_sign_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Sign());
        RxView.clicks(coin_about_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> About());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("商通红包");

        coin_total_balance_tv.setText("商通币:" + (Constant.user_info == null ? "0" : Constant.user_info.optString("integral")) + "枚");
        coin_total_income_tv.setText("0");
        coin_total_outcome_tv.setText("0");

        a_adapter = new CoinHistoryItemAdapter(a_data, getContext());
        coin_history_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(a_adapter, coin_history_list);
        coin_history_list.setEmptyView(empty_lay);
        coin_history_list.setAdapter(animatorAdapter);

        setEmptyStatus(false);
        a_adapter.notifyDataSetChanged();

        slidingLayout.setSlidingListener(new SlidingLayout.SlidingListener() {
            @Override
            public void onSlidingOffset(View view, float v) {
                if (v > 60) {
                    Login();
                }
            }

            @Override
            public void onSlidingStateChange(View view, int i) {

            }

            @Override
            public void onSlidingChangePointer(View view, int i) {

            }
        });

    }

    public void Back() {
        super.Back();
        if (coin_step1_lay.getVisibility() == View.VISIBLE) {
            coin_step0_lay.setVisibility(View.VISIBLE);
            coin_step1_lay.setVisibility(View.GONE);
            return;
        }
        listener.gotoWealth();
    }


    private void About() {
        VToast.toast(getContext(),"暂未开通");
      /*  Constant.InDirectTo = 2;
        listener.gotoCoinAbout();*/
    }

    private void Sign() {
        showWaitDialog("正在签到");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
        present.QueryMemberSign(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),"1");
    }

    private void Buy() {
        if(Constant.user_info.optString("microshop_id").equals("0")){
            Constant.InDirectTo = 2;
            listener.gotoWebview();
        }else {
            listener.gotoShop();
        }
    }

    private void Bill() {
        coin_step0_lay.setVisibility(View.GONE);
        coin_step1_lay.setVisibility(View.VISIBLE);
        showWaitDialog("请稍等");
        fetchFromNetWork();
    }

    private void Transfer() {
        Constant.InDirectTo = 1;
        listener.gotoPay();
    }

    private void Recharge() {
        listener.gotoChargeCoin();
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
            empty_tv.setText("没有充值记录");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryResumeRecord(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentCoin.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        a_data = new ArrayList<>();
    }


    @Override
    public void ResolveSearchMemberOrder(ResponseBody info) {

    }

    @Override
    public void ResolveQRCode(ResponseBody info) {

    }

    @Override
    public void ResolveScanQrcode(ResponseBody info) {

    }

    @Override
    public void ResolveChargeSTCoin(ResponseBody info) {

    }

    @Override
    public void ResolveTradeHistory(ResponseBody info) {

    }

    @Override
    public void ResolveSetPayPassword(ResponseBody info) {

    }

    @Override
    public void ResolveVerifyPayPassword(ResponseBody info) {

    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            hideWaitDialog();
            setEmptyStatus(true);
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
            setEmptyStatus(true);
            hideWaitDialog();
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            a_data.clear();
            setEmptyStatus(false);
            String str = jsonObject.optString("content");
            ArrayList<HistoryBean> temp = new ArrayList<>();
            temp.addAll(JSON.parseArray(str, HistoryBean.class));
            for(HistoryBean historyBean:temp){
                if(historyBean.getCost_state().equals("5") || historyBean.getCost_state().equals("4")){
                    a_data.add(historyBean);
                }
            }
            a_adapter.notifyDataSetChanged();
            KLog.v(str);
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }


    @Override
    public void ResolveActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveApplyActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveMemberSign(ResponseBody info) {
        hideWaitDialog();
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
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
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            VToast.toast(getContext(),"签到成功");
        }else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }

    }

    @Override
    public void ResolveMessage(ResponseBody info) {

    }

    @Override
    public void ResolveActorFirm(ResponseBody info) {

    }

    @Override
    public void ResolveMemberPayDetail(ResponseBody info) {

    }

    @Override
    public void ResolveVerifyMember(ResponseBody info) {

    }

    @Override
    public void ResolveTeamMember(ResponseBody info) {

    }

    @Override
    public void ResolveIsVerify(ResponseBody info) {

    }

    @Override
    public void ResolveLoginInfo(ResponseBody info) {
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
            Constant.user_info = new JSONObject(jsonObject.optString("content"));
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            coin_total_balance_tv.setText("商通币:" + (Constant.user_info == null ? "0" : Constant.user_info.optString("integral")) + "枚");
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    @Override
    public void ResolveRegisterInfo(ResponseBody info) {

    }

    @Override
    public void ResolveForgetInfo(ResponseBody info) {

    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {

    }
}
