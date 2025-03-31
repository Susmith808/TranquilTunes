package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class IsochronicToneListActivity extends AppCompatActivity {

    private ListView toneListView;

    // Ordered list of Isochronic Tones and their frequencies
    private static final Map<String, Double[]> ISOCHRONIC_TONES = new LinkedHashMap<>();
    static {
        ISOCHRONIC_TONES.put("Deep Sleep (Delta - 3 Hz)", new Double[]{150.0, 3.0});
        ISOCHRONIC_TONES.put("Relaxation (Theta - 6 Hz)", new Double[]{300.0, 6.0});
        ISOCHRONIC_TONES.put("Focus & Concentration (Alpha - 10 Hz)", new Double[]{400.0, 10.0});
        ISOCHRONIC_TONES.put("Energy Boost (Beta - 16 Hz)", new Double[]{600.0, 16.0});
        ISOCHRONIC_TONES.put("Memory & Learning (Gamma - 40 Hz)", new Double[]{800.0, 40.0});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isochronic_tone_list);

        toneListView = findViewById(R.id.toneListView);

        final String[] toneNames = ISOCHRONIC_TONES.keySet().toArray(new String[0]);

        // Set up ListView Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toneNames);
        toneListView.setAdapter(adapter);

        // Handle Item Clicks
        toneListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTone = toneNames[position];
            Double[] frequencies = ISOCHRONIC_TONES.get(selectedTone);

            if (frequencies != null) {
                Intent intent = new Intent(IsochronicToneListActivity.this, IsochronicTonePlayerActivity.class);
                intent.putExtra("toneName", selectedTone);
                intent.putExtra("toneFrequency", frequencies[0]);
                intent.putExtra("pulseRate", frequencies[1]);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error: Frequency data unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
