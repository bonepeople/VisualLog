package com.bonepeople.android.visuallog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class LogInfo {
    private long time;
    private String timeFormat;
    private CharSequence content;

    LogInfo(CharSequence content) {
        time = System.currentTimeMillis();
        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(new Date(time));
        this.content = content;
    }

    long getTime() {
        return time;
    }

    String getTimeFormat() {
        return timeFormat;
    }

    CharSequence getContent() {
        return content;
    }
}
