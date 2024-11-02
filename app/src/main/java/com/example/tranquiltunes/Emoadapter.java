package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
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

public class Emoadapter extends RecyclerView.Adapter<Emoadapter.MyViewHolder> {

    Context emocontext;
    ArrayList<EmoFunc> emolist;

    public Emoadapter(Context emocontext, ArrayList<EmoFunc> emolist) {
        this.emocontext = emocontext;
        this.emolist = emolist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(emocontext).inflate(R.layout.emotionitems, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmoFunc emofunc = emolist.get(position);

        // Set the values to the text views
        holder.emoname.setText(emofunc.getEmoname());
        holder.emodescription.setText(emofunc.getEmodescription());


        Glide.with(emocontext)
                .load(emofunc.getImageURL())
                .centerCrop()
                .into(holder.emobackgroundImage);




        // Set up button click listener
        holder.chooseemobtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(emocontext, Pad.class);
                emocontext.startActivity(intent);
                // Get the data of the item at this position
                String emoName = emofunc.getEmoname();
                String emoDescription = emofunc.getEmodescription();

                GlobalData.getInstance().setSelectedEmotion(emoName);

                intent.putExtra("selectedEmotion", emoName); // Pass atmosname
                emocontext.startActivity(intent);

                // Here, you can use the strings (padName and padDescription)
                // For example, you can show a Toast or use them in other logic
                Toast.makeText(emocontext, "Emotion: " + emoName + "\nDescription: " + emoDescription, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return emolist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView emoname, emodescription;
        Button chooseemobtnid;
        ImageView emobackgroundImage;// Button reference

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            emoname = itemView.findViewById(R.id.emonameid);
            emodescription = itemView.findViewById(R.id.emodescid);
            chooseemobtnid = itemView.findViewById(R.id.chooseemobtnid);  // Initialize the button
            emobackgroundImage = itemView.findViewById(R.id.emobackgroundImageid);  // Initialize ImageView

        }
    }
}
