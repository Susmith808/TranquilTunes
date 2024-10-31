// AnotherActivity.java
package com.example.tranquiltunes;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button AtmosplayButton = findViewById(R.id.button);
        AtmosplayButton.setOnClickListener(v -> Atmosengine.playAudio(this, "beach"));
        Button PadplayButton = findViewById(R.id.button2);
        PadplayButton.setOnClickListener(v -> Atmosengine.playAudio(this, "beach"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Atmosengine.stopAudio(); // Stop and release media when activity is destroyed
    }
}
