package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentRecharge extends BaseFragment implements IView{

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.recharge_content_lay)
    ScrollView recharge_content_lay;
    @Bind(R.id.recharge_record_lay)
    LinearLayout recharge_record_lay;
    @Bind(R.id.recharge_no_record_iv)
    ImageView recharge_no_record_iv;
    @Bind(R.id.recharge_submit_btn)
    TextView recharge_submit_btn;

    @Bind(R.id.recharge_date_tv)
    TextView recharge_date_tv;
    @Bind(R.id.recharge_name_tv)
    TextView recharge_name_tv;
    @Bind(R.id.recharge_cash_tv)
    TextView recharge_cash_tv;
    @Bind(R.id.recharge_total_tv)
    TextView recharge_total_tv;
    @Bind(R.id.recharge_balance_tv)
    TextView recharge_balance_tv;
    @Bind(R.id.recharge_coin_tv)
    TextView recharge_coin_tv;
    @Bind(R.id.header_search_tv)
    TextView header_search_tv;

    private static final int SEARCH_TYPE = 3;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        ButterKnife.bind(FragmentRecharge.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Back());
        RxView.clicks(header_search_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Search());
        header_title.setText("商家充值");

    }

    @Override
    public void refreshState() {
        super.refreshState();
    }

    public void Back() {
        super.Back();
        if(linearLayout_search.getVisibility()==View.VISIBLE){
            header_search_tv.setHint("请输入商家关键字");
            header_search_tv.setText("");
            initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
        }
        listener.gotoWealth();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentRecharge.this);
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
                KLog.v("initDate--"+delete.size());
                int ida = delete.size()>10? delete.size()-10:0;
                for(SearchHistoryBean d:delete){
                    if(ida==0) {
                        break;
                    }else {
                        ida --;
                        searchHistoryBeanDao.delete(d);
                    }

                }
                List<SearchHistoryBean> all =  searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                KLog.v("initDate-----"+all.size());
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
                    List<SearchHistoryBean> all =  searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                    logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setVisibility(View.GONE);

                } else {
                    WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(SEARCH_TYPE);
                    List<SearchHistoryBean> all =  searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
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
            recentLog.setIdx(logQuickSearchAdapter.getCount()+1);
            recentLog.setType(SEARCH_TYPE);

            recentLog.setCreatetime(System.currentTimeMillis());
            searchHistoryBeanDao.insert(recentLog);
            logQuickSearchAdapter.add(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
            }
        });
        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
            }
        });
    }

    private void searchInfo(final String key) {
        doSearch();
        initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
    }

    private void doSearch() {
        VToast.toast(getContext(),"没有搜索到结果");
    }

    /*** ---------------Search------------------------**/

}
