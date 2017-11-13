package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
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

/**
 * 我的订单
 */
public class FragmentOrder extends BaseFragment implements IView,OnTabSelectListener {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.tl_1)
    SlidingTabLayout tl_1;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_setting_iv)
    ImageView header_setting_iv;

    private ArrayList<OrderFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"待接收"
            , "待发送", "已接收", "已完成", "已取消"
    };
    private MyPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(FragmentOrder.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        header_title.setText("我的订单");
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());


        for (int c =0;c<mTitles.length;c++) {
            mFragments.add(OrderFragment.getInstance(c));
        }
        mAdapter = new MyPagerAdapter(getFragmentManager());
        vp.setAdapter(mAdapter);
        tl_1.setViewPager(vp);
        tl_1.setOnTabSelectListener(this);
        vp.setCurrentItem(3);

    }

    @Override
    public void refreshState() {
        fetchFromNetWork();
        super.refreshState();
    }

    public void Back() {
        super.Back();
        listener.gotoUserManager();
    }

    private void fetchFromNetWork() {

      //  a_adapter.notifyDataSetChanged();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentOrder.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
