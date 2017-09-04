package com.androidschool_bitcoin_app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Эмиль on 22.08.2017.
 */

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}