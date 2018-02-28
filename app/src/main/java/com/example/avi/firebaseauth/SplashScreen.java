package com.example.avi.firebaseauth;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2500;
    ImageView logoImg;
    TextView logoTxt;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmerLogo);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LogIn.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        logoImg = (ImageView) findViewById(R.id.logoSplashImg);
        logoTxt = (TextView) findViewById(R.id.logoText);
        shimmerFrameLayout.startShimmerAnimation();
        //animation = AnimationUtils.loadAnimation(getApplicationContext(),
          //      R.anim.logo_blink);
        //logoTxt.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }
}
