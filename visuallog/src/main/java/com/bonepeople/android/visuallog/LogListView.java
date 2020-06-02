package com.bonepeople.android.visuallog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class LogListView extends RecyclerView {
    private LogAdapter adapter;
    private Receiver receiver = new Receiver();

    public LogListView(@NonNull Context context) {
        this(context, null);
    }

    public LogListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        setLayoutManager(layoutManager);
        adapter = new LogAdapter();
        setAdapter(adapter);
        VisualLog.checkInit();
        LocalBroadcastUtil.registerReceiver(receiver, VisualLog.UPDATE_LOG);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LocalBroadcastUtil.registerReceiver(receiver, VisualLog.UPDATE_LOG);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastUtil.unregisterReceiver(receiver);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
            scrollToPosition(adapter.getItemCount() - 1);
        }
    }
}
