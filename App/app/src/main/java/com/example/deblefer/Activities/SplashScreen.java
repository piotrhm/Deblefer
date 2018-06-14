package com.example.deblefer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.deblefer.R;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME_OUT = 500;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgress = findViewById(R.id.splash_screen_progress_bar);

        new Handler().postDelayed(() -> {
            fillBar();
            startActivity(new Intent(SplashScreen.this, TexasModuleActivity.class));
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void fillBar() {
        for (int progress = 0; progress < 100; progress += 1) {
            try {
                mProgress.setProgress(progress);
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
