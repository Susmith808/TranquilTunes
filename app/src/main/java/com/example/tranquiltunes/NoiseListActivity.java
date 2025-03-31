package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class NoiseListActivity extends AppCompatActivity {

    private String[] noises = {"White Noise", "Pink Noise", "Brown Noise"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_list);

        ListView listView = findViewById(R.id.noise_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noises);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String selectedNoise = noises[position].toLowerCase().replace(" ", "");
            Intent intent = new Intent(NoiseListActivity.this, NoiseGeneratorActivity.class);
            intent.putExtra("noiseType", selectedNoise);
            startActivity(intent);
        });
    }
}
