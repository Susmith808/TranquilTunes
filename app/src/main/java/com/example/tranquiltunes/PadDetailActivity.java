package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PadDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad_detail);

        // Get the pad name passed from the RecyclerView item
        Intent intent = getIntent();
        String padname = intent.getStringExtra("padname");

        // Perform different actions based on the pad name
        switch (padname) {
            case "Space":
                // Action for Rain Pad
                Toast.makeText(this, "Space Pad Selected", Toast.LENGTH_SHORT).show();
                // For example, play a rain sound or display details
                break;
            case "Thunder Pad":
                // Action for Thunder Pad
                Toast.makeText(this, "Thunder Pad Selected", Toast.LENGTH_SHORT).show();
                // Play thunder sound or display details
                break;
            case "Ocean Pad":
                // Action for Ocean Pad
                Toast.makeText(this, "Ocean Pad Selected", Toast.LENGTH_SHORT).show();
                // Play ocean sound or display details
                break;
            default:
                // Action for unknown or other pads
                Toast.makeText(this, "Unknown Pad Selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
