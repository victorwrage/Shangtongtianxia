package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.BankItemAdapter;
import com.zdv.shangtongtianxia.bean.BankCardBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IPayView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import okhttp3.ResponseBody;

public class FragmentWithdraw extends BaseFragment implements IPayView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    View view;
    RelativeLayout withdraw_bank_card_lay, empty_lay;
    EditText withdraw_name_et, withdraw_card_et, withdraw_bank_num_et, withdraw_phone_et, withdraw_cvn2_et;
    TextView withdraw_type_sp, withdraw_bank_sp;
    ImageView withdraw_icon1, empty_iv, header_setting_iv;
    TextView withdraw_bank_name, withdraw_bank_num, withdraw_cash_tv, withdraw_balance_tv, withdraw_all_tv, empty_tv, withdraw_available_tv;
    Button withdraw_submit_btn, withdraw_add_btn;
    LinearLayout header_btn_lay, withdraw_step1, withdraw_step2, withdraw_code_lay, withdraw_step3, header_setting_lay,withdraw_cvn2_lay,withdraw_available_lay;
    TextView header_title;
    RecyclerViewWithEmpty withdraw_bank_list;
    @Bind(R.id.btn_txt0)
    TextView btn_txt0;
    @Bind(R.id.btn_txt1)
    TextView btn_txt1;
    @Bind(R.id.btn_txt2)
    TextView btn_txt2;
    @Bind(R.id.btn_txt3)
    TextView btn_txt3;
    @Bind(R.id.btn_txt4)
    TextView btn_txt4;
    @Bind(R.id.btn_txt5)
    TextView btn_txt5;
    @Bind(R.id.btn_txt6)
    TextView btn_txt6;
    @Bind(R.id.btn_txt7)
    TextView btn_txt7;
    @Bind(R.id.btn_txt8)
    TextView btn_txt8;
    @Bind(R.id.btn_txt9)
    TextView btn_txt9;
    @Bind(R.id.btn_dot)
    TextView btn_dot;
    @Bind(R.id.btn_del)
    TextView btn_del;
    @Bind(R.id.btn_confirm)
    TextView btn_confirm;
    @Bind(R.id.btn_cancel)
    TextView btn_cancel;
    @Bind(R.id.tv_digit)
    TextView tv_digit;
    View popupWindowView;
    View popupWindowViewBank;
    View popupWindowViewCardType;
    private PopupWindow popupWindow;
    private PopupWindow popupWindowBank;
    private PopupWindow popupWindowCardType;
    Boolean isFromOther = false;

    ArrayList<BankCardBean> b_data;
    BankItemAdapter b_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_withdraw, container, false);
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
        input_pw = "";
        withdraw_cash_tv.setText("");
        withdraw_name_et.setText("");
        withdraw_card_et.setText("");
        withdraw_bank_num_et.setText("");
        withdraw_phone_et.setText("");
        withdraw_cvn2_et.setText("");
        withdraw_type_sp.setText("储蓄卡");
        withdraw_bank_sp.setText("工商银行");
        withdraw_cvn2_lay.setVisibility(View.GONE);
        withdraw_available_lay.setVisibility(View.GONE);
        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        popupWindowViewVerify = View.inflate(getContext(), R.layout.pop_verify, null);
        ButterKnife.bind(FragmentWithdraw.this, popupWindowView);
        isFromOther = false;
        b_data.clear();
        b_data.add(new BankCardBean());
        b_adapter.notifyDataSetChanged();
        if(Constant.InDirectTo==1){
            ChooseBank();
        }else{
            withdraw_step1.setVisibility(View.VISIBLE);
            withdraw_step2.setVisibility(View.GONE);
            header_title.setText("提现申请");
            header_setting_lay.setVisibility(View.GONE);
        }
    }

    private void initView() {
        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        popupWindowViewBank = View.inflate(getContext(),R.layout.pop_menu, null);
        popupWindowViewCardType = View.inflate(getContext(),R.layout.pop_menu, null);

        ButterKnife.bind(FragmentWithdraw.this, popupWindowView);

        withdraw_bank_card_lay = (RelativeLayout) view.findViewById(R.id.withdraw_bank_card_lay);
        empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);
        withdraw_step1 = (LinearLayout) view.findViewById(R.id.withdraw_step1);
        withdraw_step2 = (LinearLayout) view.findViewById(R.id.withdraw_step2);
        withdraw_bank_list = (RecyclerViewWithEmpty) view.findViewById(R.id.withdraw_bank_list);
        withdraw_icon1 = (ImageView) view.findViewById(R.id.withdraw_icon1);
        empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        withdraw_icon1 = (ImageView) view.findViewById(R.id.withdraw_icon1);
        header_setting_iv = (ImageView) view.findViewById(R.id.header_setting_iv);
        empty_tv = (TextView) view.findViewById(R.id.empty_tv);
        withdraw_bank_name = (TextView) view.findViewById(R.id.withdraw_bank_name);
        withdraw_bank_num = (TextView) view.findViewById(R.id.withdraw_bank_num);
        withdraw_cash_tv = (TextView) view.findViewById(R.id.withdraw_cash_tv);
        withdraw_balance_tv = (TextView) view.findViewById(R.id.withdraw_balance_tv);
        withdraw_all_tv = (TextView) view.findViewById(R.id.withdraw_all_tv);
        withdraw_submit_btn = (Button) view.findViewById(R.id.withdraw_submit_btn);
        withdraw_add_btn = (Button) view.findViewById(R.id.withdraw_add_btn);
        header_btn_lay = (LinearLayout) view.findViewById(R.id.header_btn_lay);
        header_title = (TextView) view.findViewById(R.id.header_title);
        withdraw_name_et = (EditText) view.findViewById(R.id.withdraw_name_et);
        withdraw_card_et = (EditText) view.findViewById(R.id.withdraw_card_et);
        withdraw_bank_num_et = (EditText) view.findViewById(R.id.withdraw_bank_num_et);
        withdraw_phone_et = (EditText) view.findViewById(R.id.withdraw_phone_et);
        withdraw_cvn2_et = (EditText) view.findViewById(R.id.withdraw_cvn2_et);

        withdraw_type_sp = (TextView) view.findViewById(R.id.withdraw_type_sp);
        withdraw_available_tv = (TextView) view.findViewById(R.id.withdraw_available_tv);
        withdraw_bank_sp = (TextView) view.findViewById(R.id.withdraw_bank_sp);

        withdraw_code_lay = (LinearLayout) view.findViewById(R.id.withdraw_code_lay);
        withdraw_step3 = (LinearLayout) view.findViewById(R.id.withdraw_step3);
        header_setting_lay = (LinearLayout) view.findViewById(R.id.header_setting_lay);
        withdraw_cvn2_lay = (LinearLayout) view.findViewById(R.id.withdraw_cvn2_lay);
        withdraw_available_lay = (LinearLayout) view.findViewById(R.id.withdraw_available_lay);

        RxView.clicks(withdraw_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit());
        RxView.clicks(withdraw_cash_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showPopupWindow());
        RxView.clicks(withdraw_type_sp).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ShowPopupWindowCardType(withdraw_type_sp));
        RxView.clicks(withdraw_bank_sp).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ShowPopupWindowBank(withdraw_bank_sp));
        RxView.clicks(withdraw_all_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> WithdrawAll());
        RxView.clicks(withdraw_bank_card_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ChooseBank());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        withdraw_type_sp.setText("储蓄卡");
        withdraw_bank_sp.setText("工商银行");
        withdraw_cvn2_lay.setVisibility(View.GONE);
        withdraw_available_lay.setVisibility(View.GONE);
        header_title.setText("提现申请");
        setEmptyStatus(false);

        b_adapter = new BankItemAdapter(b_data, getContext());
        withdraw_bank_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(b_adapter, withdraw_bank_list);
        withdraw_bank_list.setEmptyView(empty_lay);
        withdraw_bank_list.setAdapter(animatorAdapter);
        setEmptyStatus(false);
        b_data.add(new BankCardBean());
        b_adapter.notifyDataSetChanged();
        if(Constant.InDirectTo==1){
            ChooseBank();
        }
    }


    private void ShowPopupWindowBank(View view) {
        if (popupWindowBank == null) {
            popupWindowBank = new PopupWindow(popupWindowViewBank, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
         //   popupWindowBank.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindowBank.setBackgroundDrawable(dw);
         //   popupWindowBank.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("工商银行");
            menu_data.add("建设银行");
            menu_data.add("农业银行");
            menu_data.add("兴业银行");
            menu_data.add("浦发银行");
            ArrayAdapter<String> menu_adapter;
            menu_adapter =  new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowViewBank.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                withdraw_bank_sp.setText(menu_data.get(i));
                popupWindowBank.dismiss();
            });
        }
        popupWindowBank.showAtLocation(view,
                Gravity.CENTER | Gravity.BOTTOM, 20,30);

    }

    private void ShowPopupWindowCardType(View view) {
        if (popupWindowCardType == null) {
            popupWindowCardType = new PopupWindow(popupWindowViewCardType, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
          //  popupWindowCardType.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindowCardType.setBackgroundDrawable(dw);
          //  popupWindowCardType.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("储蓄卡");
            menu_data.add("信用卡");
            ArrayAdapter<String> menu_adapter;
            menu_adapter =  new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowViewCardType.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                if(i==0){
                    withdraw_type_sp.setText(menu_data.get(i));
                    withdraw_cvn2_lay.setVisibility(View.GONE);
                    withdraw_available_lay.setVisibility(View.GONE);
                }else{
                    withdraw_type_sp.setText(menu_data.get(i));
                    withdraw_cvn2_lay.setVisibility(View.VISIBLE);
                    withdraw_available_lay.setVisibility(View.VISIBLE);
                }
                popupWindowCardType.dismiss();
            });
        }
        popupWindowCardType.showAtLocation(view,
                Gravity.CENTER | Gravity.CENTER, 20,20);

    }
    private void ChooseBank() {
        withdraw_step1.setVisibility(View.GONE);
        withdraw_step2.setVisibility(View.VISIBLE);
        withdraw_step3.setVisibility(View.GONE);
        header_setting_lay.setVisibility(View.VISIBLE);
        header_setting_iv.setImageResource(R.drawable.add);
        RxView.clicks(header_setting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> AddBankCard());
        header_title.setText("选择银行卡");
    }

    private void AddBankCard() {
        withdraw_step2.setVisibility(View.GONE);
        withdraw_step3.setVisibility(View.VISIBLE);
        header_setting_lay.setVisibility(View.GONE);
        header_title.setText("添加银行卡");
    }

    private void WithdrawAll() {

    }

    private void Submit() {
        showPopupWindow2(R.layout.fragment_withdraw
                ,"提现金额",withdraw_cash_tv.getText().toString());
    }

    protected void setEmptyStatus(boolean isOffLine) {

        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.smile);
            empty_tv.setText("没有绑定银行卡");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {

    }

    private void showPopupWindow() {
        btn_dot.setVisibility(View.VISIBLE);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
            passwordLis();
        }
        tv_digit.setText("0");
        popupWindow.showAtLocation(View.inflate(getContext(), R.layout.pay_lay, null),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void passwordLis() {
        RxView.clicks(btn_txt1).subscribe(s -> textBtn('1'));
        RxView.clicks(btn_txt2).subscribe(s -> textBtn('2'));
        RxView.clicks(btn_txt3).subscribe(s -> textBtn('3'));
        RxView.clicks(btn_txt4).subscribe(s -> textBtn('4'));
        RxView.clicks(btn_txt5).subscribe(s -> textBtn('5'));
        RxView.clicks(btn_txt6).subscribe(s -> textBtn('6'));
        RxView.clicks(btn_txt7).subscribe(s -> textBtn('7'));
        RxView.clicks(btn_txt8).subscribe(s -> textBtn('8'));
        RxView.clicks(btn_txt9).subscribe(s -> textBtn('9'));
        RxView.clicks(btn_txt0).subscribe(s -> textBtn('0'));
        RxView.clicks(btn_dot).subscribe(s -> textBtn('.'));
        RxView.clicks(btn_del).subscribe(s -> del());
        RxView.clicks(btn_confirm).subscribe(s -> confirm());
        RxView.clicks(btn_cancel).subscribe(s -> clear());
    }

    private void clear() {
        tv_digit.setText("0");
    }

    /**
     * 显示并格式化输入
     *
     * @param paramChar
     */
    private void textBtn(char paramChar) {
        StringBuilder sb = new StringBuilder();
        String val = tv_digit.getText().toString();

        if (val.indexOf(".") == val.length() - 3 && val.length() > 3) {//小数点后面保留两位
            return;
        }
        if (paramChar == '.' && val.indexOf(".") != -1) {//只出现一次小数点
            return;
        }
        if (paramChar == '0' && val.charAt(0) == '0' && val.indexOf(".") == -1) {//no 0000
            return;
        }
        if (val.length() > 10) {//最大长度
            return;
        }
        sb.append(val.toCharArray()).append(paramChar);

        if (sb.length() > 1 && sb.charAt(0) == '0' && sb.charAt(1) != '.') {
            sb.deleteCharAt(0);
        }
        tv_digit.setText(sb.toString());
    }

    /**
     * 退格
     */
    private void del() {
        char[] chars = tv_digit.getText().toString().toCharArray();

        if (chars.length == 1) {
            tv_digit.setText("0");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(chars);
        sb.deleteCharAt(sb.length() - 1);
        if (sb.charAt(sb.length() - 1) == '.') {
            sb.deleteCharAt(sb.length() - 1);
        }
        tv_digit.setText(sb.toString());
    }

    /**
     * 确定
     */
    private void confirm() {
        popupWindow.dismiss();
        String d = tv_digit.getText().toString();
        if (d.indexOf(".") == -1) {
            d += ".00";
        }
        if (d.indexOf(".") == d.length() - 1) {
            d += "00";
        }
        if (d.equals("0.0") || d.equals("0.00")) {
            d = "0";
        }
        withdraw_cash_tv.setText("￥" + d);
    }


    public void Back() {
        super.Back();
        if (withdraw_step3.getVisibility() == View.VISIBLE) {
            ChooseBank();
            return;
        }

       if(Constant.InDirectTo==1){
            ChooseBank();
            Constant.InDirectTo = 0;
            listener.gotoProfile();
            return;
        }
        if (withdraw_step2.getVisibility() == View.VISIBLE) {
            withdraw_step1.setVisibility(View.VISIBLE);
            withdraw_step2.setVisibility(View.GONE);
            header_title.setText("提现申请");
            header_setting_lay.setVisibility(View.GONE);
            return;
        }

        popupWindowViewVerify.destroyDrawingCache();
        popupWindowView.destroyDrawingCache();
        popupWindow = null;
        popupWindowVerify = null;
        listener.gotoProfit();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentWithdraw.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        b_data = new ArrayList<>();
        b_adapter = new BankItemAdapter(b_data,getContext());
    }

    @Override
    protected void confirm2(String pw) {
        super.confirm2(pw);
        showWaitDialog("正在验证");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
        present.QueryVerifyPayPassword(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                pw);
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

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if ( jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            Withdraw();
        }else{
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    private void Withdraw() {
        VToast.toast(getContext(),"提现暂未开通");
    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {

    }
}
