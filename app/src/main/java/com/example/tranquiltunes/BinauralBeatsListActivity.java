package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BinauralBeatsListActivity extends AppCompatActivity {

    private ListView beatListView;
    private List<BinauralBeat> binauralBeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural_beats_list);

        beatListView = findViewById(R.id.beatListView);

        // Initialize list of binaural beats
        binauralBeats = new ArrayList<>();
        binauralBeats.add(new BinauralBeat("Delta (Deep Sleep)", "Delta waves (0.5–4 Hz) help with deep sleep and healing.", 2));
        binauralBeats.add(new BinauralBeat("Theta (Creativity)", "Theta waves (4–8 Hz) are linked to deep relaxation and creativity.", 6));
        binauralBeats.add(new BinauralBeat("Alpha (Mindfulness)", "Alpha waves (8–14 Hz) promote relaxation and mindfulness.", 10));
        binauralBeats.add(new BinauralBeat("Beta (Focus)", "Beta waves (14–30 Hz) boost focus and cognitive function.", 18));
        binauralBeats.add(new BinauralBeat("Gamma (Higher Activity)", "Gamma waves (30–100 Hz) are linked to heightened perception and memory.", 40));

        // Set up custom adapter
        BeatListAdapter adapter = new BeatListAdapter(this, binauralBeats);
        beatListView.setAdapter(adapter);

        // Handle item click
        beatListView.setOnItemClickListener((parent, view, position, id) -> {
            BinauralBeat selectedBeat = binauralBeats.get(position);

            // Start new activity and pass data
            Intent intent = new Intent(BinauralBeatsListActivity.this, BinauralPresets.class);
            intent.putExtra("BEAT_NAME", selectedBeat.getName());
            intent.putExtra("BEAT_FREQUENCY", selectedBeat.getFrequency());
            startActivity(intent);
        });
    }
}
