package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Atmosadapter extends RecyclerView.Adapter<Atmosadapter.MyViewHolder> {

    Context context;
    ArrayList<Func> list;

    public Atmosadapter(Context context, ArrayList<Func> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.soundscapeitems, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Func func = list.get(position);

        // Set the values to the text views
        holder.atmosname.setText(func.getAtmosname());
        holder.atmosdescription.setText(func.getAtmosdescription());

        // Load image from URL using Glide
        Glide.with(context)
                .load(func.getImageURL()) // Assuming getImageURL() provides the image URL from Firebase
                .centerCrop()
                .into(holder.atmosImageView);

        // Set up button click listener
        holder.chooseatmosbtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Instrument.class);
                String atmosname = func.getAtmosname();
                String atmosdescription = func.getAtmosdescription();
                intent.putExtra("atmosname", atmosname); // Pass atmosname
                context.startActivity(intent);

                // Store selected atmosname in SharedPreferences
                SharedPreferences sharedATMOSPreferences = context.getSharedPreferences("AtmosPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedATMOSPreferences.edit();
                editor.putString("selectedAtmosname", atmosname);
                editor.apply();

                // Display a Toast message with details

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView atmosname, atmosdescription;
        ImageView atmosImageView; // ImageView reference for the image
        Button chooseatmosbtnid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            atmosname = itemView.findViewById(R.id.atmosnameid);
            atmosdescription = itemView.findViewById(R.id.atmosdescid);
            atmosImageView = itemView.findViewById(R.id.atmosImageView); // Initialize ImageView
            chooseatmosbtnid = itemView.findViewById(R.id.chooseatmosbtnid);  // Initialize the button
        }
    }
}
