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

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.TeamItemAdapter;
import com.zdv.shangtongtianxia.bean.MemberBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
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
public class TeamFragment extends BaseFragment implements IMemberView {
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
    static ArrayList<MemberBean> data;
    static ArrayList<MemberBean> source_data;
    TeamItemAdapter adapter;
    public static int type;

    public static TeamFragment getInstance(int type_) {
        TeamFragment sf = new TeamFragment();
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
        ButterKnife.bind(TeamFragment.this, v);
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
        present = QueryPresent.getInstance(getContext());
        present.setView(TeamFragment.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        data = new ArrayList<>();
        source_data = new ArrayList<>();
    }

    private void initView() {
        adapter = new TeamItemAdapter(data, getContext());
        data_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, data_list);
        data_list.setEmptyView(empty_lay);
        data_list.setAdapter(animatorApdapter);
        empty_lay.setVisibility(View.VISIBLE);
        data_list.setVisibility(View.GONE);
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
            empty_tv.setText("没有数据");
        }
    }

    protected void emptyClick() {
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryTeamMember(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"));
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
        setEmptyStatus(false);
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            source_data.clear();
            source_data.addAll(JSON.parseArray(jsonObject.optString("content"), MemberBean.class));
            SwitchTab(0);
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }

        hideWaitDialog();
    }

    @Override
    public void ResolveIsVerify(ResponseBody info) {

    }

    public void SwitchTab(int i) {
        type = i;
        switch (i) {
            case 0:
                data.clear();
                for (MemberBean memberBean : source_data) {
                    if (memberBean.getStatus().equals("1")) {
                        data.add(memberBean);
                    }
                }
                break;
            case 1:
                data.clear();
                for (MemberBean memberBean : source_data) {
                    if (memberBean.getStatus().equals("0")) {
                        data.add(memberBean);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void filter(int i) {
        switch (type) {
            case 0:
                data.clear();
                for (MemberBean memberBean : source_data) {
                    if (memberBean.getStatus().equals("1")) {
                        data.add(memberBean);
                    }
                }
                break;
            case 1:
                data.clear();
                for (MemberBean memberBean : source_data) {
                    if (memberBean.getStatus().equals("0")) {
                        data.add(memberBean);
                    }
                }
                break;
        }


        ArrayList<MemberBean> temp = new ArrayList<>();
        for (MemberBean memberBean : data) {
            if (memberBean.getLid() != null && memberBean.getLid().equals(i + "")) {
                temp.add(memberBean);
            }
        }
        data.clear();
        data.addAll(temp);
        adapter.notifyDataSetChanged();
    }
}
