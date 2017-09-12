package com.zdv.shangtongtianxia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FragmentProfile extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";

	@Bind(R.id.header_btn)
    ImageView header_btn;
	@Bind(R.id.header_title)
    TextView header_title;
	@Bind(R.id.header_btn_lay)
	LinearLayout header_btn_lay;

	@Bind(R.id.profile_account_tv)
	TextView profile_account_tv;
	@Bind(R.id.profile_phone_tv)
	TextView profile_phone_tv;
	@Bind(R.id.profile_bank_num)
	TextView profile_bank_num;
	@Bind(R.id.profile_bank_tv)
	TextView profile_bank_tv;

	@Bind(R.id.profile_account_lay)
	RelativeLayout profile_account_lay;
	@Bind(R.id.profile_phone_lay)
	RelativeLayout profile_phone_lay;
	@Bind(R.id.profile_edit_pw_lay)
	RelativeLayout profile_edit_pw_lay;
	@Bind(R.id.profile_verify_lay)
	RelativeLayout profile_verify_lay;
	@Bind(R.id.profile_qrcode_lay)
	RelativeLayout profile_qrcode_lay;
	@Bind(R.id.profile_bank_num_lay)
	RelativeLayout profile_bank_num_lay;
	@Bind(R.id.profile_bank_lay)
	RelativeLayout profile_bank_lay;
	@Bind(R.id.profile_add_lay)
	RelativeLayout profile_add_lay;
	@Bind(R.id.profile_pay_pw_lay)
	RelativeLayout profile_pay_pw_lay;


	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		ButterKnife.bind(FragmentProfile.this,view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initDate();
		initView();
	}

	private void initView() {
		RxView.clicks(profile_account_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Account());
		RxView.clicks(profile_phone_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Phone());
		RxView.clicks(profile_edit_pw_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> EditPW());
		RxView.clicks(profile_verify_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Verify());
		RxView.clicks(profile_qrcode_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> QRcode());
		RxView.clicks(profile_bank_num_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> BankNum());
		RxView.clicks(profile_bank_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Bank());
		RxView.clicks(profile_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Add());
		RxView.clicks(profile_pay_pw_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> PayPW());

		RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.gotoUserManager());
		header_title.setText("个人资料");
	}

	private void initDate() {
		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		profile_account_tv.setText(Constant.user_info==null?"":Constant.user_info.optString("name"));
		profile_phone_tv.setText(Constant.user_info==null?"":Constant.user_info.optString("tel").length()==11?util.getPhoneEncrypt(Constant.user_info.optString("tel")):"");
		profile_bank_num.setText(Constant.user_info==null?"":"");
		profile_bank_tv.setText(Constant.user_info==null?"":"");
	}

	@Override
	public void refreshState() {
		super.refreshState();
		header_title.setText("个人资料");
	}

	@Override
	public void Back() {
		super.Back();
		listener.gotoUserManager();
	}

	private void PayPW() {
		listener.gotoPassword();
	}

	private void Add() {
		listener.gotoAddress();
	}

	private void Bank() {

	}

	private void BankNum() {
		VToast.toast(getContext(),"暂未开通");
		/*Constant.InDirectTo = 1;
		listener.gotoWithdraw();*/
	}

	private void QRcode() {
		listener.gotoQRcode();
	}

	private void Verify() {
       listener.gotoVerify();
	}

	private void EditPW() {
		Constant.InDirectTo = 1;
		listener.gotoForget();
	}

	private void Phone() {

	}

	private void Account() {

	}
}
