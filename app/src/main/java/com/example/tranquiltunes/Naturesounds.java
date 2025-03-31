package com.example.tranquiltunes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Naturesounds extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    private DatabaseReference databaseReference;
    private final List<AudioModel> audioList = new ArrayList<>();
    private NatureSoundPicker natureSoundPicker;
    private Button selectSoundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naturesounds);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        audioAdapter = new AudioAdapter(this, audioList);
        recyclerView.setAdapter(audioAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("natureSounds");
        natureSoundPicker = new NatureSoundPicker(this);

        selectSoundButton = findViewById(R.id.selectSoundButton);
        selectSoundButton.setOnClickListener(v -> natureSoundPicker.showSoundPickerDialog());

        fetchAudioFiles();
    }

    private void fetchAudioFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                audioList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AudioModel audioFile = dataSnapshot.getValue(AudioModel.class);
                    if (audioFile != null) {
                        audioList.add(audioFile);
                    }
                }
                audioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Naturesounds.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
