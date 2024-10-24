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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        padrecyclerView=findViewById(R.id.padlist);
        paddatabase = FirebaseDatabase.getInstance().getReference("pads");
        padrecyclerView.setHasFixedSize(true);
        padrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        padlist = new ArrayList<>();
        padadapter=new Padadapter(this,padlist);
        padrecyclerView.setAdapter(padadapter);

        paddatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    PadFunc padfunc = dataSnapshot.getValue(PadFunc.class);
                    padlist.add(padfunc);
                }
                padadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){


            }



        });
    }
}