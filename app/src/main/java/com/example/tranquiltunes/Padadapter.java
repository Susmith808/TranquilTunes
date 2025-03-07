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

public class Padadapter extends RecyclerView.Adapter<Padadapter.MyViewHolder> {

    Context padcontext;
    ArrayList<PadFunc> padlist;

    public Padadapter(Context padcontext, ArrayList<PadFunc> padlist) {
        this.padcontext = padcontext;
        this.padlist = padlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(padcontext).inflate(R.layout.paditems, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PadFunc padfunc = padlist.get(position);

        // Set the values to the text views
        holder.padname.setText(padfunc.getPadname());
        holder.paddescription.setText(padfunc.getPaddescription());

        // Load image if PadFunc has an image URL (assumes `getImageURL` is added to PadFunc)
        Glide.with(padcontext)
                .load(padfunc.getImageURL())
                .centerCrop()
                .into(holder.padbackgroundImage);

        // Set up button click listener
        holder.choosepadbtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(padcontext, Soundscape.class);

                // Pass pad name
                String padName = padfunc.getPadname();
                String padDescription = padfunc.getPaddescription();

                intent.putExtra("padname", padName);
                padcontext.startActivity(intent);

                // Store the selected pad name in SharedPreferences
                SharedPreferences sharedPADSPreferences = padcontext.getSharedPreferences("PadsPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPADSPreferences.edit();
                editor.putString("selectedpadName", padName);
                editor.apply();

                // Show a Toast message

            }
        });
    }

    @Override
    public int getItemCount() {
        return padlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView padname, paddescription;
        Button choosepadbtnid;
        ImageView padbackgroundImage; // Added ImageView reference

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            padname = itemView.findViewById(R.id.padnameid);
            paddescription = itemView.findViewById(R.id.paddescid);
            choosepadbtnid = itemView.findViewById(R.id.choosepadbtnid);
            padbackgroundImage = itemView.findViewById(R.id.padbackgroundImageid); // Initialize ImageView
        }
    }
}
