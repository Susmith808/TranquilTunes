package com.example.tranquiltunes;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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

public class Soundscape extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    Atmosadapter atmosadapter;
    ArrayList<Func> soundscapelist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundscape);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        recyclerView=findViewById(R.id.soundscapelist);
        database = FirebaseDatabase.getInstance().getReference("atmospheres");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        soundscapelist = new ArrayList<>();
        atmosadapter=new Atmosadapter(this,soundscapelist);
        recyclerView.setAdapter(atmosadapter);

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                soundscapelist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Func func = dataSnapshot.getValue(Func.class);
                    soundscapelist.add(func);


                }
                atmosadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(Soundscape.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }





        });
    }
}