package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * 《我的》页面
 */
public class FragmentUserManager extends BaseFragment implements IMemberView {
    private static final String COOKIE_KEY = "cookie";
    private static final String ALIAS_KEY = "alias";


    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_setting_lay)
    LinearLayout header_setting_lay;

    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.user_profile_lay)
    RelativeLayout user_profile_lay;
    @Bind(R.id.user_actor_lay)
    RelativeLayout user_actor_lay;
    @Bind(R.id.user_shop_lay)
    RelativeLayout user_shop_lay;
    @Bind(R.id.user_alliance_lay)
    RelativeLayout user_alliance_lay;
    @Bind(R.id.user_team_lay)
    RelativeLayout user_team_lay;

    @Bind(R.id.user_order_lay)
    RelativeLayout user_order_lay;
    @Bind(R.id.user_add_lay)
    RelativeLayout user_add_lay;
    @Bind(R.id.user_hongbao_lay)
    RelativeLayout user_hongbao_lay;
    @Bind(R.id.user_coupon_lay)
    RelativeLayout user_coupon_lay;

    @Bind(R.id.manager_nickname_tv)
    TextView manager_nickname_tv;
    @Bind(R.id.manager_level_tv)
    TextView manager_level_tv;

    @Bind(R.id.user_my_profile_unread)
    ImageView user_my_profile_unread;

    private PopupWindow popupWindow;
    View popupWindowView;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;

    private String AwardCount = "-1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_lay, container, false);
        ButterKnife.bind(FragmentUserManager.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentUserManager.this);
    }

    private void initView() {

        popupWindowView = View.inflate(getContext(), R.layout.pop_red, null);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.gotoMain());
        RxView.clicks(header_setting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Setting());
        RxView.clicks(user_profile_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Profile());
        RxView.clicks(user_actor_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Actor());
        RxView.clicks(user_shop_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Shop());
        RxView.clicks(user_team_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Team());
        RxView.clicks(user_alliance_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Alliance());
        RxView.clicks(user_order_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Order());
        RxView.clicks(user_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Add());
        RxView.clicks(user_hongbao_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Hongbao());
        RxView.clicks(user_coupon_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Coupon());

        header_setting_lay.setVisibility(View.VISIBLE);
        header_title.setText("个人中心");
        manager_nickname_tv.setText(Constant.user_info != null ? Constant.user_info.optString("name") : "未注册");
        manager_level_tv.setText(Constant.user_info != null ? Constant.user_info.optString("level_name") : "游客");
    }

    private void Coupon() {
         listener.gotoCoupon();
    }

    private void Team() {
        listener.gotoTeam();
    }

    @Override
    public void Back() {
        super.Back();
        listener.gotoMain();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        manager_nickname_tv.setText(Constant.user_info != null ? Constant.user_info.optString("name") : "未注册");
        manager_level_tv.setText(Constant.user_info != null ? Constant.user_info.optString("level_name") : "游客");
    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, (int) util.convertDpToPixel(300f, getContext()),
                    (int) util.convertDpToPixel(420f, getContext()), true);
            popupWindow.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
            Button open_btn = (Button) popupWindowView.findViewById(R.id.open_btn);
            Button close = (Button) popupWindowView.findViewById(R.id.close);
            TextView textView3 = (TextView) popupWindowView.findViewById(R.id.textView3);
            TextView textView2 = (TextView) popupWindowView.findViewById(R.id.textView2);
            AnimationDrawable animationDrawable = (AnimationDrawable) open_btn.getBackground();
            RxView.clicks(close).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> {
                popupWindow.dismiss();
                popupWindow =null;
            });
            RxView.clicks(open_btn).throttleFirst(1500, TimeUnit.MILLISECONDS).subscribe(s -> {
                animationDrawable.start();
                RxView.clicks(open_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s1 -> {
                    open_btn.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setText("商通币"+AwardCount+"个");
                });
            });
        }
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(view,
                Gravity.CENTER | Gravity.CENTER, 0, 0);

    }

    private void Choujiang() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
        present.QueryMemberSign(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),"2");
    }


    private void Hongbao() {
        Choujiang();
    }

    private void Add() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void Order() {
        listener.gotoOrder();
    }

    private void Alliance() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void Shop() {

        if(Constant.user_info.optString("microshop_id").equals("0")){
            Constant.InDirectTo = 3;
            listener.gotoWebview();
        }else {
            listener.gotoShop();
        }
    }

    private void Actor() {
        listener.gotoActor();
    }

    private void Profile() {
        listener.gotoProfile();
    }

    private void Setting() {
        listener.gotoSetting();
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

            return;
        }
        // VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));

        if(jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)){
            AwardCount =  jsonObject.optString("integral");

            ShowPopupWindow(user_hongbao_lay);
        }else{
            VToast.toast(getContext(),"已经抽过奖了，别太贪心哦!");
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
}
