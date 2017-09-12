package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.view.IView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentTeam extends BaseFragment  implements IView ,OnTabSelectListener{

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_setting_iv)
    ImageView header_setting_iv;
    @Bind(R.id.header_setting_lay)
    LinearLayout header_setting_lay;


    private PopupWindow popupWindow;
    View popupWindowView;

    @Bind(R.id.fl_change)
    FrameLayout fl_change;
    @Bind(R.id.tl_1)
    SegmentTabLayout tl_1;
    TeamFragment teamFragment;
    private final String[] mTitles = {"有效会员"
            , "无效会员" };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        ButterKnife.bind(FragmentTeam.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        popupWindowView = View.inflate(getContext(),R.layout.pop_menu, null);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Back());
        RxView.clicks(header_setting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  ShowPopupWindow(header_setting_lay));

        header_title.setText("我的团队");
        header_setting_lay.setVisibility(View.VISIBLE);
        header_setting_iv.setImageResource(R.drawable.team_icon1);

        tl_1.setTabData(mTitles);
        tl_1.setOnTabSelectListener(this);
        teamFragment = TeamFragment.getInstance(0);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_change,teamFragment,"TEAM_FRAGMENT");
        fragmentTransaction.commit();

    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
         //   popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("零级会员");
            menu_data.add("一级会员");
            menu_data.add("二级会员");
            menu_data.add("三级会员");
            menu_data.add("四级会员");
            menu_data.add("五级会员");
            ArrayAdapter<String> menu_adapter;
            menu_adapter =  new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowView.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
               // teamFragment.SwitchTab(i);
                teamFragment.filter(i);
                popupWindow.dismiss();
            });
        }
        popupWindow.showAtLocation(view,
                Gravity.TOP | Gravity.RIGHT, 0,120);
    }


    @Override
    public void refreshState() {
        super.refreshState();
    }

    public void Back() {
        super.Back();
        listener.gotoUserManager();
    }


    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentTeam.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

    }


    @Override
    public void onTabSelect(int i) {
        teamFragment.SwitchTab(i);

    }

    @Override
    public void onTabReselect(int i){
    }


}
