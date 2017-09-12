package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.STTXApplication;
import com.zdv.shangtongtianxia.bean.DaoSession;
import com.zdv.shangtongtianxia.bean.SearchHistoryBean;
import com.zdv.shangtongtianxia.bean.SearchHistoryBeanDao;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.util.search.LogQuickSearchData.SearchHistoryBeanAdapter;
import com.zdv.shangtongtianxia.util.search.Utilities.InitiateSearch;
import com.zdv.shangtongtianxia.view.IView;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

public class FragmentResearch extends BaseFragment implements IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.research_catering_lay)
    LinearLayout research_catering_lay;
    @Bind(R.id.research_physical_lay)
    LinearLayout research_physical_lay;
    @Bind(R.id.research_cosmetics_lay)
    LinearLayout research_cosmetics_lay;
    @Bind(R.id.research_clothing_lay)
    LinearLayout research_clothing_lay;
    @Bind(R.id.research_goods_lay)
    LinearLayout research_goods_lay;
    @Bind(R.id.research_entertainment_lay)
    LinearLayout research_entertainment_lay;
    @Bind(R.id.research_phone_lay)
    LinearLayout research_phone_lay;
    @Bind(R.id.research_technology_lay)
    LinearLayout research_technology_lay;
    @Bind(R.id.research_electrical_lay)
    LinearLayout research_electrical_lay;
    @Bind(R.id.research_other_lay)
    LinearLayout research_other_lay;

    @Bind(R.id.research_picker_lay)
    RelativeLayout research_picker_lay;
    @Bind(R.id.research_date_tv)
    TextView research_date_tv;


    private static final int SEARCH_TYPE = 2;
    @Bind(R.id.edit_text_search)
    EditText edit_text_search;
    @Bind(R.id.image_search_back)
    ImageView image_search_back;
    @Bind(R.id.clearSearch)
    ImageView clearSearch;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.linearLayout_search)
    LinearLayout linearLayout_search;
    private InitiateSearch initiateSearch;
    private SearchHistoryBeanAdapter logQuickSearchAdapter;
    private Set<String> set;
    SearchHistoryBeanDao searchHistoryBeanDao;

    @Bind(R.id.header_search_et)
    TextView header_search_et;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_setting_lay)
    LinearLayout header_setting_lay;
    @Bind(R.id.header_setting_iv)
    ImageView header_setting_iv;
    @Bind(R.id.header_left_iv)
    ImageView header_left_iv;
    @Bind(R.id.header_btn)
    ImageView header_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_research, container, false);
        ButterKnife.bind(FragmentResearch.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(research_picker_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ShowDatePicker());
        RxView.clicks(header_setting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Search());
        RxView.clicks(header_search_et).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Search());


        header_search_et.setVisibility(View.VISIBLE);
        header_setting_lay.setVisibility(View.VISIBLE);
        header_setting_iv.setImageResource(R.drawable.map_research);
    }

    @Override
    public void refreshState() {
        super.refreshState();
    }

    public void Back() {
        super.Back();
        edit_text_search.setHint("请输入关键字");
        header_search_et.setHint("请输入关键字");
        edit_text_search.setText("");
        header_search_et.setText("");
        if(linearLayout_search.getVisibility()==View.VISIBLE){
            initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
        }
        listener.gotoWealth();
    }

    /**
     * 弹出日期选择框
     */
    private void ShowDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setGravity(Gravity.BOTTOM);//弹框居中
        picker.setRangeStart(calendar.get(Calendar.YEAR)-1, 1, 1);
        picker.setRangeEnd(calendar.get(Calendar.YEAR), 12, 31);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) ->
                research_date_tv.setText(year + "-" + month + "-" + day));
        picker.show();
    }


    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentResearch.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        DaoSession daoSession = STTXApplication.getDaoSession(getContext());
        searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();

    }

    /*** ---------------Search------------------------**/
    private void Search() {
        if (initiateSearch == null) {
            showWaitDialog("请稍等");
            initiateSearch = new InitiateSearch();
            InitiateSearch();
            HandleSearch();
            executor.execute(() -> {

                WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(SEARCH_TYPE);
                Set<SearchHistoryBean> delete = new HashSet<>(searchHistoryBeanDao.queryBuilder().where(wc1).
                        orderAsc(SearchHistoryBeanDao.Properties.Createtime).list());
                KLog.v("initDate--" + delete.size());
                int ida = delete.size() > 10 ? delete.size() - 10 : 0;
                for (SearchHistoryBean d : delete) {
                    if (ida == 0) {
                        break;
                    } else {
                        ida--;
                        searchHistoryBeanDao.delete(d);
                    }
                }
                List<SearchHistoryBean> all = searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                KLog.v("initDate-----" + all.size());
                logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);

                set = new HashSet<>();
                header_btn_lay.post(() -> {
                    hideWaitDialog();
                    listView.setAdapter(logQuickSearchAdapter);
                    initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
                    edit_text_search.setText("");
                    listView.setVisibility(View.VISIBLE);
                });
            });
            return;
        }
        initiateSearch.handleToolBar(getActivity(), linearLayout_search,listView, edit_text_search);
        edit_text_search.setText("");
        listView.setVisibility(View.VISIBLE);
    }

    private void InitiateSearch() {

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            SearchHistoryBean logQuickSearch = logQuickSearchAdapter.getItem(position);
            edit_text_search.setText(logQuickSearch.getKey_word());
            listView.setVisibility(View.GONE);
            hidSoftInput();
            searchInfo(logQuickSearch.getKey_word());
        });
        edit_text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_text_search.getText().toString().length() == 0) {
                    WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(SEARCH_TYPE);
                    List<SearchHistoryBean> all = searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                    logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setVisibility(View.GONE);

                } else {
                    WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(SEARCH_TYPE);
                    List<SearchHistoryBean> all = searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                    logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setVisibility(View.VISIBLE);
                    clearSearch.setImageResource(R.drawable.cancel);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(v -> {
            if (edit_text_search.getText().toString().length() == 0) {

            } else {
                header_search_et.setText("请输入消费关键字");
                edit_text_search.setText("");
                listView.setVisibility(View.VISIBLE);
                hidSoftInput();
            }
        });
    }

    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < logQuickSearchAdapter.getCount(); i++) {
            SearchHistoryBean ls = logQuickSearchAdapter.getItem(i);
            String name = ls.getKey_word();
            set.add(name);
        }
        if(set ==null)set=new HashSet<>();
        if (set.add(item.toUpperCase())) {
            SearchHistoryBean recentLog = new SearchHistoryBean();
            recentLog.setKey_word(item);
            recentLog.setIdx(logQuickSearchAdapter.getCount() + 1);
            recentLog.setType(SEARCH_TYPE);

            recentLog.setCreatetime(System.currentTimeMillis());
            searchHistoryBeanDao.insert(recentLog);
            logQuickSearchAdapter.add(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(v -> initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search));
        edit_text_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (edit_text_search.getText().toString().trim().length() > 0) {

                    hidSoftInput();
                    UpdateQuickSearch(edit_text_search.getText().toString());
                    listView.setVisibility(View.GONE);
                    searchInfo(edit_text_search.getText().toString());

                }
                return true;
            }
            return false;
        });
    }

    private void searchInfo(final String key) {
        header_search_et.setText(key);
        doSearch();
        initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
    }

    private void doSearch() {
        VToast.toast(getContext(), "没有搜索到结果");
    }

    /*** ---------------Search------------------------**/

}
