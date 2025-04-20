package com.example.tranquiltunes;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tranquiltunes.R;

public class HelpActivity extends AppCompatActivity {

    String[] helpTopics = {
            "Ambient Music",
            "Solfeggio Frequencies",
            "Isochronic Tones",
            "Noise Generator",
            "Binaural Beats"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help2);

        ListView listView = findViewById(R.id.helpListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, helpTopics);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
                intent.putExtra("topic", helpTopics[position]);
                startActivity(intent);
            }
        });
    }
}
