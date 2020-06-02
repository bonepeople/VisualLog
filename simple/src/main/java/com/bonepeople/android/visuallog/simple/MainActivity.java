package com.bonepeople.android.visuallog.simple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bonepeople.android.visuallog.VisualLog;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final int MSG_PRINT_LOG = 1;
    private Switch switch_auto;

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
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(MSG_PRINT_LOG);
        super.onDestroy();
    }

    public void send(View view) {
        VisualLog.log("这是一条手动发送的日志");
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
}
