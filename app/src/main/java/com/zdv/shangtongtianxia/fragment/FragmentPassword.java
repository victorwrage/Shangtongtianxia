package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IPayView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentPassword extends BaseFragment implements IPayView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.pay_step2_lay)
    LinearLayout pay_step2_lay;
    @Bind(R.id.pay_step_lay)
    LinearLayout pay_step_lay;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;

    @Bind(R.id.pay_password_et)
    EditText pay_password_et;
    @Bind(R.id.pay_password2_et)
    EditText pay_password2_et;

    @Bind(R.id.pay_pw1_show_iv)
    CheckBox pay_pw1_show_iv;
    @Bind(R.id.pay_pw2_show_iv)
    CheckBox pay_pw2_show_iv;

    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.pay_reset_btn)
    Button pay_reset_btn;
    @Bind(R.id.pay_step1_btn)
    Button pay_step1_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_pw, container, false);
        ButterKnife.bind(FragmentPassword.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(pay_step1_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Submit());
        RxView.clicks(pay_reset_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Reset());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Back());

        pay_pw1_show_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw(b);
        });
        pay_pw2_show_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw2(b);
        });
        header_title.setText("支付密码");

    }

    private void visiblePw(Boolean b) {
        if (b) {
            pay_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            pay_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        pay_password_et.postInvalidate();
        CharSequence charSequence = pay_password_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void visiblePw2(Boolean b) {
        if (b) {
            pay_password2_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            pay_password2_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        pay_password2_et.postInvalidate();
        CharSequence charSequence = pay_password2_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void Submit() {
        if(pay_password_et.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入登录密码");
            return;
        }
        if(pay_password2_et.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入支付密码");
            return;
        }
        if(pay_password2_et.getText().toString().trim().length()!=6){
            VToast.toast(getContext(),"支付密码须为6位数字");
            return;
        }
        showWaitDialog("正在提交");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
        present.QuerySetPayPassword(util.UrlEnco(Constant.WDT_SECRET),Constant.user_info.optString("code"),util.getMD5(pay_password_et.getText().toString().trim()),
                pay_password2_et.getText().toString().trim());
    }

    private void Reset() {
        pay_step_lay.setVisibility(View.GONE);
        pay_step2_lay.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshState() {
        super.refreshState();
        pay_step_lay.setVisibility(View.VISIBLE);
        pay_step2_lay.setVisibility(View.GONE);
        pay_password_et.setText("");
        pay_password2_et.setText("");

    }

    public void Back() {
        super.Back();
        if(pay_step2_lay.getVisibility()==View.VISIBLE){
            pay_step_lay.setVisibility(View.VISIBLE);
            pay_step2_lay.setVisibility(View.GONE);
            return;
        }
        listener.gotoProfile();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentPassword.this);
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
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            hideWaitDialog();
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
            hideWaitDialog();
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            VToast.toast(getContext(), "设置成功");
            listener.gotoProfile();
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveVerifyPayPassword(ResponseBody info) {

    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {

    }

}
