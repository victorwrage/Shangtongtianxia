package com.zdv.shangtongtianxia.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 设置
 */
public class FragmentSetting extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
    @Bind(R.id.header_btn)
    ImageView header_btn;
    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.setting_help_lay)
    RelativeLayout setting_help_lay;
    @Bind(R.id.setting_update_lay)
    RelativeLayout setting_update_lay;
    @Bind(R.id.setting_hot_line_lay)
    RelativeLayout setting_hot_line_lay;
    @Bind(R.id.setting_feedback_lay)
    RelativeLayout setting_feedback_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;

    @Bind(R.id.setting_version_tv)
    TextView setting_version_tv;
    @Bind(R.id.setting_version_title_tv)
    TextView setting_version_title_tv;

    @Bind(R.id.setting_logout_btn)
    Button setting_logout_btn;
    @Bind(R.id.setting_phone_num)
    TextView setting_phone_num;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    private String SetAlias = "setAlias";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_lay, container, false);
        ButterKnife.bind(FragmentSetting.this, view);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        sp = getContext().getSharedPreferences( COOKIE_KEY,0);
        RxView.clicks(setting_hot_line_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> HotLine());

        RxView.clicks(setting_help_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Helper());
        RxView.clicks(setting_update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> versionUpdate());
        RxView.clicks(setting_feedback_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> FeedBack());
        RxView.clicks(setting_logout_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Logout());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("设置");
        setting_version_title_tv.setText("商通天下" + util.getAppVersionName(getContext()));
        setting_version_tv.setText(util.getAppVersionName(getContext()));
        return view;
    }


    @Override
    public void Back() {
        super.Back();
        listener.gotoUserManager();
    }

    private void HotLine() {
        new MaterialDialog.Builder(getContext())
                .title("提示")
                .content("是否拨打客服电话？")
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)

                .autoDismiss(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+setting_phone_num.getText().toString().trim().replaceAll("-","")));
                        startActivity(intent);
                    }
                })
                .show();


      /*  new CommomDialog(getContext(), R.style.dialog, "是否拨打客服电话？", (dialog, confirm) -> {
            if (confirm) {

            }

        }).setTitle("提示").show();*/
    }

    private void Helper() {
        VToast.toast(getContext(),"敬请期待");
    }

    private void Logout() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
  /*      editor.putBoolean(SetAlias, false);

        editor.putBoolean("remember", false);
        editor.putBoolean("auto_login", false);
        editor.commit();
*/
        Constant.user_info = null;
        Constant.MESSAGE_UPDATE_TIP = "";
        listener.gotoLogin();
    }

    private void FeedBack() {
        listener.gotoFeedBack();
    }

    private void versionUpdate() {
        BmobUpdateAgent.forceUpdate(getContext());
    }

}
