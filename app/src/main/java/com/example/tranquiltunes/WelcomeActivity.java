package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button startButton, helpButton, exitButton, solfeggioButton, isochronicButton, bbButton, noiseButton, natureButton;

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finishAffinity(); // Close all activities
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize Buttons
        natureButton = findViewById(R.id.natureButton);
        startButton = findViewById(R.id.startbuttonid);
        helpButton = findViewById(R.id.helpbuttonid);
        exitButton = findViewById(R.id.exitButtonid);
        solfeggioButton = findViewById(R.id.solfeggioButton);
        isochronicButton = findViewById(R.id.isochronicButton);
        noiseButton = findViewById(R.id.noiseButton);
        bbButton = findViewById(R.id.binauralBeatsButton);

        // Set Click Listeners
        natureButton.setOnClickListener(view -> startActivity(new Intent(this, Naturesounds.class)));
        startButton.setOnClickListener(view -> startActivity(new Intent(this, Emotion.class)));
        helpButton.setOnClickListener(view -> startActivity(new Intent(this, BinauralBeatsListActivity.class)));
        bbButton.setOnClickListener(view -> startActivity(new Intent(this, QuestionnaireActivity.class)));
        solfeggioButton.setOnClickListener(view -> startActivity(new Intent(this, SolfeggioListActivity.class)));
        isochronicButton.setOnClickListener(view -> startActivity(new Intent(this, IsochronicToneListActivity.class)));
        noiseButton.setOnClickListener(view -> startActivity(new Intent(this, NoiseListActivity.class)));
        exitButton.setOnClickListener(view -> showExitDialog());
    }
}
