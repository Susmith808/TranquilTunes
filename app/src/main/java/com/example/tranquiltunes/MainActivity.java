package com.example.tranquiltunes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.button);

//Retrieve atmosname from SharedPreferences and pass it to Atmosengine
        SharedPreferences sharedATMOSPreferences = getSharedPreferences("AtmosPreferences", MODE_PRIVATE);
        String atmosname = sharedATMOSPreferences.getString("selectedAtmosname", "defaultAtmos"); // "defaultAtmos" as fallback

        SharedPreferences sharedPADSPreferences = getSharedPreferences("PadsPreferences", MODE_PRIVATE);
        String padName = sharedPADSPreferences.getString("selectedpadName", "defaultPads"); // "defaultAtmos" as fallback

        playButton.setOnClickListener(v -> {
            // Play audio from both Atmosengine and Padengine simultaneously
            Atmosengine.playAudio(this, atmosname); // Replace "forest" with your desired atmos name
            Padengine.playAudio(this, padName); // Replace "space" with your desired pad name
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Atmosengine.stopAudio(); // Stop and release all media when activity is destroyed
        Padengine.stopAudio(); // Stop pad audio if needed
    }
}
