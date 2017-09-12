package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IMemberView;
import com.zdv.shangtongtianxia.view.IPayView;
import com.zdv.shangtongtianxia.view.IUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import okhttp3.ResponseBody;

/**
 * Info: 充值
 * Created by xiaoyl
 * 创建时间:2017/8/8 18:42
 */

public class FragmentChargeSTCoin extends BaseFragment implements IPayView, IUserView, IMemberView {
    private final int QRCODE_FINISH = 111;
    ImageView header_btn, pay_qcode_iv;
    TextView header_title, pay_set_tv, pay_save_tv, pay_cash_tv, pay_receive_type;
    LinearLayout header_btn_lay;
    Button pay_generate_qcode;
    RelativeLayout pay_step_1, pay_step_2;
    Spinner pay_tunnel_cs, pay_type_cs;

    protected ArrayList<String> pay_types = new ArrayList<>();
    protected ArrayList<String> type_types = new ArrayList<>();
    protected ArrayAdapter<String> bankAdapter;
    protected ArrayAdapter<String> typeAdapter;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    View popupWindowView;

    Bitmap qrCodeBitmap;
    private PopupWindow popupWindow;
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

    View view;
    String qcode = "qcode";
    int pay_type = 0;
    int charge_type = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QRCODE_FINISH:
                  //  listener.print();
                    hideWaitDialog();
                    pay_receive_type.setText("个人充值");
                    pay_qcode_iv.setImageBitmap(qrCodeBitmap);
                    pay_step_1.setVisibility(View.GONE);
                    pay_step_2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.charge_lay, container, false);
        return view;
    }

    @Override
    public void Back() {
        super.Back();
        if (pay_step_2.getVisibility() == View.VISIBLE) {
            SearchOrder();
            return;
        }
        popupWindowView.destroyDrawingCache();
        popupWindow = null;
        Login();
    }

    @Override
    public void refreshState() {
        super.refreshState();
        pay_type = 0;
        qcode = "qcode";
        qrCodeBitmap = null;
        pay_cash_tv.setText("0");
        pay_tunnel_cs.setSelection(0);

        popupWindowView.destroyDrawingCache();
        popupWindow = null;
        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        ButterKnife.bind(FragmentChargeSTCoin.this, popupWindowView);

    }

    private void generate() {

        showWaitDialog("正在获取二维码");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryChargeSTCoin(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"), pay_cash_tv.getText().toString().trim(),
                (pay_type + 3) + "", charge_type + "", "1");

    }

    /**
     * 查询充值结果
     */
    private void SearchOrder() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryMemberPayDetail(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                Constant.temp_info.get(Constant.ORDER_ID));
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
        present.setView(FragmentChargeSTCoin.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        ButterKnife.bind(FragmentChargeSTCoin.this, popupWindowView);
        type_types.add("商通币");
        type_types.add("余额");

        pay_types.add("微信");
        pay_types.add("支付宝");
        pay_types.add("QQ钱包");
    }

    protected void initView() {
        header_btn = (ImageView) view.findViewById(R.id.header_btn);
        pay_qcode_iv = (ImageView) view.findViewById(R.id.pay_qcode_iv);
        header_title = (TextView) view.findViewById(R.id.header_title);
        pay_cash_tv = (TextView) view.findViewById(R.id.pay_cash_tv);
        pay_set_tv = (TextView) view.findViewById(R.id.pay_set_tv);
        pay_save_tv = (TextView) view.findViewById(R.id.pay_save_tv);
        pay_receive_type = (TextView) view.findViewById(R.id.pay_receive_type);
        header_btn_lay = (LinearLayout) view.findViewById(R.id.header_btn_lay);
        pay_step_1 = (RelativeLayout) view.findViewById(R.id.pay_step_1);
        pay_step_2 = (RelativeLayout) view.findViewById(R.id.pay_step_2);
        /**         充值方式             **/
        pay_tunnel_cs = (Spinner) view.findViewById(R.id.pay_tunnel_cs);
        bankAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pay_types);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pay_tunnel_cs.setAdapter(bankAdapter);
        pay_tunnel_cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                charge_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**         充值类型             **/
        pay_type_cs = (Spinner) view.findViewById(R.id.pay_type_cs);
        typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, type_types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pay_type_cs.setAdapter(typeAdapter);
        pay_type_cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pay_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pay_step_1 = (RelativeLayout) view.findViewById(R.id.pay_step_1);
        pay_generate_qcode = (Button) view.findViewById(R.id.pay_generate_qcode);

        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        ButterKnife.bind(FragmentChargeSTCoin.this, popupWindowView);
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(pay_save_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Save());
        RxView.clicks(pay_generate_qcode).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> doQuery());
        RxView.clicks(pay_cash_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showPopupWindow());
        RxView.clicks(pay_set_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        if (Constant.InDirectTo == 1) {
            pay_tunnel_cs.setSelection(1);
            pay_tunnel_cs.setEnabled(false);
        }
        header_title.setText("充值");
    }


    private void Save() {
        util.saveImageToGallery(getContext(), qrCodeBitmap);
        VToast.toast(getContext(), "二维码已经保存");
    }

    private void doQuery() {
        if (Integer.parseInt(pay_cash_tv.getText().toString()) == 0) {
            VToast.toast(getContext(), "支付金额不能为0");
            return;
        }
        showPopupWindow2(R.layout.pay_lay, pay_types.get(pay_tunnel_cs.getSelectedItemPosition()), pay_cash_tv.getText().toString());

    }

    private void Login() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), sp.getString("user_name", ""),
                util.getMD5(sp.getString("user_pw", "")));
    }

    @Override
    protected void confirm2(String pw) {
        super.confirm2(pw);
        showWaitDialog("正在验证");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryVerifyPayPassword(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                pw);
    }

    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
        }
        passwordLis();
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
        if (val.length() > 30) {//最大长度
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
        int s = Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.optString("integral"));
       /* if (Integer.parseInt(tv_digit.getText().toString()) > s) {
            VToast.toast(getContext(), "不能大于账户余额");
            return;
        }*/
        String d = tv_digit.getText().toString();

        pay_cash_tv.setText(d);
    }

    @Override
    public void ResolveSearchMemberOrder(ResponseBody info) {

    }

    @Override
    public void ResolveQRCode(ResponseBody info) {

    }

    private void syncEncodeQRCode() {
        String bottom = "";
        switch (charge_type) {
            case 0:
                bottom = "微信支付:" + pay_cash_tv.getText().toString().trim();
                break;
            case 1:
                bottom = "支付宝支付:" + pay_cash_tv.getText().toString().trim();
                break;
            case 2:
                bottom = "QQ钱包支付:" + pay_cash_tv.getText().toString().trim();
                break;
        }
        Constant.PAY_TOTAL = pay_cash_tv.getText().toString().trim();
        bottom += "(元)";
        KLog.v("syncEncodeQRCode0");
        Bitmap temp_bitmap = QRCodeEncoder.syncEncodeQRCode(qcode, 350, Color.BLACK, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        Bitmap topBmp = util.getImage(temp_bitmap.getWidth(), 30, "商通天下充值二维码", 30, getResources().getColor(R.color.shangtongtianx_btn_back));
        Bitmap bottomBmp = util.getImage(temp_bitmap.getWidth(), 36, bottom, 36, getResources().getColor(R.color.shangtongtianx_txt));

        qrCodeBitmap = util.addTopBmp(topBmp, bottomBmp, temp_bitmap);
        KLog.v("syncEncodeQRCode1");
        handler.sendEmptyMessage(QRCODE_FINISH);

    }

    @Override
    public void ResolveScanQrcode(ResponseBody info) {

    }

    @Override
    public void ResolveChargeSTCoin(ResponseBody info) {
        hideWaitDialog();
        qcode = "qcode";
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            info.close();
            KLog.v(res);
            jsonObject = new JSONObject(res);
            JSONObject jsobj = new JSONObject(jsonObject.optString("content"));
            qcode = jsobj.optString("qrcode");
            Constant.temp_info.put(Constant.ORDER_ID, jsobj.optString("order_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            showWaitDialog("正在生成二维码");
            executor.execute(() -> syncEncodeQRCode());
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
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
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            qcode = "";
            Login();
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {

    }

    @Override
    public void ResolveLoginInfo(ResponseBody info) {
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
            Constant.user_info = new JSONObject(jsonObject.optString("content"));
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            if (qcode.equals("")) {
                generate();
            } else {

                listener.gotoCoin();
            }
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
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

    @Override
    public void ResolveActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveApplyActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveMemberSign(ResponseBody info) {

    }

    @Override
    public void ResolveMessage(ResponseBody info) {

    }

    @Override
    public void ResolveActorFirm(ResponseBody info) {

    }

    @Override
    public void ResolveMemberPayDetail(ResponseBody info) {
        if (info.source() == null) {
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
            hideWaitDialog();
            return;
        }
        pay_step_1.setVisibility(View.VISIBLE);
        pay_step_2.setVisibility(View.GONE);
        hideWaitDialog();
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            showSuccessDialog("已经充值成功");
            listener.print();
        } else {
            showSuccessDialog("充值没有成功");
        }


    }

    private void showSuccessDialog(String content) {
        new MaterialDialog.Builder(getContext())
                .title("提示")
                .content(content)
                .positiveText(R.string.bga_pp_confirm)
                .autoDismiss(true)
                .cancelable(false)
                .show();
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
