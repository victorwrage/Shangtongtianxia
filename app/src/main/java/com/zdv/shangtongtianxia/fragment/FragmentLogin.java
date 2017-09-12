package com.zdv.shangtongtianxia.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.tencent.smtt.sdk.QbSdk;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.service.AdvanceLoadX5Service;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;
import okhttp3.ResponseBody;

public class FragmentLogin extends BaseFragment implements IUserView {

    private final static int INIT_FINISHED = 1001;
    @Bind(R.id.login_user_et)
    EditText login_user_et;
    @Bind(R.id.login_password_et)
    EditText login_password_et;
    @Bind(R.id.login_login_btn)
    Button login_login_btn;
    @Bind(R.id.login_remember_btn)
    CheckBox login_remember_btn;
    @Bind(R.id.login_auto_btn)
    CheckBox login_auto_btn;
    @Bind(R.id.update_iv)
    ImageView update_iv;
    @Bind(R.id.update_tv)
    TextView update_tv;
    @Bind(R.id.update_lay)
    LinearLayout update_lay;

    @Bind(R.id.login_auto_activity)
    RelativeLayout login_auto_activity;


    @Bind(R.id.login_register_tv)
    TextView login_register_tv;
    @Bind(R.id.login_forget_tv)
    TextView login_forget_tv;
    @Bind(R.id.login_show_iv)
    CheckBox login_show_iv;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_FINISHED:
                    VToast.toast(getContext(), "你好 " + Constant.user_info.optString("name"));
                    hidSoftInput();
                    listener.gotoMain();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(FragmentLogin.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        login_remember_btn.setChecked(sp.getBoolean("remember", true));
        login_auto_btn.setChecked(sp.getBoolean("auto_login", true));
        if (sp.getBoolean("remember", true)) {
            login_user_et.setText(sp.getString("user_name", ""));
            login_password_et.setText(sp.getString("user_pw", ""));
        }

        RxView.clicks(login_login_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Login());
        RxView.clicks(login_register_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Register());
        RxView.clicks(login_forget_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Forget());
        login_password_et.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                    Login();
                    break;
            }
            return false;
        });
        login_show_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw(b);
        });
        RxView.clicks(update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> BmobUpdateAgent.forceUpdate(getContext()));
        update_tv.setText("当前版本" + util.getAppVersionName(getContext()));
        login_auto_btn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                login_remember_btn.setChecked(true);
            }
        });
        handler.postDelayed(() -> AutoLogin(), 2000);
    }

    private void AutoLogin() {
        if (login_auto_btn.isChecked()) {
            if (login_user_et.getText().toString().trim().equals("") || login_password_et.getText().toString().trim().equals("")) {
                login_auto_activity.setVisibility(View.GONE);
                return;
            }
            showWaitDialog("正在登录");
            present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
            present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), login_user_et.getText().toString().trim(),
                    util.getMD5(login_password_et.getText().toString().trim()));
        }else{
            login_auto_activity.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshState() {
        super.refreshState();
        login_show_iv.setChecked(false);
        login_user_et.setText(sp.getString("user_name", ""));
        login_password_et.setText(sp.getString("user_pw", ""));
        login_remember_btn.setChecked(sp.getBoolean("remember", true));
        login_auto_btn.setChecked(sp.getBoolean("auto_login", true));
        login_auto_activity.setVisibility(View.GONE);
    }

    @Override
    public void Back() {
        super.Back();
    }

    private void Forget() {
        listener.gotoForget();
    }

    private void Register() {
        listener.gotoRegister();
    }

    private void visiblePw(Boolean b) {
        if (b) {
            login_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            login_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        login_password_et.postInvalidate();
        CharSequence charSequence = login_password_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void initDate() {
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(getContext());
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.setView(FragmentLogin.this);

        util = Utils.getInstance();
    }

    private void Login() {
        if (!util.isNetworkConnected(getContext())) {
            VToast.toast(getContext(), "没有网络连接");
            return;
        }
        if (!util.verifyPhone(login_user_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确的手机号");
        } else if (login_password_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入密码");
        } else {

            showWaitDialog("正在登录");
            present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
            present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), login_user_et.getText().toString().trim(),
                    util.getMD5(login_password_et.getText().toString().trim()));
        }
    }


    private void saveInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name", login_user_et.getText().toString().trim());
        editor.putString("user_pw", login_password_et.getText().toString().trim());
        if (login_remember_btn.isChecked()) {
            editor.putBoolean("remember", login_remember_btn.isChecked());
        } else {
            editor.putBoolean("remember", false);
        }
        if (login_auto_btn.isChecked()) {
            editor.putBoolean("remember", true);
            editor.putBoolean("auto_login", true);
        } else {
            editor.putBoolean("auto_login", false);
        }
        editor.commit();
    }

    @Override
    public void ResolveLoginInfo(ResponseBody info) {
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            hideWaitDialog();
            login_auto_activity.setVisibility(View.GONE);
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);
            Constant.user_info = jsonObject.optString(Constant.ERRCODE).equals(SUCCESS) ? new JSONObject(jsonObject.optString("content")) : null;
        } catch (JSONException e) {
            login_auto_activity.setVisibility(View.GONE);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            login_auto_activity.setVisibility(View.GONE);
            VToast.toast(getContext(), "网络超时");
            hideWaitDialog();
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            saveInfo();
            executor.execute(() -> {
                preinitX5WebCore();
                //预加载x5内核
                Intent intent = new Intent(getContext(), AdvanceLoadX5Service.class);
                getContext().startService(intent);

                handler.sendEmptyMessage(INIT_FINISHED);
            });


        } else {
            login_auto_activity.setVisibility(View.GONE);
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }

    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(getContext(), null);// 设置X5初始化完成的回调接口
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
