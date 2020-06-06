package com.bonepeople.android.visuallog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bonepeople.android.dimensionutil.DimensionUtil;

class ControlWindow extends LinearLayout implements View.OnClickListener {
    private static final int STATUS_EMPTY = 0;
    private static final int STATUS_SHOW = 1;
    private static final int STATUS_HIDE = 2;
    private WindowManager window;
    private WindowManager.LayoutParams layoutParams;
    private int touchSlop;
    private LogWindow logWindow;
    private int status = STATUS_EMPTY;
    private float pointX, pointY;
    private float layoutX, layoutY;
    private boolean moving = false;
    private boolean showLog = false;

    public ControlWindow(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        window = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        setBackgroundResource(android.R.drawable.ic_dialog_info);
        setBackgroundTintList(ColorStateList.valueOf(0xCCCCCCCC));
        setOnClickListener(this);
    }

    public boolean show() {
        if (window == null) {
            Toast.makeText(getContext(), "窗口未能正常显示，找不到正常的容器", Toast.LENGTH_LONG).show();
            Log.w(VisualLog.TAG, "窗口未能正常显示，找不到正常的容器");
            return false;
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
                layoutParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

                layoutParams.width = DimensionUtil.getPx(50);
                layoutParams.height = DimensionUtil.getPx(50);
                layoutParams.gravity = Gravity.START | Gravity.TOP;
                layoutParams.x = DimensionUtil.getDisplayWidth() - (layoutParams.width + DimensionUtil.getPx(10));
                layoutParams.y = DimensionUtil.getDisplayHeight() - (layoutParams.height + DimensionUtil.getPx(50));
                layoutParams.format = PixelFormat.TRANSLUCENT;

                try {
                    window.addView(this, layoutParams);
                    status = STATUS_SHOW;
                    return true;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "窗口未能正常显示，请检查权限", Toast.LENGTH_LONG).show();
                    Log.w(VisualLog.TAG, "窗口未能正常显示：" + e.getMessage());
                }
                break;
            }
            case STATUS_HIDE: {
                setVisibility(VISIBLE);
                status = STATUS_SHOW;
                return true;
            }
        }
        return false;
    }

    public boolean hide() {
        if (status == STATUS_SHOW) {
            hideLogWindow();
            setVisibility(GONE);
            status = STATUS_HIDE;
            return true;
        }
        return false;
    }

    public void destroy() {
        if (status != STATUS_EMPTY) {
            destroyLogWindow();
            window.removeView(this);
            status = STATUS_EMPTY;
        }
    }

    private void showLogWindow() {
        if (logWindow == null)
            logWindow = new LogWindow(getContext());
        logWindow.show();
        layoutParams.x = DimensionUtil.getDisplayWidth() - (layoutParams.width + DimensionUtil.getPx(10));
        layoutParams.y = DimensionUtil.getDisplayHeight() - (layoutParams.height + DimensionUtil.getPx(50));
        window.updateViewLayout(this, layoutParams);
        showLog = true;
    }

    private void hideLogWindow() {
        if (logWindow == null)
            return;
        logWindow.hide();
        showLog = false;
    }

    private void destroyLogWindow() {
        if (logWindow == null)
            return;
        logWindow.destroy();
        logWindow = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pointX = event.getRawX();
                pointY = event.getRawY();
                layoutX = layoutParams.x;
                layoutY = layoutParams.y;
                moving = false;
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                float moveX = event.getRawX() - pointX;
                float moveY = event.getRawY() - pointY;
                if (Math.abs(moveX) > touchSlop || Math.abs(moveY) > touchSlop) {
                    moving = true;
                }
                if (moving) {
                    layoutParams.x = (int) (layoutX + moveX);
                    layoutParams.y = (int) (layoutY + moveY);
                    window.updateViewLayout(this, layoutParams);
                }
                return true;
            }
            case MotionEvent.ACTION_UP: {
                if (moving) {
                    if (layoutParams.x > DimensionUtil.getDisplayWidth() - layoutParams.width)
                        layoutParams.x = DimensionUtil.getDisplayWidth() - layoutParams.width;
                    if (layoutParams.y > DimensionUtil.getDisplayHeight() - layoutParams.height)
                        layoutParams.y = DimensionUtil.getDisplayHeight() - layoutParams.height;
                    if (layoutParams.x < 0)
                        layoutParams.x = 0;
                    if (layoutParams.y < 0)
                        layoutParams.y = 0;
                    window.updateViewLayout(this, layoutParams);
                } else {
                    performClick();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onClick(View v) {
        if (showLog) {
            hideLogWindow();
        } else {
            showLogWindow();
        }
    }
}
