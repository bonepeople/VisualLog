package com.bonepeople.android.visuallog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

class LogView extends LinearLayout {
    private TextView textView_time;
    private TextView textView_content;

    LogView(Context context) {
        this(context, null);
    }

    LogView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    LogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    LogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        textView_time = new TextView(getContext());
        textView_time.setTextColor(0xA0000000);

        textView_content = new TextView(getContext());
        textView_content.setTextColor(0xFF000000);
        textView_content.setPadding(10, 10, 10, 20);

        addView(textView_time);
        addView(textView_content);
    }

    void updateView(LogInfo logInfo) {
        textView_time.setText(logInfo.getTimeFormat());
        textView_content.setText(logInfo.getContent());
    }
}
