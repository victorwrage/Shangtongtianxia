package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.ActorLevel;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 角色申请
 */
public class FragmentActor extends BaseFragment implements IMemberView, IUserView,IPayView {
    private final int QRCODE_FINISH = 111;
    @Bind(R.id.header_btn)
    ImageView header_btn;
    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.actor_success_tip_tv)
    TextView actor_success_tip_tv;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    protected ArrayAdapter<String> actorAdapter;
    protected ArrayAdapter<String> payAdapter;
    protected ArrayAdapter<String> tunerAdapter;
    @Bind(R.id.actor_location_lay)
    LinearLayout actor_location_lay;
    @Bind(R.id.actor_code_lay)
    LinearLayout actor_code_lay;

    @Bind(R.id.actor_work_lay)
    LinearLayout actor_work_lay;
    @Bind(R.id.actor_step2_lay)
    LinearLayout actor_step2_lay;
    @Bind(R.id.actor_step3_lay)
    LinearLayout actor_step3_lay;

    @Bind(R.id.actor_name_et)
    EditText actor_name_et;
    @Bind(R.id.actor_phone_et)
    EditText actor_phone_et;
    @Bind(R.id.actor_firm_et)
    EditText actor_firm_et;
    @Bind(R.id.actor_firm_add_et)
    EditText actor_firm_add_et;
    @Bind(R.id.actor_code_et)
    EditText actor_code_et;
    @Bind(R.id.actor_cash_et)
    EditText actor_cash_et;
    @Bind(R.id.actor_invite_et)
    EditText actor_invite_et;
    @Bind(R.id.actor_firm_type_et)
    EditText actor_firm_type_et;

    @Bind(R.id.actor_actor_sp)
    Spinner actor_actor_sp;
    @Bind(R.id.actor_pay_type_sp)
    Spinner actor_pay_type_sp;
    @Bind(R.id.actor_tuner_type_sp)
    Spinner actor_tuner_type_sp;
    @Bind(R.id.actor_code_tv)
    TextView actor_code_tv;

    @Bind(R.id.actor_submit_btn)
    Button actor_submit_btn;
    @Bind(R.id.actor_back_btn)
    Button actor_back_btn;
    @Bind(R.id.actor_pay_btn)
    Button actor_pay_btn;

    @Bind(R.id.actor_qcode_iv)
    ImageView actor_qcode_iv;
    @Bind(R.id.actor_save_tv)
    TextView actor_save_tv;

    Bitmap qrCodeBitmap;
    ArrayList<String> actor_types;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    ArrayList<ActorLevel> actorLevels;
    ActorLevel firmInfo;
    private Disposable disposable;
    private String[] pay_type = new String[]{"微信", "支付宝", "QQ钱包"};
    private String[] tuner_type = new String[]{"T1"};
    private String verrify;
    private String qcode;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QRCODE_FINISH:
                    KLog.v("handler");
                    hideWaitDialog();
                    actor_qcode_iv.setImageBitmap(qrCodeBitmap);
                    VToast.toast(getContext(), "申请成功");
                    actor_step2_lay.setVisibility(View.GONE);
                    actor_step3_lay.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor, container, false);
        ButterKnife.bind(FragmentActor.this, view);
        return view;
    }

    @Override
    public void refreshState() {
        super.refreshState();
        actor_work_lay.setVisibility(View.VISIBLE);
        actor_step2_lay.setVisibility(View.GONE);
        actor_step3_lay.setVisibility(View.GONE);

        actor_name_et.setText("");
        actor_phone_et.setText("");
        actor_firm_et.setText("");
        actor_firm_add_et.setText("");
        actor_code_et.setText("");
        actor_cash_et.setText("");
        actor_actor_sp.setSelection(0);
        actor_pay_type_sp.setSelection(0);
        actor_tuner_type_sp.setSelection(0);
        actor_code_et.setEnabled(true);
        header_title.setText("角色申请");
        actor_phone_et.setText(Constant.user_info.optString("tel"));
        actor_firm_add_et.setText(Constant.location == null ? "" : Constant.location.getAddrStr());
        fetchFromNetWork();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(actor_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit());
        RxView.clicks(actor_pay_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Pay());
        RxView.clicks(actor_back_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> {
            hidSoftInput();
            listener.gotoUserManager();
        });
        RxView.clicks(actor_location_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Location());
        RxView.clicks(actor_code_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Code());
        RxView.clicks(actor_save_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> SaveImage());

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("角色申请");
        actor_phone_et.setText(Constant.user_info.optString("tel"));
        actorAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, actor_types);
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor_actor_sp.setAdapter(actorAdapter);
        actor_actor_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actor_actor_sp.setSelection(0);
        payAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pay_type);
        payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor_pay_type_sp.setAdapter(payAdapter);
        actor_pay_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actor_pay_type_sp.setSelection(0);

        tunerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tuner_type);
        tunerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor_tuner_type_sp.setAdapter(tunerAdapter);
        actor_tuner_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actor_tuner_type_sp.setSelection(0);

        actor_firm_add_et.setText(Constant.location == null ? "" : Constant.location.getAddrStr());
        actor_code_et.setEnabled(true);

        fetchFromNetWork();
    }

    private void SaveImage() {
        util.saveImageToGallery(getContext(), qrCodeBitmap);
        VToast.toast(getContext(), "二维码已经保存");
    }


    private void initDate() {
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentActor.this);
        actor_types = new ArrayList<>();

    }

    private void Location() {
        actor_firm_add_et.setText(Constant.location == null ? "" : Constant.location.getAddrStr());
    }

    private void fetchFromNetWork() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryActorMember(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));
        present.QueryActorFirm(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));
    }

    private void Login() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryLogin(util.UrlEnco(Constant.WDT_SECRET), sp.getString("user_name", ""),
                util.getMD5(sp.getString("user_pw", "")));
    }

    @Override
    public void Back() {
        super.Back();
        if (actor_step3_lay.getVisibility() == View.VISIBLE) {
            SearchOrder();
            return;
        }
        if (actor_step2_lay.getVisibility() == View.VISIBLE) {
            actor_work_lay.setVisibility(View.VISIBLE);
            actor_step2_lay.setVisibility(View.GONE);
            return;
        }
        actor_types.clear();
        actorAdapter.notifyDataSetChanged();
        listener.gotoUserManager();
    }

    private void Pay() {
        if (actor_cash_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入金额");
            return;
        }
        showPopupWindow2(R.layout.fragment_actor,pay_type[actor_pay_type_sp.getSelectedItemPosition()],actor_cash_et.getText().toString());

    }

    /**
     * 查询充值结果
     */
    private void SearchOrder(){
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA,false);
        present.QueryMemberPayDetail(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                Constant.temp_info.get(Constant.ORDER_ID));
    }

    private void Submit() {
        if (actor_name_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入姓名");
            return;
        }
        if (!util.verifyPhone(actor_phone_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确的手机");
            return;
        }
        if (actor_code_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入手机验证码");
            return;
        }
        if (actor_invite_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入推荐码");
            return;
        }
        if (actor_firm_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入公司名称");
            return;
        }
        if (actor_firm_add_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入公司地址");
            return;
        }
        actor_cash_et.setText(actorLevels.get(actor_actor_sp.getSelectedItemPosition()).getL_integral());
        actor_work_lay.setVisibility(View.GONE);
        actor_step2_lay.setVisibility(View.VISIBLE);
    }


    private void Code() {
        if (!util.verifyPhone(actor_phone_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确的手机");
            return;
        }
        showWaitDialog("请稍等");

        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryCode(util.UrlEnco(Constant.WDT_SECRET), null,actor_phone_et.getText().toString().trim());
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
            actor_types.clear();
            actorAdapter.notifyDataSetChanged();
            listener.gotoUserManager();
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
        hideWaitDialog();
        actor_code_lay.setEnabled(true);
        actor_code_tv.setText("获取验证码");

        if (info.source() == null) {

            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(info.string());

            KLog.v(info.string());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }

        VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            actor_code_et.setEnabled(true);
            actor_code_lay.setEnabled(false);

            VToast.toast(getContext(), "验证码已发送至您的手机");
            try {
                verrify = new JSONObject(jsonObject.optString("content")).optString("vertify");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            timer();
            KLog.v("verrify=" + verrify);
        }

    }

    /**
     * 计时器
     **/
    private void timer() {
        int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        Disposable temp_dis;
        temp_dis = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete())
                .subscribe(s -> next(s));

        if (disposable != null) {
            disposable.dispose();
        }
        disposable = temp_dis;
    }

    private void complete() {
        actor_code_lay.setEnabled(true);
        actor_code_tv.setText("重新获取");
    }

    private void next(int s) {
        if (s >= 10) {
            actor_code_tv.setText( s +"");
        } else {
            actor_code_tv.setText("0" + s );
        }
    }

    @Override
    public void ResolveActorMember(ResponseBody info) {
        hideWaitDialog();
        if (info.source() == null) {
            showDialog(DIALOG_TYPE_1, "提示", "获取角色列表失败，是否重试？");
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            info.close();
            KLog.v(res);
            jsonObject = new JSONObject(res);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Back();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if (!jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            new MaterialDialog.Builder(getContext())
                    .title("您还不能申请角色")
                    .content(jsonObject.optString(Constant.ERRMSG))
                    .positiveText(R.string.bga_pp_confirm)
                    .autoDismiss(true)
                    .dismissListener(dialogInterface -> Back())
                    .onPositive((materialDialog, dialogAction) -> {
                        Back();
                    })
                    .show();

        }else{
            if(jsonObject.optString("content")!=null) {
                actorLevels = new ArrayList<>();
                actorLevels.addAll(JSON.parseArray(jsonObject.optString("content"), ActorLevel.class));
                for (ActorLevel actorLevel : actorLevels) {
                    actor_types.add(actorLevel.getLevel_name());
                }
                actorAdapter.notifyDataSetChanged();
            }else{
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("您还不能申请角色")
                        .positiveText(R.string.bga_pp_confirm)
                        .cancelable(false)
                        .dismissListener(dialogInterface -> Back())
                        .autoDismiss(true)
                        .onPositive((materialDialog, dialogAction) -> {
                            Back();
                        })
                        .show();
            }
        }
    }

    @Override
    public void ResolveApplyActorMember(ResponseBody info) {
        hideWaitDialog();
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
            Constant.temp_info.put(Constant.ORDER_ID,jsobj.optString("order_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if (!jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }else{
            showWaitDialog("正在生成二维码");
            executor.execute(() -> syncEncodeQRCode());
        }
    }

    @Override
    public void ResolveMemberSign(ResponseBody info) {

    }

    @Override
    public void ResolveMessage(ResponseBody info) {

    }

    private void syncEncodeQRCode() {
        String  bottom = pay_type[actor_pay_type_sp.getSelectedItemPosition()]+"支付" + actor_cash_et.getText().toString().trim();
        Constant.PAY_TOTAL = actor_cash_et.getText().toString().trim();
        KLog.v("syncEncodeQRCode0");
        Bitmap temp_bitmap = QRCodeEncoder.syncEncodeQRCode(qcode, 350,  Color.BLACK , BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        Bitmap topBmp = util.getImage(temp_bitmap.getWidth(),30,"商通天下付款二维码",30, getResources().getColor(R.color.shangtongtianx_btn_back));
        Bitmap bottomBmp = util.getImage(temp_bitmap.getWidth(),36,bottom ,36, getResources().getColor(R.color.shangtongtianx_txt));

        qrCodeBitmap = util.addTopBmp(topBmp ,bottomBmp,temp_bitmap);
        KLog.v("syncEncodeQRCode1");
        handler.sendEmptyMessage(QRCODE_FINISH);

    }

    @Override
    public void ResolveActorFirm(ResponseBody info) {
        hideWaitDialog();
        if (info.source() == null) {
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("获取公司机构失败，是否重试？")
                    .positiveText(R.string.retry)
                    .autoDismiss(true)
                    .dismissListener(dialogInterface -> Back())
                    .onPositive((materialDialog, dialogAction) -> {
                        fetchFromNetWork();
                    })
                    .show();
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            info.close();
            KLog.v(res);
            jsonObject = new JSONObject(res);

            firmInfo = JSON.parseArray(jsonObject.optString("content"), ActorLevel.class).get(0);
            actor_firm_et.setText(firmInfo.getName());
            switch(Integer.parseInt(firmInfo.getCompany_type())){
                case 0:
                    actor_firm_type_et.setText("直营");
                    break;
                case 1:
                    actor_firm_type_et.setText("加盟");
                    break;
                case 2:
                    actor_firm_type_et.setText("分销");
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            return;
        }
        if (!jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
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
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            showSuccessDialog("已经充值成功");
            listener.print();
        }else{
            showSuccessDialog("某种原因，充值没有成功");
        }
        actor_step2_lay.setVisibility(View.VISIBLE);
        actor_step3_lay.setVisibility(View.GONE);
        hideWaitDialog();
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



    @Override
    protected void cancel(int type) {
        super.cancel(type);
        switch (type) {
            case DIALOG_TYPE_1:
                Back();
                break;
            case DIALOG_TYPE_2:
                break;
            case DIALOG_TYPE_3:
                break;
        }
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
            FetchQcode();
        }else{
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
    }

    private void FetchQcode() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryApplyActorMember(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code")
                , actorLevels.get(actor_actor_sp.getSelectedItemPosition()).getId(), firmInfo.getId(),
                actor_pay_type_sp.getSelectedItemPosition()+"",
                "1",actor_invite_et.getText().toString().trim());
           //     actor_tuner_type_sp.getSelectedItemPosition()+"",actor_invite_et.getText().toString().trim());
    }

    @Override
    public void ResolveResumeRecord(ResponseBody info) {

    }
}
