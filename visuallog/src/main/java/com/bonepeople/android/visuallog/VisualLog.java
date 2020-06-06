package com.bonepeople.android.visuallog;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

import java.util.ArrayList;
import java.util.Objects;

public class VisualLog {
    static final String TAG = "VisualLog";
    static final String UPDATE_LOG = "VisualLog_UPDATE_LOG";
    static ArrayList<LogInfo> data = new ArrayList<>();
    private static ControlWindow controlWindow;
    private static boolean init = false;

    public static void init(@NonNull Context context) {
        Objects.requireNonNull(context);
        LocalBroadcastUtil.init(context);
        init = true;
    }

    public synchronized static void log(@NonNull CharSequence content) {
        checkInit();
        if (TextUtils.isEmpty(content))
            throw new IllegalArgumentException("请提供正常的日志内容");
        data.add(new LogInfo(content));
        LocalBroadcastUtil.sendBroadcast(UPDATE_LOG);
    }

    public synchronized static void clear() {
        checkInit();
        data.clear();
        LocalBroadcastUtil.sendBroadcast(UPDATE_LOG);
    }

    public static boolean showFloatWindow(@NonNull Context context) {
        Objects.requireNonNull(context);
        checkInit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(intent);
                return false;
            }
        }
        if (controlWindow == null)
            controlWindow = new ControlWindow(context);
        return controlWindow.show();
    }

    public static boolean hideFloatWindow() {
        if (controlWindow == null)
            return false;
        return controlWindow.hide();
    }

    public static void destroyFloatWindow() {
        if (controlWindow == null)
            return;
        controlWindow.destroy();
        controlWindow = null;
    }

    static void checkInit() {
        if (!init)
            throw new IllegalStateException("使用日志功能前必须先调用init(Context)方法进行初始化");
    }
}
