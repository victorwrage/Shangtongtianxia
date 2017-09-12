package com.zdv.shangtongtianxia.util.search.LogQuickSearchData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.zdv.shangtongtianxia.bean.SearchHistoryBean;

import java.util.List;

public class SearchHistoryBeanAdapter extends ArrayAdapter<SearchHistoryBean> {
    Context mContext;
    public static List<SearchHistoryBean> mLogs;

    public SearchHistoryBeanAdapter(Context context, int textViewResourceId, List<SearchHistoryBean> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<SearchHistoryBean> logs) {
        mLogs = logs;
    }

    public List<SearchHistoryBean> getLogs() {
        return mLogs;
    }

    public void add(SearchHistoryBean log) {
        mLogs.add(log);
    }

    public void remove(SearchHistoryBean log) {
        SearchHistoryBeanAdapter.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public SearchHistoryBean getItem(int position) {
        return mLogs.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LogQuickSearchRow view = (LogQuickSearchRow) convertView;
        if (view == null) {
            view = new LogQuickSearchRow(mContext);
        }
        SearchHistoryBean log = getItem(position);
        view.setLog(log);
        return view;
    }
}
