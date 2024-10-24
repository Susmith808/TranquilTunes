package com.example.tranquiltunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Set up button click listener
        holder.choosepadbtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data of the item at this position
                String padName = padfunc.getPadname();
                String padDescription = padfunc.getPaddescription();

                // Here, you can use the strings (padName and padDescription)
                // For example, you can show a Toast or use them in other logic
                Toast.makeText(padcontext, "Pad Name: " + padName + "\nDescription: " + padDescription, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return padlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView padname, paddescription;
        Button choosepadbtnid;  // Button reference

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            padname = itemView.findViewById(R.id.padnameid);
            paddescription = itemView.findViewById(R.id.paddescid);
            choosepadbtnid = itemView.findViewById(R.id.choosepadbtnid);  // Initialize the button
        }
    }
}
