package com.zdv.shangtongtianxia.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.STTXApplication;
import com.zdv.shangtongtianxia.bean.DaoSession;
import com.zdv.shangtongtianxia.bean.SearchHistoryBean;
import com.zdv.shangtongtianxia.bean.SearchHistoryBeanDao;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
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

public class FragmentMap extends BaseFragment implements IView, OnGetPoiSearchResultListener {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    TextureMapView bmapView;

    SupportMapFragment map;
    LatLng latLng;
    ArrayList<PoiInfo> nearList;
    ArrayList<Marker> markerListA;
    ArrayList<Marker> markerListB;
    @Bind(R.id.map_member_count_tv)
    TextView map_member_count_tv;
    @Bind(R.id.map_merchant_count_tv)
    TextView map_merchant_count_tv;
/*    @Bind(R.id.map_stub)
    ViewStub map_stub;*/

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


    private PopupWindow popupWindow;
    View popupWindowView;

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

    View view;
    private boolean isInit = false;
    private int maxDistance = 1000;
    private String key_word = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(FragmentMap.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }


    private void initView() {
        popupWindowView = View.inflate(getContext(), R.layout.pop_menu, null);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ShowPopupWindow(header_btn_lay));
        RxView.clicks(header_setting_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Search());
        RxView.clicks(header_search_et).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Search());


        header_search_et.setVisibility(View.VISIBLE);
        header_setting_lay.setVisibility(View.VISIBLE);
        header_left_iv.setVisibility(View.VISIBLE);
        header_btn.setVisibility(View.GONE);
        header_setting_iv.setImageResource(R.drawable.map_research);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInit) {

            isInit = true;
            MapStatus.Builder builder = new MapStatus.Builder();
            if (Constant.location != null) {
                latLng = new LatLng(Constant.location.getLatitude(), Constant.location.getLongitude());
                builder.target(latLng).zoom(16);
            }
            BaiduMapOptions bo = new BaiduMapOptions().mapStatus(builder.build())
                    .compassEnabled(true).zoomControlsEnabled(true);
            map = SupportMapFragment.newInstance(bo);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().add(R.id.map, map, "map_fragment").commitAllowingStateLoss();
            //   map = bmapView.getMap();
            executor.execute(() -> {
                if (Constant.location != null) {
                    key_word = "餐厅";
                    searchNearBy();
                }
            });
        }
    }

    private void searchNearBy() {
        header_search_et.setText(key_word);
        PoiSearch mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(key_word);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(latLng);
        option.radius(maxDistance);
        option.pageNum(1);
        option.pageCapacity(100);
        mPoiSearch.searchNearby(option);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        KLog.v("onGetPoiResult" + poiResult);

        OverlayOptions ooA0 = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                .zIndex(4).draggable(true);
        map.getBaiduMap().addOverlay(ooA0);
        if (poiResult != null) {
            if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                nearList.clear();
                markerListA.clear();
                markerListB.clear();
                nearList.addAll(poiResult.getAllPoi());
                map.getBaiduMap().clear();
                int i = 0;
                for (PoiInfo poiInfo : nearList) {
                    i++;
                    if (i % 3 == 0) {
                        OverlayOptions ooA = new MarkerOptions().position(poiInfo.location).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_merchant2))
                                .draggable(false);
                        markerListA.add((Marker) (map.getBaiduMap().addOverlay(ooA)));
                    } else {
                        OverlayOptions ooA = new MarkerOptions().position(poiInfo.location).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_merchant))
                                .draggable(false);
                        markerListB.add((Marker) (map.getBaiduMap().addOverlay(ooA)));
                    }
                }
                KLog.v("markerListB" + markerListB.size());
                map_merchant_count_tv.setText("商家数：" + markerListA.size() + "户");
                map_member_count_tv.setText("会员数：" + markerListB.size() + "人");

            } else {
                VToast.toast(getContext(), "没有相关商家");
            }
        } else {
            VToast.toast(getContext(), "没有相关商家");
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        KLog.v("onGetPoiDetailResult");
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        KLog.v("onGetPoiIndoorResult");
    }

    private void Search() {
        if (initiateSearch == null) {
            showWaitDialog("请稍等");
            initiateSearch = new InitiateSearch();
            InitiateSearch();
            HandleSearch();
            executor.execute(() -> {
                WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(0);
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
                logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);
                KLog.v("pos---t-");

                set = new HashSet<>();
                KLog.v("pos---t");
                header_btn_lay.post(() -> {
                    listView.setAdapter(logQuickSearchAdapter);
                    KLog.v("post");
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

    @Override
    public void refreshState() {
        super.refreshState();
    }

    public void Back() {
        super.Back();
        listener.gotoMain();
    }

    private void initDate() {

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentMap.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        DaoSession daoSession = STTXApplication.getDaoSession(getContext());
        searchHistoryBeanDao = daoSession.getSearchHistoryBeanDao();

        nearList = new ArrayList<>();
        markerListA = new ArrayList<>();
        markerListB = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.RECEIVE_LOCATION_SUCCESS);
        getContext().registerReceiver(positionReceive, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(positionReceive);
    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);

            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("1-----3公里");
            menu_data.add("3-----5公里");
            menu_data.add("5-----8公里");
            menu_data.add("10公里以上");
            ArrayAdapter<String> menu_adapter;
            menu_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowView.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);
            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                switch (i) {
                    case 0:
                        maxDistance = 3000;
                        break;
                    case 1:
                        maxDistance = 5000;
                        break;
                    case 2:
                        maxDistance = 8000;
                        break;
                    case 3:
                        maxDistance = 10000;
                        break;
                }
                searchNearBy();
                popupWindow.dismiss();
            });
        }

        popupWindow.showAtLocation(view,
                Gravity.TOP | Gravity.LEFT, 0, 120);

    }

    /*** ---------------Search------------------------**/

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
                    WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(0);
                    List<SearchHistoryBean> all = searchHistoryBeanDao.queryBuilder().where(wc1).limit(10).list();
                    logQuickSearchAdapter = new SearchHistoryBeanAdapter(getContext(), 0, all);
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setVisibility(View.GONE);

                } else {
                    WhereCondition wc1 = SearchHistoryBeanDao.Properties.Type.eq(0);
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
                header_search_et.setText("请输入具体地址");
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

        if (set == null) set = new HashSet<>();
        if (set.add(item.toUpperCase())) {
            SearchHistoryBean recentLog = new SearchHistoryBean();
            recentLog.setKey_word(item);
            recentLog.setIdx(logQuickSearchAdapter.getCount() + 1);
            recentLog.setType(0);

            recentLog.setCreatetime(System.currentTimeMillis());
            searchHistoryBeanDao.insert(recentLog);
            logQuickSearchAdapter.add(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(v -> {
            header_search_et.setText("请输入具体地址");
            initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
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
        key_word = key;
        searchNearBy();
        initiateSearch.handleToolBar(getActivity(), linearLayout_search, listView, edit_text_search);
    }


    /*** ---------------Search------------------------**/


    BroadcastReceiver positionReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.RECEIVE_LOCATION_SUCCESS)) {
                MapStatusUpdate mapState = MapStatusUpdateFactory.newLatLng(new LatLng(Constant.location.getLatitude(), Constant.location.getLongitude()));
                map.getBaiduMap().animateMapStatus(mapState);
            }
        }
    };
}
