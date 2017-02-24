package top.lemonsoda.gunners;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import top.lemonsoda.gunners.news.NewsIndexActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 1500;

    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NewsIndexActivity.class);
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
