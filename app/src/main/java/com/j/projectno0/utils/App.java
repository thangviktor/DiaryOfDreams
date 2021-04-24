package com.j.projectno0.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.j.projectno0.R;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
