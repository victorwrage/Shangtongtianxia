package com.zdv.shangtongtianxia.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.CouponItemAdapter;
import com.zdv.shangtongtianxia.bean.ConponsBean;
import com.zdv.shangtongtianxia.bean.CouponBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.MD5Utils;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.util.TimeUtils;
import com.zdv.shangtongtianxia.view.IMemberView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import okhttp3.ResponseBody;

@SuppressLint("ValidFragment")
public class CouponFragment extends BaseFragment implements IMemberView {
    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.data_list)
    RecyclerViewWithEmpty data_list;

    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;
    static ArrayList<ConponsBean.ContentBean> data;
    static ArrayList<ConponsBean.ContentBean> source_data;
    CouponItemAdapter adapter;
    public static int type;
    private Gson gson;

    public static CouponFragment getInstance(int type_) {
        CouponFragment sf = new CouponFragment();
        type = type_;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, null);
        ButterKnife.bind(CouponFragment.this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
        gson = new Gson();
        present = QueryPresent.getInstance(getContext());
        present.setView(CouponFragment.this);
        present.initRetrofit(Constant.URL_TONGDUI, false);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        data = new ArrayList<>();
        source_data = new ArrayList<>();
    }

    private void initView() {
        adapter = new CouponItemAdapter(data, getContext());
        data_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, data_list);
        data_list.setEmptyView(empty_lay);
        data_list.setAdapter(animatorApdapter);
        empty_lay.setVisibility(View.VISIBLE);
        data_list.setVisibility(View.GONE);

        // test();

        fetchFromNetWork();

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
            empty_tv.setText("没有优惠券");
        }
    }

    private CouponBean testItem(String status) {
        CouponBean memberBean = new CouponBean();
        memberBean.setName("万州烤鱼大锅 4人份 送小菜");
        memberBean.setType("鱼种：草鱼");
        memberBean.setAvailable("有效期2017-09至2017-12");
        memberBean.setCount(10);
        memberBean.setIcon(R.drawable.sample1);
        memberBean.setPrice("150.0元");
        memberBean.setCur_count(1);
        memberBean.setStatus(status);
        memberBean.setSelect(false);
        return memberBean;
    }

/*    private void test() {

        source_data.add(testItem("0"));
        source_data.add(testItem("0"));
        source_data.add(testItem("0"));
        source_data.add(testItem("1"));
        source_data.add(testItem("1"));

        SwitchTab(0);
        setEmptyStatus(false);
    }*/

    protected void emptyClick() {
        fetchFromNetWork();
    }

    /**
     * 获取卡劵
     */
    private void fetchFromNetWork() {
        String key = Constant.PUBLIC_TONGDUI_KEY;
        String time = TimeUtils.getDate2();
        String md5_key = MD5Utils.md5(key);
        String sign = MD5Utils.md5(key + md5_key + time);
        present.QueryMemCurpons(Constant.user_info.optString("code"), sign);
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
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            hideWaitDialog();
            setEmptyStatus(true);
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);
            ConponsBean bean = gson.fromJson(jsonObject.toString(), ConponsBean.class);
            int errCode = bean.getErrcode();
            String errMsg = bean.getErrmsg();
            if (errCode == 200) {
                source_data.clear();
                source_data.addAll(bean.getContent());
                SwitchTab(0);
            } else {
                VToast.toast(this.getActivity(), errMsg);
            }
            setEmptyStatus(false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getContext(), "网络超时");
            setEmptyStatus(true);
            return;
        }
    }

    @Override
    public void ResolveActorFirm(ResponseBody info) {

    }

    @Override
    public void ResolveMemberPayDetail(ResponseBody info) {

    }

    @Override
    public void ResolveVerifyMember(ResponseBody info) {

    }

    @Override
    public void ResolveTeamMember(ResponseBody info) {
        if (info.source() == null) {
            VToast.toast(getContext(), "网络错误，请重试!");
            hideWaitDialog();
            setEmptyStatus(true);
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
            setEmptyStatus(true);
            hideWaitDialog();
            return;
        }
        SwitchTab(0);
        setEmptyStatus(false);
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            source_data.clear();
            //      source_data.addAll(JSON.parseArray(jsonObject.optString("content"), CouponBean.class));
            SwitchTab(0);
        } else {
            //    VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveIsVerify(ResponseBody info) {

    }


    public void SwitchTab(int i) {
        switch (i) {
            case 0:
                data.clear();
                for (ConponsBean.ContentBean memberBean : source_data) {
                    if (TimeUtils.compareNowTime(memberBean.getStoptime()) == true) {
                        if (Integer.valueOf(memberBean.getNumber()) > 0 && Integer.valueOf(memberBean.getNumber()) > Integer.valueOf(memberBean.getUse_num())) {
                            memberBean.setStatus("0");
                            data.add(memberBean);
                        }
                    }
                }
                break;
            case 1:
                data.clear();
                for (ConponsBean.ContentBean memberBean : source_data) {
                    if (Integer.valueOf(memberBean.getUse_num()) > 0) {
                        memberBean.setStatus("1");
                        data.add(memberBean);
                    }
                }
                break;
            case 2:
                data.clear();
                for (ConponsBean.ContentBean memberBean : source_data) {
                    if (TimeUtils.compareNowTime(memberBean.getStoptime()) == false) {
                        memberBean.setStatus("2");
                        data.add(memberBean);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }
}
