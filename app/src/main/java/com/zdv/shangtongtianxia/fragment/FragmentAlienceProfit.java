package com.zdv.shangtongtianxia.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
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
import com.zdv.shangtongtianxia.STTXApplication;
import com.zdv.shangtongtianxia.adapter.TerminalProfitItemAdapter;
import com.zdv.shangtongtianxia.bean.DaoSession;
import com.zdv.shangtongtianxia.bean.SearchHistoryBean;
import com.zdv.shangtongtianxia.bean.SearchHistoryBeanDao;
import com.zdv.shangtongtianxia.bean.TerminalProfitBean;
import com.zdv.shangtongtianxia.customView.RecyclerViewWithEmpty;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.util.search.LogQuickSearchData.SearchHistoryBeanAdapter;
import com.zdv.shangtongtianxia.util.search.Utilities.InitiateSearch;
import com.zdv.shangtongtianxia.view.IView;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

/**
 * 商盟分润
 */
public class FragmentAlienceProfit extends BaseFragment implements IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    ArrayList<TerminalProfitBean> a_data;

    @Bind(R.id.terminal_data_list)
    RecyclerViewWithEmpty terminal_data_list;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_right_tv)
    TextView header_right_tv;
    @Bind(R.id.header_search_tv)
    TextView header_search_tv;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;


    TerminalProfitItemAdapter a_adapter;

    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    private PopupWindow popupWindow;
    View popupWindowView;

    private static final int SEARCH_TYPE = 1;
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
        View view = inflater.inflate(R.layout.fragment_terminal_profit, container, false);
        ButterKnife.bind(FragmentAlienceProfit.this, view);

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
        RxView.clicks(header_search_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Search());
        header_right_tv.setText("查询");
        header_title.setText("商联商家收益");
        header_edit_lay.setVisibility(View.VISIBLE);

        a_adapter = new TerminalProfitItemAdapter(a_data,getContext());
        terminal_data_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(a_adapter, terminal_data_list);
        terminal_data_list.setEmptyView(empty_lay);
        terminal_data_list.setAdapter(animatorApdapter);

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
            popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("我的收益");

            ArrayAdapter<String> menu_adapter;
            menu_adapter =  new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowView.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                popupWindow.dismiss();
            });
        }
        backgroundAlpha(0.5f);
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
        if(linearLayout_search.getVisibility()==View.VISIBLE){
            header_search_tv.setText("请输入团队关键字");
            initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
        }
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
        a_data.add(new TerminalProfitBean());
        a_data.add(new TerminalProfitBean());
        a_data.add(new TerminalProfitBean());
        a_adapter.notifyDataSetChanged();
    }

    private void initDate() {
        DaoSession daoSession = (STTXApplication.getDaoSession(getContext()));
        searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();


        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentAlienceProfit.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        a_data = new ArrayList<>();
        a_data.add(new TerminalProfitBean());
        a_data.add(new TerminalProfitBean());
        a_data.add(new TerminalProfitBean());

    }

    /*** ---------------Search------------------------**/
    private void Search() {
        if (initiateSearch == null) {
            showWaitDialog("请稍等");
            executor.execute(() -> {
                initiateSearch = new InitiateSearch();
                InitiateSearch();
                HandleSearch();
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
                listView.setAdapter(logQuickSearchAdapter);
                set = new HashSet<>();
                header_btn_lay.post(() -> {
                    hideWaitDialog();
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
                header_search_tv.setText("请输入商家关键字");
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
        image_search_back.setOnClickListener(v -> {
            header_search_tv.setText("请输入商家关键字");
            initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
        });
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
        header_search_tv.setText(key);
        doSearch();
        initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
    }

    private void doSearch() {
        VToast.toast(getContext(),"没有搜索到结果");
    }

    /*** ---------------Search------------------------**/



}
