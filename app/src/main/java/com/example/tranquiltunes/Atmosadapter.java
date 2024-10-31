package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Set up button click listener
        holder.chooseatmosbtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Pad.class);

                context.startActivity(intent);
                // Get the data of the item at this position
                String atmosname = func.getAtmosname();
                String atmosdescription = func.getAtmosdescription();
                intent.putExtra("atmosname", atmosname); // Pass atmosname
                context.startActivity(intent);

                // Here, you can use the strings (padName and padDescription)
                // For example, you can show a Toast or use them in other logic
                Toast.makeText(context, "Soundscape: " + atmosname + "\nDescription: " + atmosdescription, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView atmosname, atmosdescription;
        Button chooseatmosbtnid;  // Button reference

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            atmosname = itemView.findViewById(R.id.atmosnameid);
            atmosdescription = itemView.findViewById(R.id.atmosdescid);
            chooseatmosbtnid= itemView.findViewById(R.id.chooseatmosbtnid);  // Initialize the button
        }
    }
}
