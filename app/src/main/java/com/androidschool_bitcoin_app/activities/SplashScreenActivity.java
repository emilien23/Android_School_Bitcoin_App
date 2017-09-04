package com.androidschool_bitcoin_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.androidschool_bitcoin_app.R;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Эмиль on 22.08.2017.
 */

public class SplashScreenActivity extends AppCompatActivity{
    ImageView loadingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.ac_splash_sceen);

        loadingAnimatoin();

    }
    public void loadingAnimatoin(){
        loadingImage = (ImageView) findViewById(R.id.loading_image);
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        rotate.setDuration(900);
        rotate.setRepeatCount(Animation.INFINITE);
        loadingImage.setAnimation(rotate);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this,BitcoinListActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, 2000);
    }

}
