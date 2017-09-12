package com.zdv.shangtongtianxia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentFeedBack extends BaseFragment {

    @Bind(R.id.header_btn)
    ImageView header_btn;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;

    @Bind(R.id.feedback_content)
    EditText feedback_content;
    @Bind(R.id.feedback_contact_et)
    EditText feedback_contact_et;

    @Bind(R.id.feedback_upload_iv)
    ImageView feedback_upload_iv;

    @Bind(R.id.feedback_commit_btn)
    Button feedback_commit_btn;

    QueryPresent present;
    Utils util;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        ButterKnife.bind(FragmentFeedBack.this,view);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());

        RxView.clicks(feedback_upload_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> updateImg());
        RxView.clicks(feedback_commit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> commit());

        header_title.setText("反馈");

        return view;
    }

    @Override
    public void Back() {
        super.Back();
        listener.gotoSetting();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        feedback_content.setText("");
        feedback_contact_et.setText("");
    }

    private void commit() {
        if(feedback_content.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入反馈内容");
            return;
        }
        if(feedback_contact_et.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入联系方式");
            return;
        }
        VToast.toast(getContext(),"谢谢您的反馈!");
        listener.gotoSetting();
    }

    private void updateImg() {

    }



}
