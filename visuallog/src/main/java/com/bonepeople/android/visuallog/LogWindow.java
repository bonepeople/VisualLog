package com.bonepeople.android.visuallog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bonepeople.android.dimensionutil.DimensionUtil;

class LogWindow extends FrameLayout {
    private static final int STATUS_EMPTY = 0;
    private static final int STATUS_SHOW = 1;
    private static final int STATUS_HIDE = 2;
    private WindowManager window;
    private WindowManager.LayoutParams layoutParams;
    private int status = STATUS_EMPTY;

    public LogWindow(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        window = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        setBackgroundColor(0xF038B0DE);

        LogListView logListView = new LogListView(getContext());
        addView(logListView);
    }

    public void show() {
        if (window == null) {
            Toast.makeText(getContext(), "窗口未能正常显示，找不到正常的容器", Toast.LENGTH_LONG).show();
            Log.w(VisualLog.TAG, "窗口未能正常显示，找不到正常的容器");
            return;
        }
        switch (status) {
            case STATUS_EMPTY: {
                layoutParams = new WindowManager.LayoutParams();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }

                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                layoutParams.width = DimensionUtil.getDisplayWidth() - DimensionUtil.getPx(40);
                layoutParams.height = DimensionUtil.getDisplayHeight() - DimensionUtil.getPx(250);
                layoutParams.x = DimensionUtil.getPx(20);
                layoutParams.y = DimensionUtil.getPx(20);
                layoutParams.gravity = Gravity.START | Gravity.TOP;
                layoutParams.format = PixelFormat.TRANSLUCENT;

                try {
                    window.addView(this, layoutParams);
                    status = STATUS_SHOW;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "窗口未能正常显示，请检查权限", Toast.LENGTH_LONG).show();
                    Log.w(VisualLog.TAG, "窗口未能正常显示：" + e.getMessage());
                }
                break;
            }
            case STATUS_HIDE: {
                setVisibility(VISIBLE);
                status = STATUS_SHOW;
                break;
            }
        }
    }

    public void hide() {
        if (status == STATUS_SHOW) {
            setVisibility(GONE);
            status = STATUS_HIDE;
        }
    }

    public void destroy() {
        if (status != STATUS_EMPTY) {
            window.removeView(this);
            status = STATUS_EMPTY;
        }
    }
}
