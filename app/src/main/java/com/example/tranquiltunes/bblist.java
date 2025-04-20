package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class bblist extends AppCompatActivity {

    ListView listView;
    ArrayList<String> binauralFrequencies;
    TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bblist);

        listView = findViewById(R.id.listView);
        descriptionText = findViewById(R.id.descriptionText);

        // Set description text explaining binaural beats
        descriptionText.setText("Binaural beats are auditory illusions created when two tones of slightly different frequencies " +
                "are played in each ear. They are believed to enhance relaxation, focus, meditation, and sleep quality.\n\n" +
                "🌟 Benefits of Binaural Beats:\n" +
                "- Improves concentration and memory\n" +
                "- Promotes deep relaxation and reduces stress\n" +
                "- Helps in better sleep and meditation\n" +
                "- Enhances creativity and cognitive function\n\n" +
                "🧘 Who Can Benefit?\n" +
                "✅ Students & Professionals (Focus & Learning)\n" +
                "✅ Meditation Practitioners (Relaxation & Mindfulness)\n" +
                "✅ People with Anxiety or Insomnia (Sleep & Calmness)\n");

        // List of binaural frequency categories
        binauralFrequencies = new ArrayList<>();
        binauralFrequencies.add("Delta (2 Hz) - Deep Sleep & Healing");
        binauralFrequencies.add("Theta (5 Hz) - Meditation & Intuition");
        binauralFrequencies.add("Alpha (10 Hz) - Relaxation & Creativity");
        binauralFrequencies.add("Beta (18 Hz) - Focus & Problem Solving");
        binauralFrequencies.add("Gamma (40 Hz) - Cognitive Boost & Awareness");

        // Adapter to display frequencies in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, binauralFrequencies);
        listView.setAdapter(adapter);

        // Handle list item click to navigate to BinauralActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFrequency = binauralFrequencies.get(position);
                Intent intent = new Intent(bblist.this, BinauralActivity.class);
                intent.putExtra("frequency", selectedFrequency);
                startActivity(intent);
            }
        });
    }
}
