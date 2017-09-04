package com.androidschool_bitcoin_app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.androidschool_bitcoin_app.R;

/**
 * Created by Эмиль on 25.08.2017.
 */

public class PrefActivity extends PreferenceActivity {
    SharedPreferences sp;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

}