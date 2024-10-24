package com.example.tranquiltunes;


import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class Emotion extends AppCompatActivity {
    RecyclerView emorecyclerView;
    DatabaseReference emodatabase;
    Emoadapter emoadapter;
    ArrayList<EmoFunc> emolist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        emorecyclerView=findViewById(R.id.emolist);
        emodatabase = FirebaseDatabase.getInstance().getReference("emotions");
        emorecyclerView.setHasFixedSize(true);
        emorecyclerView.setLayoutManager(new LinearLayoutManager(this));

        emolist = new ArrayList<>();
        emoadapter=new Emoadapter(this,emolist);
        emorecyclerView.setAdapter(emoadapter);

        emodatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emolist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    EmoFunc emofunc = dataSnapshot.getValue(EmoFunc.class);
                    emolist.add(emofunc);


                }
                emoadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(Emotion.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }





        });
    }
}