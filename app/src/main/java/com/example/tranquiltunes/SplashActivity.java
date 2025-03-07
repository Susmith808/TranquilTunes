package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set your custom splash layout
        setContentView(R.layout.activity_splash);

        // Add a delay (for example, 3 seconds) to show splash screen before transitioning to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after splash screen duration
                Intent intent = new Intent(SplashActivity.this,WelcomeActivity.class);
                startActivity(intent);
                finish(); // Close the SplashActivity to prevent the user from returning to it
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
