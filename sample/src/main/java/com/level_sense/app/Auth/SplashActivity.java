package com.level_sense.app.Auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;

import com.google.android.gms.flags.Flag;
import com.level_sense.app.R;
import com.level_sense.app.Session.SessionParam;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Upon interacting with UI controls, delay any scheduled hide()
        //operations to prevent the jarring behavior of controls going away
        //while interacting with the UI.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("splashs", SessionParam.getSessionKey(SplashActivity.this) + "//");

                if (SessionParam.getSessionKey(SplashActivity.this) != null && SessionParam.getSessionKey(SplashActivity.this).length() > 0) {

                    Log.e("sdsds", "Dashboard");
                    startActivity(DashBoardActivity.getIntent(SplashActivity.this));
                   finish();


                } else {

                    Log.e("sdsds", "Login");
                    //startActivity(LoginActivity.getIntent(SplashActivity.this));
                    startActivity(new Intent(SplashActivity.this, Hardware.class));
                    finish();
                }
            }

        }, 1000);

    }

}
