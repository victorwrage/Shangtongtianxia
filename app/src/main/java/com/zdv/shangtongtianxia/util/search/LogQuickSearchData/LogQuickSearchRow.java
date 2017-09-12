package com.zdv.shangtongtianxia.util.search.LogQuickSearchData;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.bean.SearchHistoryBean;


public class LogQuickSearchRow extends LinearLayout {
    Context mContext;
    SearchHistoryBean mLog;

    public LogQuickSearchRow(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public LogQuickSearchRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.list_quick_search_row, this);
    }

    public void setLog(SearchHistoryBean log) {
        mLog = log;
        Typeface roboto_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
        TextView text = (TextView) findViewById(R.id.textView);
        text.setTypeface(roboto_regular);
        text.setText(mLog.getKey_word());

    }
}