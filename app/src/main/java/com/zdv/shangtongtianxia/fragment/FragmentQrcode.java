package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.view.IView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Info: QR
 * Created by xiaoyl
 * 创建时间:2017/8/8 18:42
 */
public class FragmentQrcode extends BaseFragment implements IView {
    private final int QRCODE_FINISH = 111;


    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.qr_name_tv)
    TextView qr_name_tv;
    @Bind(R.id.qr_add_tv)
    TextView qr_add_tv;
    @Bind(R.id.qr_scan_tv)
    TextView qr_scan_tv;

    @Bind(R.id.qr_qcode_iv)
    ImageView qr_qcode_iv;

    Bitmap qrCodeBitmap;
    View view;
    String qcode;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QRCODE_FINISH:
                    qr_qcode_iv.setImageBitmap(qrCodeBitmap);
                    hideWaitDialog();

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.qr_lay, container, false);
        ButterKnife.bind(FragmentQrcode.this, view);
        return view;
    }


    @Override
    public void Back() {
        super.Back();
        listener.gotoProfile();
    }

    @Override
    public void refreshState() {
        super.refreshState();
      //  qcode = Constant.user_info == null ? "" : Constant.user_info.optString("code&")+Constant.user_info.optString("name");
        qcode = "http://t.cn/RNg0dqH";
        qr_name_tv.setText(Constant.user_info == null ? "" : Constant.user_info.optString("name"));
        qr_add_tv.setText(Constant.user_info == null ? "" :  Constant.user_info.optString("company_name"));
        generQr();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentQrcode.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }

    protected void initView() {
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(qr_scan_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Share());
        header_title.setText("我的二维码");
        qcode = Constant.user_info == null ? "" : Constant.user_info.optString("code&")+Constant.user_info.optString("name");
        qr_name_tv.setText(Constant.user_info == null ? "" : Constant.user_info.optString("name"));
        qr_add_tv.setText(Constant.user_info == null ? "" :  Constant.user_info.optString("company_name"));
        generQr();
    }

    private void Share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "hi,我是"+Constant.user_info.optString("name") + " 邀请你体验【商通天下】，邀请码为:" + Constant.user_info.optString("tel")
                + "\r\n下载地址:http://t.cn/RNg0dqH");

        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享好友"));
    }

    private void generQr() {
        showWaitDialog("正在生成二维码");
        executor.execute(() -> syncEncodeQRCode());
    }

    private void syncEncodeQRCode() {
        qrCodeBitmap = QRCodeEncoder.syncEncodeQRCode(qcode, 320, R.color.shangtongtianx_txt, BitmapFactory.decodeResource(getResources(), R.drawable.profit_icon_avatar));
        handler.sendEmptyMessage(QRCODE_FINISH);

    }

}
