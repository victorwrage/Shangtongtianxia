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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.adapter.PosProfitItemAdapter;
import com.zdv.shangtongtianxia.bean.PosProfitBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.view.IView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class FragmentPosProfit extends BaseFragment implements IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    ArrayList<PosProfitBean> a_data;

    @Bind(R.id.pos_data_list)
    RecyclerViewWithEmpty pos_data_list;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_right_tv)
    TextView header_right_tv;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;
    @Bind(R.id.pos_date_to_tv)
    TextView pos_date_to_tv;

    PosProfitItemAdapter a_adapter;

    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    private PopupWindow popupWindow;
    View popupWindowView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pos_profit, container, false);
        ButterKnife.bind(FragmentPosProfit.this, view);

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
        RxView.clicks(header_edit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  ShowPopupWindow(header_edit_lay));
        header_right_tv.setText("筛选");
        header_title.setText("POS机收益");
        header_edit_lay.setVisibility(View.VISIBLE);
        a_adapter = new PosProfitItemAdapter(a_data,getContext());
        pos_data_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(a_adapter, pos_data_list);
        pos_data_list.setEmptyView(empty_lay);
        pos_data_list.setAdapter(animatorApdapter);
        pos_date_to_tv.setText("至"+util.currentDate("yyyy-MM-dd"));
        setEmptyStatus(false);
        a_adapter.notifyDataSetChanged();
    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
           // popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("我的直销收益");

            ArrayAdapter<String> menu_adapter;
            menu_adapter =  new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowView.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                popupWindow.dismiss();
            });
        }
        //backgroundAlpha(0.5f);
        popupWindow.showAtLocation(view,
                Gravity.TOP | Gravity.RIGHT, 0,120);

    }



    @Override
    public void refreshState() {
        if(a_data!=null) a_data.clear();
        a_adapter.notifyDataSetChanged();
        fetchFromNetWork();
        super.refreshState();

    }

    public void Back() {
        super.Back();
        listener.gotoProfit();
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
            empty_tv.setText("没有记录");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {
      //  a_data.add(new PosProfitBean());
     //   a_data.add(new PosProfitBean());
       // a_data.add(new PosProfitBean());
        a_adapter.notifyDataSetChanged();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentPosProfit.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        a_data = new ArrayList<>();
      //  a_data.add(new PosProfitBean());
      //  a_data.add(new PosProfitBean());
      //  a_data.add(new PosProfitBean());

    }

}
