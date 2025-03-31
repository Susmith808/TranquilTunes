package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolfeggioListActivity extends AppCompatActivity {

    private final String[] frequencies = {"174 Hz", "285 Hz", "396 Hz", "417 Hz", "528 Hz", "639 Hz", "741 Hz", "852 Hz", "963 Hz"};
    private final String[] descriptions = {
            "Reduces pain and stress, promotes emotional healing.",
            "Enhances energy and rejuvenation.",
            "Liberates fear and guilt.",
            "Facilitates change and transitions.",
            "Transforms and repairs DNA.",
            "Encourages harmony and connection.",
            "Cleanses toxins and negativity.",
            "Expands consciousness and awareness.",
            "Promotes spiritual enlightenment and unity."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solfeggio_list);

        ListView listView = findViewById(R.id.listView);
        List<Map<String, String>> data = new ArrayList<>();

        for (int i = 0; i < frequencies.length; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("title", frequencies[i]);
            item.put("description", descriptions[i]);
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "description"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(SolfeggioListActivity.this, Solfeggio.class);
            intent.putExtra("frequency", frequencies[position]);
            startActivity(intent);
        });
    }
}
