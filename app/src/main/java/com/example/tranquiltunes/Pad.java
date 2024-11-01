package com.example.tranquiltunes;


import android.annotation.SuppressLint;
import android.os.Bundle;

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

public class Pad extends AppCompatActivity {
    RecyclerView padrecyclerView;
    DatabaseReference paddatabase;
    Padadapter padadapter;
    ArrayList<PadFunc> padlist;
    ArrayList<PadFunc> filteredPadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        padrecyclerView = findViewById(R.id.padlist);
        paddatabase = FirebaseDatabase.getInstance().getReference("pads");
        padrecyclerView.setHasFixedSize(true);
        padrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        padlist = new ArrayList<>();
        filteredPadList = new ArrayList<>();
        padadapter = new Padadapter(this, filteredPadList);
        padrecyclerView.setAdapter(padadapter);

        // Retrieve the selected pad category from intent
        String selectedEmotion = getIntent().getStringExtra("selectedEmotion");

        paddatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                padlist.clear();
                filteredPadList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PadFunc padfunc = dataSnapshot.getValue(PadFunc.class);
                    padlist.add(padfunc);

                    // Filter pads that match the selected category
                    if (padfunc != null && padfunc.getPaddescription().equalsIgnoreCase(selectedEmotion)) {
                        filteredPadList.add(padfunc);
                    }
                }
                padadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }
}
