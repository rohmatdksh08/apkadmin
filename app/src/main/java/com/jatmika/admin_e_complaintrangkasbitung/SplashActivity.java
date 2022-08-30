package com.jatmika.admin_e_complaintrangkasbitung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.jatmika.admin_e_complaintrangkasbitung.SharePref.SharePref;

public class SplashActivity extends AppCompatActivity {

    Animation fromnot;
    RelativeLayout relativeLayout;
    public static int splashInterval = 3000;
    SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        relativeLayout = findViewById(R.id.relative1);
        fromnot = AnimationUtils.loadAnimation(this, R.anim.fromnot);
        relativeLayout.startAnimation(fromnot);
        sharePref = new SharePref(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharePref.getStatusLogin() == true) {
                    Intent a = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(a);

                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                this.finish();
            }

            private void finish() {

            }
        }, splashInterval);
    }
}
