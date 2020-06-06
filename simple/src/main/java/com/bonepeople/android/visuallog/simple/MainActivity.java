package com.bonepeople.android.visuallog.simple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bonepeople.android.visuallog.VisualLog;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final int MSG_PRINT_LOG = 1;
    private Switch switch_auto;
    private Button button_float;
    private boolean showFloatWindow = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (switch_auto.isChecked()) {
                VisualLog.log("自动记录日志");
                handler.sendEmptyMessageDelayed(MSG_PRINT_LOG, 3000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_auto = findViewById(R.id.switch_auto);
        switch_auto.setOnCheckedChangeListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_clear).setOnClickListener(this);
        button_float = findViewById(R.id.button_float);
        button_float.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(MSG_PRINT_LOG);
        VisualLog.destroyFloatWindow();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            VisualLog.log("开始自动发送日志");
            handler.sendEmptyMessage(MSG_PRINT_LOG);
        } else {
            VisualLog.log("停止自动发送日志");
            handler.removeMessages(MSG_PRINT_LOG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send: {
                VisualLog.log("这是一条手动发送的日志");
                break;
            }
            case R.id.button_clear: {
                VisualLog.clear();
                break;
            }
            case R.id.button_float: {
                if (showFloatWindow) {
                    if (VisualLog.hideFloatWindow()) {
                        showFloatWindow = false;
                        button_float.setText("显示悬浮窗");
                    }
                } else {
                    if (VisualLog.showFloatWindow(this)) {
                        showFloatWindow = true;
                        button_float.setText("隐藏悬浮窗");
                    }
                }
                break;
            }
        }
    }
}
