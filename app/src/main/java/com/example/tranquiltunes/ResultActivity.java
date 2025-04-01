package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultText;
    private Button retryButton, binauralButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);
        retryButton = findViewById(R.id.retryButton);
        binauralButton = findViewById(R.id.binauralButton); // Initialize binauralButton

        // Retrieve the psychological state (String) from intent
        String state = getIntent().getStringExtra("STATE");

        if (state == null) {
            state = "UNKNOWN"; // Default value if the intent data is missing
        }

        resultText.setText("Your current Mental State: " + state);

        // Start Binaural Beats activity with selected state
        String finalState = state;
        binauralButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, BinauralBeatsActivity.class);
            intent.putExtra("STATE", finalState);
            startActivity(intent);
        });

        // Restart the questionnaire
        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            finish();
        });
    }
}