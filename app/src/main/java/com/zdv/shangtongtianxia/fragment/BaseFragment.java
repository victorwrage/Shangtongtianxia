package com.zdv.shangtongtianxia.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.customView.ProgressBarItem;
import com.zdv.shangtongtianxia.view.IFragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class BaseFragment extends Fragment {
    protected final String COOKIE_KEY = "cookie";
    protected static final String SUCCESS = "200";
    protected static final int DIALOG_TYPE_1 = 0;
    protected static final int DIALOG_TYPE_2 = 1;
    protected static final int DIALOG_TYPE_3 = 2;
    protected Executor executor;
    protected IFragmentActivity listener;
    protected View popupWindowViewVerify;
    protected PopupWindow popupWindowVerify;
    protected TextView verify_cash_tv, verify_btn_txt0, verify_btn_txt1, verify_btn_txt2, verify_btn_txt3, verify_btn_txt4, verify_btn_txt5, verify_btn_txt6,
            verify_btn_txt7, verify_btn_txt8, verify_btn_txt9, verify_pw_tv1, verify_pw_tv2, verify_pw_tv3, verify_pw_tv4, verify_pw_tv5, verify_pw_tv6;
    protected ImageView btn_delete;
    protected LinearLayout verify_shut_lay;
    protected String input_pw = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor  = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        popupWindowViewVerify = View.inflate(getContext(), R.layout.pop_verify, null);

    }

    protected void showWaitDialog(String tip){
        ProgressBarItem.show(getContext(),tip,false,null);
    }

    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    protected void startLoading(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        imageView.startAnimation(rotate);
    }

    protected void stopLoading(ImageView imageView) {
        imageView.setVisibility(View.GONE);
        imageView.clearAnimation();
    }

    protected void showPopupWindow2(int ids,String type,String count) {
        if (popupWindowVerify == null) {
            popupWindowVerify = new PopupWindow(popupWindowViewVerify, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindowVerify.setAnimationStyle(R.style.AnimationBottomFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindowVerify.setBackgroundDrawable(dw);
        }
        popupWindowVerify.setOnDismissListener(() -> backgroundAlpha(1.0f));
        backgroundAlpha(0.5f);
        initVerifyView(type,count);
        popupWindowVerify.showAtLocation(View.inflate(getContext(), ids, null),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void initVerifyView(String type, String count) {
        verify_cash_tv = (TextView) popupWindowViewVerify.findViewById(R.id.verify_cash_tv);
        verify_btn_txt0 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt0);
        verify_btn_txt1 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt1);
        verify_btn_txt2 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt2);
        verify_btn_txt3 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt3);
        verify_btn_txt4 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt4);
        verify_btn_txt5 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt5);
        verify_btn_txt6 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt6);
        verify_btn_txt7 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt7);
        verify_btn_txt8 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt8);
        verify_btn_txt9 = (TextView) popupWindowViewVerify.findViewById(R.id.btn_txt9);
        verify_pw_tv1 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv1);
        verify_pw_tv2 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv2);
        verify_pw_tv3 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv3);
        verify_pw_tv4 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv4);
        verify_pw_tv5 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv5);
        verify_pw_tv6 = (TextView) popupWindowViewVerify.findViewById(R.id.verify_pw_tv6);
        btn_delete = (ImageView) popupWindowViewVerify.findViewById(R.id.btn_delete);
        verify_shut_lay = (LinearLayout) popupWindowViewVerify.findViewById(R.id.verify_shut_lay);
        RxView.clicks(verify_btn_txt1).subscribe(s -> textBtn2('1'));
        RxView.clicks(verify_btn_txt2).subscribe(s -> textBtn2('2'));
        RxView.clicks(verify_btn_txt3).subscribe(s -> textBtn2('3'));
        RxView.clicks(verify_btn_txt4).subscribe(s -> textBtn2('4'));
        RxView.clicks(verify_btn_txt5).subscribe(s -> textBtn2('5'));
        RxView.clicks(verify_btn_txt6).subscribe(s -> textBtn2('6'));
        RxView.clicks(verify_btn_txt7).subscribe(s -> textBtn2('7'));
        RxView.clicks(verify_btn_txt8).subscribe(s -> textBtn2('8'));
        RxView.clicks(verify_btn_txt9).subscribe(s -> textBtn2('9'));
        RxView.clicks(verify_btn_txt0).subscribe(s -> textBtn2('0'));
        input_pw = "";
        verify_pw_tv1.setText("");
        verify_pw_tv2.setText("");
        verify_pw_tv3.setText("");
        verify_pw_tv4.setText("");
        verify_pw_tv5.setText("");
        verify_pw_tv6.setText("");
        verify_cash_tv.setText(type+"支付:" + count);
        RxView.clicks(btn_delete).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> del2());
        RxView.clicks(verify_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> popupWindowVerify.dismiss());

    }

    protected void backgroundAlpha(float v) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = v;
        getActivity().getWindow().setAttributes(lp);
    }

    protected void textBtn2(char paramChar) {
        if (input_pw.length() == 6) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(input_pw.toCharArray()).append(paramChar);
        input_pw = sb.toString();
        switch (input_pw.length()) {
            case 1:
                verify_pw_tv1.setText(paramChar + "");
                break;
            case 2:
                verify_pw_tv2.setText(paramChar + "");
                break;
            case 3:
                verify_pw_tv3.setText(paramChar + "");
                break;
            case 4:
                verify_pw_tv4.setText(paramChar + "");
                break;
            case 5:
                verify_pw_tv5.setText(paramChar + "");
                break;
            case 6:
                verify_pw_tv6.setText(paramChar + "");
                break;
        }
        if (input_pw.length() > 5) {//最大长度
            confirm2(input_pw.toString());
        }

    }

    protected void del2() {
        if (input_pw.length() == 0) {
            return;
        }
        input_pw = input_pw.substring(0, input_pw.length() - 1);

        switch (input_pw.length()) {
            case 0:
                verify_pw_tv1.setText("");
                break;
            case 1:
                verify_pw_tv2.setText("");
                break;
            case 2:
                verify_pw_tv3.setText("");
                break;
            case 3:
                verify_pw_tv4.setText("");
                break;
            case 4:
                verify_pw_tv5.setText("");
                break;
            case 5:
                verify_pw_tv6.setText("");
                break;
        }
    }

    protected void confirm2(String pw) {
           popupWindowVerify.dismiss();
    }

    public void Back(){
         hidSoftInput();
    }

    protected void hidSoftInput(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View v = new View(getContext());
            ViewGroup g1 = (ViewGroup)getActivity().getWindow().getDecorView();
            ViewGroup g2 = (ViewGroup)g1.getChildAt(0);
            g2.addView(v);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    AlertDialog dialog;
    protected void showDialog(int type, String title, String tip) {
        new MaterialDialog.Builder(getContext())
                .title(title)
                .content(tip)
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)
                .onNegative((materialDialog, dialogAction) -> cancel(type))
                .autoDismiss(true)
                .onNeutral((materialDialog, dialogAction) -> confirm(type))
                .show();
    }

    public void refreshState(){

    }
    protected void cancel(int type) {
    }

    protected void confirm(int type) {
        dialog.dismiss();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IFragmentActivity) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

}
