package com.zdv.shangtongtianxia.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentMain extends BaseFragment implements IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    View view;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;
    WebView tbsContent;
    @Bind(R.id.main_stub)
    ViewStub main_stub;
    @Bind(R.id.main_content_lay)
    RelativeLayout main_content_lay;


/*    @Bind(R.id.slidingLayout)
    SlidingLayout slidingLayout;*/
    @Bind(R.id.main_scan_lay)
    LinearLayout main_scan_lay;
    @Bind(R.id.main_pay_lay)
    LinearLayout main_pay_lay;
    //WebView tbsContent;

    private boolean isInit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(FragmentMain.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    @JavascriptInterface
    private void initView() {
        RxView.clicks(main_scan_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Scan());
        RxView.clicks(main_pay_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Pay());
     /*   slidingLayout.setSlidingListener(new SlidingLayout.SlidingListener() {
            @Override
            public void onSlidingOffset(View view, float v) {
                if (v > 60) {
                    if (tbsContent != null) {
                        empty_lay.setVisibility(View.VISIBLE);
                        tbsContent.reload();
                    }
                }
            }

            @Override
            public void onSlidingStateChange(View view, int i) {

            }

            @Override
            public void onSlidingChangePointer(View view, int i) {

            }
        });*/
    }


    private void Pay() {
        listener.gotoPay();
    }

    private void Scan() {
        Acp.getInstance(getContext()).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
          /*      .setDeniedMessage("获取摄像头权限失败")
                .setDeniedCloseBtn("关闭")
                .setDeniedSettingBtn("设置")
                .setRationalMessage("")
                .setRationalBtn("好")*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        listener.gotoScan();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        VToast.toast(getContext(),"请打开摄像头权限");
                    }
                });
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentMain.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        Acp.getInstance(getContext()).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.INTERNET)
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        VToast.toast(getContext(),"没有网络权限");
                    }
                });

        if (!isInit) {
            isInit = true;
            main_stub.setVisibility(View.VISIBLE);
            tbsContent = (WebView) view.findViewById(R.id.tbsContent);
            WebSettings webSettings = tbsContent.getSettings();
            webSettings.setJavaScriptEnabled(true);

            tbsContent.loadUrl("http://wdt.qianhaiwei.com/Project/BeforeSea/shoppingMall/index.html?key=L8NNvvqqqZr&company_id=5777#");


            tbsContent.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);

                    empty_lay.setVisibility(View.GONE);
                    tbsContent.setVisibility(View.VISIBLE);
                    //  }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
        if(tbsContent != null){
            tbsContent.onResume();
        }
    }

    @Override
    public void onPause() {
        tbsContent.onPause();
        super.onPause();
    }

}
