package com.bonepeople.android.visuallog;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

import java.util.ArrayList;
import java.util.Objects;

public class VisualLog {
    static final String UPDATE_LOG = "VisualLog_UPDATE_LOG";
    static ArrayList<LogInfo> data = new ArrayList<>();
    private static boolean init = false;

    public static void init(@NonNull Context context) {
        Objects.requireNonNull(context);
        LocalBroadcastUtil.init(context);
        init = true;
    }

    public synchronized static void log(@NonNull CharSequence content) {
        if (!init)
            throw new IllegalStateException("使用日志功能前必须先调用init(Context)方法进行初始化");
        if (TextUtils.isEmpty(content))
            throw new IllegalArgumentException("请提供正常的日志内容");
        data.add(new LogInfo(content));
        LocalBroadcastUtil.sendBroadcast(UPDATE_LOG);
    }

    public synchronized static void clear() {
        if (!init)
            throw new IllegalStateException("使用日志功能前必须先调用init(Context)方法进行初始化");
        data.clear();
        LocalBroadcastUtil.sendBroadcast(UPDATE_LOG);
    }
}
