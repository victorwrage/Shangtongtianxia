package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentShop extends BaseFragment implements IPayView,IMemberView ,IUserView{

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.shop_name_tv)
    TextView shop_name_tv;

    @Bind(R.id.shop_waiting_lay)
    LinearLayout shop_waiting_lay;
    @Bind(R.id.shop_sending_lay)
    LinearLayout shop_sending_lay;
    @Bind(R.id.shop_receive_lay)
    LinearLayout shop_receive_lay;
    @Bind(R.id.shop_refund_lay)
    LinearLayout shop_refund_lay;
    @Bind(R.id.shop_finish_lay)
    LinearLayout shop_finish_lay;


    @Bind(R.id.shop_order_lay)
    RelativeLayout shop_order_lay;
    @Bind(R.id.shop_settle_lay)
    RelativeLayout shop_settle_lay;
    @Bind(R.id.shop_pay_lay)
    RelativeLayout shop_pay_lay;
    @Bind(R.id.shop_manager_lay)
    RelativeLayout shop_manager_lay;
    @Bind(R.id.shop_store_lay)
    RelativeLayout shop_store_lay;
    @Bind(R.id.shop_coupon_lay)
    RelativeLayout shop_coupon_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(FragmentShop.this, view);

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
    }

    private void Login() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), sp.getString("user_name", ""),
                util.getMD5(sp.getString("user_pw", "")));
    }

    private void initView() {

        RxView.clicks(shop_waiting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Waiting());
        RxView.clicks(shop_sending_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Sending());
        RxView.clicks(shop_receive_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Receive());
        RxView.clicks(shop_refund_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Refund());
        RxView.clicks(shop_finish_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Finished());
        RxView.clicks(shop_order_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Order());
        RxView.clicks(shop_settle_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Settle());
        RxView.clicks(shop_pay_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Pay());
        RxView.clicks(shop_manager_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Manager());
        RxView.clicks(shop_store_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Store());
        RxView.clicks(shop_coupon_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Coupon());


        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("我的微店");

    }

    private void Coupon() {
        listener.gotoCouponManager();
    }

    private void Store() {
        Constant.InDirectTo = 10;
        listener.gotoWebview();

    }

    private void Manager() {
        Constant.InDirectTo = 11;
        listener.gotoWebview();
    }

    private void Pay() {
        Constant.InDirectTo = 12;
        listener.gotoWebview();
    }

    private void Order() {
        Constant.InDirectTo = 13;
        listener.gotoWebview();
    }

    public void Back() {
        super.Back();
        listener.gotoUserManager();
    }


    private void Settle() {
        Constant.InDirectTo = 14;
        listener.gotoWebview();
    }

    private void Finished() {
        Constant.InDirectTo = 15;
        listener.gotoWebview();
    }

    private void Refund() {
        Constant.InDirectTo = 16;
        listener.gotoWebview();
    }

    private void Receive() {
        Constant.InDirectTo = 17;
        listener.gotoWebview();
    }

    private void Waiting() {
        Constant.InDirectTo = 18;
        listener.gotoWebview();
    }

    private void Sending() {
        Constant.InDirectTo = 19;
        listener.gotoWebview();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentShop.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

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

    }


    @Override
    public void ResolveActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveApplyActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveMemberSign(ResponseBody info) {


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
