package com.bonepeople.android.visuallog.simple;

import android.app.Application;

import com.bonepeople.android.visuallog.VisualLog;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VisualLog.init(this);
    }
}
