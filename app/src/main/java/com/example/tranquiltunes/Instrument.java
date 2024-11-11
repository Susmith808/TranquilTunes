package com.example.tranquiltunes;

import android.os.Bundle;
import android.view.View;

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

public class Instrument extends AppCompatActivity {

    private RecyclerView instrumentRecyclerView;
    private DatabaseReference instrumentDatabase;
    private InstrumentAdapter instrumentAdapter;
    private ArrayList<InstrumentFunc> instrumentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        instrumentRecyclerView = findViewById(R.id.instrumentlist);
        instrumentRecyclerView.setHasFixedSize(true);
        instrumentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        instrumentList = new ArrayList<>();
        instrumentAdapter = new InstrumentAdapter(this, instrumentList);
        instrumentRecyclerView.setAdapter(instrumentAdapter);

        // Firebase database reference
        instrumentDatabase = FirebaseDatabase.getInstance().getReference("instruments");

        // Fetch data from Firebase and update RecyclerView
        instrumentDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                instrumentList.clear(); // Clear list to avoid duplicates
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    InstrumentFunc instrument = dataSnapshot.getValue(InstrumentFunc.class);
                    if (instrument != null) {
                        instrumentList.add(instrument);
                    }
                }
                instrumentAdapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });
    }
}
