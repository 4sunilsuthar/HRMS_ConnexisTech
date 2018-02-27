package com.lms.admin.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imgOrgLogo, imgAppLogo;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgAppLogo = findViewById(R.id.imgAppName);
        imgOrgLogo = findViewById(R.id.imgCompanyLogo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        linearLayout = findViewById(R.id.layoutLinear);
        linearLayout.setAnimation(animation);
        /*imgOrgLogo.startAnimation(animation);
        imgAppLogo.startAnimation(animation);*/

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();

                    super.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();

    }
}
